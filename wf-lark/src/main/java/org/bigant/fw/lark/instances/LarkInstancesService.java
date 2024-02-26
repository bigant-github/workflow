package org.bigant.fw.lark.instances;

import com.google.gson.annotations.SerializedName;
import com.lark.oapi.Client;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.response.BaseResponse;
import com.lark.oapi.core.response.RawResponse;
import com.lark.oapi.core.token.AccessTokenType;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.core.utils.UnmarshalRespUtil;
import com.lark.oapi.service.approval.v4.model.*;
import com.oracle.tools.packager.IOUtils;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.LarkConfig;
import org.bigant.fw.lark.LarkConstant;
import org.bigant.fw.lark.process.LarkProcessService;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.form.ComponentConvert;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentParseAll;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.AttachmentComponent;
import org.bigant.wf.form.component.bean.DateComponent;
import org.bigant.wf.form.component.bean.DateRangeComponent;
import org.bigant.wf.form.component.bean.ImageComponent;
import org.bigant.wf.form.option.MultiSelectOption;
import org.bigant.wf.form.option.SelectOption;
import org.bigant.wf.instances.InstancesService;
import org.bigant.wf.instances.bean.InstancesPreview;
import org.bigant.wf.instances.bean.InstancesPreviewResult;
import org.bigant.wf.instances.bean.InstancesStart;
import org.bigant.wf.instances.bean.InstancesStartResult;
import org.bigant.wf.process.bean.ProcessDetail;
import org.bigant.wf.user.UserService;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 飞书审批流程service
 *
 * @author galen
 * @date 2024/2/1815:49
 */
@Slf4j
public class LarkInstancesService implements InstancesService {

    private LarkConfig larkConfig;

    private LarkProcessService larkProcessService;

    private UserService userService;

    private FormConvert formConvert;

    public LarkInstancesService(LarkConfig larkConfig, LarkProcessService larkProcessService, UserService userService) {
        this.larkConfig = larkConfig;
        this.userService = userService;
        this.larkProcessService = larkProcessService;
        formConvert = new FormConvert(larkConfig);
    }

    /**
     * 发起审批实例
     * 接口地址 https://open.feishu.cn/document/server-docs/approval-v4/instance/create?appId=cli_a52dc3cf4f3b500e
     *
     * @param instancesStart
     * @return
     */
    @Override
    public InstancesStartResult start(InstancesStart instancesStart) {
        // 构建client
        Client client = larkConfig.getClient();

        String userId = userService.getThirdPartyId(instancesStart.getUserId(), LarkConstant.NAME);

        ProcessDetail processDetail = larkProcessService.detail(instancesStart.getCode());

        Map<String, ProcessDetail.FormItem> formItemMap =
                processDetail.getForm().stream().collect(Collectors.toMap(ProcessDetail.FormItem::getName, x -> x));

        List<FormComponent> formComponents = instancesStart.getFormComponents();

        String form = this.parseFormValues(formComponents, formItemMap);

        ArrayList<NodeApprover> nodeApprovers = new ArrayList<>();

        List<InstancesStart.TargetSelectUser> targetSelectUsers = instancesStart.getTargetSelectUsers();
        List<InstancesStart.TargetSelectUserAuthMatch> targetSelectUsersAuthMatch = instancesStart.getTargetSelectUsersAuthMatch();
        if (targetSelectUsers != null && targetSelectUsers.size() > 0) {
            targetSelectUsers.forEach(targetSelectUser -> {

                String[] userIds = targetSelectUser.getUserIds().stream()
                        .map(x -> userService.getThirdPartyId(x, LarkConstant.NAME))
                        .collect(Collectors.toList())
                        .toArray(new String[]{});

                nodeApprovers.add(NodeApprover.newBuilder()
                        .key(targetSelectUser.getKey())
                        .value(userIds)
                        .build());
            });
        } else if (targetSelectUsersAuthMatch != null && targetSelectUsersAuthMatch.size() > 0) {
            // 自选节点自动匹配
            log.debug("飞书-发起审批实例：code:{}，共{}个节点，使用自选节点自动匹配。", instancesStart.getCode(), targetSelectUsersAuthMatch.size());

            PreviewInstanceRespBody preview = this.preview(instancesStart.getCode(), userId, null, form);

            List<PreviewNode> needApproverNodes =
                    Arrays.stream(preview.getPreviewNodes())
                            .filter(PreviewNode::getIsApproverTypeFree)
                            .collect(Collectors.toList());

            if (needApproverNodes.size() != targetSelectUsersAuthMatch.size()) {
                String errorMsg = String.format("自选节点自动匹配的流程节点与预测结果需要的数量不匹配，输入数量:%s，需要数量:%s"
                        , targetSelectUsersAuthMatch.size()
                        , needApproverNodes.size());
                log.error(errorMsg);
                throw new WfException(errorMsg);
            }
            for (int i = 0; i < needApproverNodes.size(); i++) {
                InstancesStart.TargetSelectUserAuthMatch targetSelectUser = targetSelectUsersAuthMatch.get(i);
                PreviewNode previewNode = needApproverNodes.get(i);
                String[] userIds = targetSelectUser.getUserIds().stream()
                        .map(x -> userService.getThirdPartyId(x, LarkConstant.NAME))
                        .collect(Collectors.toList())
                        .toArray(new String[]{});

                nodeApprovers.add(NodeApprover.newBuilder()
                        .key(previewNode.getNodeId())
                        .value(userIds)
                        .build());
            }
        }

        // 创建请求对象
        CreateInstanceReq req = CreateInstanceReq.newBuilder()
                .instanceCreate(InstanceCreate.newBuilder()
                        .approvalCode(instancesStart.getCode())
                        .userId(userId)
                        .form(form)
                        .nodeApproverUserIdList(nodeApprovers.toArray(new NodeApprover[]{}))
                        .build())
                .build();

        // 发起请求
        try {
            CreateInstanceResp resp = client.approval().instance().create(req);
            // 处理服务端错误
            if (!resp.success()) {
                String errMsg = String.format("飞书-发起审批实例失败。code:%s,msg:%s,reqId:%s,data:%s,form:%s",
                        resp.getCode(),
                        resp.getMsg(),
                        resp.getRequestId(),
                        Jsons.DEFAULT.toJson(instancesStart),
                        form);
                log.error(errMsg);
                throw new WfException(errMsg);
            }

            CreateInstanceRespBody data = resp.getData();

            return InstancesStartResult.builder().instanceId(data.getInstanceCode())
                    .code(instancesStart.getCode())
                    .build();

        } catch (WfException e) {
            throw e;
        } catch (Exception e) {
            String errMsg = String.format("飞书-发起审批实例失败。data:%s,form:%s",
                    Jsons.DEFAULT.toJson(instancesStart),
                    form);
            log.error(errMsg);
            throw new WfException(errMsg, e);
        }

    }

    @Override
    public InstancesPreviewResult preview(InstancesPreview instancesPreview) {

        log.debug("飞书-预览审批实例:{}", Jsons.DEFAULT.toJson(instancesPreview));


        ProcessDetail processDetail = larkProcessService.detail(instancesPreview.getCode());

        Map<String, ProcessDetail.FormItem> formItemMap =
                processDetail.getForm().stream().collect(Collectors.toMap(ProcessDetail.FormItem::getName, x -> x));

        List<FormComponent> formComponents = instancesPreview.getFormComponents();

        String form = this.parseFormValues(formComponents, formItemMap);

        this.preview(instancesPreview.getCode(),
                userService.getThirdPartyId(instancesPreview.getUserId(), LarkConstant.NAME),
                userService.getThirdPartyId(instancesPreview.getDeptId(), LarkConstant.NAME),
                form);

        return null;
    }


    public PreviewInstanceRespBody preview(String approvalCode, String larkUserId, String larkDeptId, String form) {

        log.debug("飞书-预览审批实例。approvalCode:{},larkUserId:{},larkDeptId:{},form:{}", approvalCode, larkUserId, larkDeptId, form);

        try {
            // 创建请求对象
            PreviewInstanceReq req = PreviewInstanceReq.newBuilder()
                    .previewInstanceReqBody(PreviewInstanceReqBody.newBuilder()
                            .userId(larkUserId)
                            .approvalCode(approvalCode)
                            .departmentId(larkDeptId)
                            .form(form)
                            .build())
                    .userIdType("user_id")
                    .build();
            // 发起请求
            PreviewInstanceResp resp = larkConfig.getClient().approval().instance().preview(req);

            // 处理服务端错误
            if (!resp.success()) {
                String errMsg = String.format("飞书-预览审批实例失败。code:%s,msg:%s,reqId:%s,approvalCode:%s,larkUserId:%s,larkDeptId:%s,form:%s",
                        resp.getCode(),
                        resp.getMsg(),
                        resp.getRequestId(),
                        approvalCode,
                        larkUserId,
                        larkDeptId,
                        form);
                throw new WfException(errMsg);
            }

            return resp.getData();

        } catch (WfException e) {
            throw e;
        } catch (Exception e) {
            String errMsg = String.format("飞书-预览审批实例失败。approvalCode:%s,larkUserId:%s,larkDeptId:%s,form:%s",
                    approvalCode,
                    larkUserId,
                    larkDeptId,
                    form);
            log.error(errMsg, e);
            throw new WfException(errMsg, e);
        }

    }


    public String parseFormValues(List<FormComponent> formComponents, Map<String, ProcessDetail.FormItem> formItemMap) {

        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (FormComponent formComponent : formComponents) {
            maps.add(this.parseFormValues(formComponent, formItemMap.get(formComponent.getName())));
        }

        return Jsons.DEFAULT.toJson(maps);
    }


    public Map<String, Object> parseFormValues(FormComponent formComponents, ProcessDetail.FormItem formItem) {

        return formConvert.convert(formComponents.getComponentType(), new FormItemConvert(formComponents, formItem));
    }


    @Override
    public String getType() {
        return LarkConstant.NAME;
    }


    @Slf4j
    @AllArgsConstructor
    public static class FormConvert extends ComponentConvert<FormItemConvert, Map<String, Object>> {

        private LarkConfig larkConfig;

        @Override
        public Map<String, Object> convert(ComponentType type, FormItemConvert component) {
            if (!type.equals(component.getFormItem().getType())) {
                String errMsg = String.format("飞书-转换表单内容失败，传入表单类型与飞书平台配置类型不匹配或飞书平台设置类型系统不支持。name:%s,系统类型:%s,飞书类型:%s",
                        component.getFormItem().getName(),
                        component.getFormComponents().getComponentType(),
                        component.getFormItem().getType());
                log.error(errMsg);
                throw new WfException(errMsg);
            }
            return super.convert(type, component);
        }

        @Override
        protected Map<String, Object> text(FormItemConvert component) {
            return this.base(component, "input", component.getFormComponents().getValue());
        }

        @Override
        protected Map<String, Object> textarea(FormItemConvert component) {
            return this.base(component, "textarea", component.getFormComponents().getValue());
        }

        @Override
        protected Map<String, Object> select(FormItemConvert component) {

            SelectOption option = (SelectOption) component.getFormItem().getOption();

            Map<String, String> optionMap = option.optionsToMap();


            return this.base(component, "radioV2", optionMap.get(component.getFormComponents().getValue()));
        }

        @Override
        protected Map<String, Object> multiSelect(FormItemConvert component) {

            List<String> list = ComponentParseAll
                    .COMPONENT_PARSE_MULTI_SELECT
                    .toJava(component.getFormComponents().getValue());


            MultiSelectOption option = (MultiSelectOption) component.getFormItem().getOption();

            Map<String, String> optionMap = option.optionsToMap();

            return this.base(component, "checkboxV2", list.stream()
                    .map(optionMap::get)
                    .collect(Collectors.toList()));
        }

        @Override
        protected Map<String, Object> date(FormItemConvert component) {

            DateComponent dateComponent = ComponentParseAll
                    .COMPONENT_PARSE_DATE
                    .toJava(component.getFormComponents().getValue());

            return this.base(component, "date", dateComponent.getDate().atOffset(ZoneOffset.ofHours(8))
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }

        @Override
        protected Map<String, Object> dateRange(FormItemConvert component) {
            DateRangeComponent dateRange = ComponentParseAll
                    .COMPONENT_PARSE_DATE_RANGE
                    .toJava(component.getFormComponents().getValue());
            String begin = dateRange.getBegin().atOffset(ZoneOffset.ofHours(8))
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String end = dateRange.getEnd().atOffset(ZoneOffset.ofHours(8))
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            HashMap<String, Object> map = new HashMap<>();
            map.put("start", begin);
            map.put("end", end);

            switch (dateRange.getDateFormat()) {
                case YYYY_MM_DD:
                    map.put("interval",
                            Duration.between(dateRange.getBegin().toLocalDate()
                                    , dateRange.getEnd().toLocalDate()).toDays());

                case YYYY_MM_DD_HH_MM:

                    map.put("interval",
                            Duration.between(
                                    dateRange.getBegin().withSecond(0),
                                    dateRange.getEnd().withSecond(0)).toHours());
            }

            return this.base(component, "dateInterval", map);

        }

        @Override
        protected Map<String, Object> number(FormItemConvert component) {
            return this.base(component, "number", component.getFormComponents().getValue());
        }

        @Override
        protected Map<String, Object> amount(FormItemConvert component) {
            Map<String, Object> map = this.base(component, "amount", component.getFormComponents().getValue());
            map.put("currency", "CNY");
            return map;
        }

        @Override
        protected Map<String, Object> image(FormItemConvert component) {

            List<ImageComponent> imageComponents = ComponentParseAll
                    .COMPONENT_PARSE_IMAGE
                    .toJava(component.getFormComponents().getValue());

            List<String> list = new ArrayList<>();
            for (ImageComponent imageComponent : imageComponents) {

                String code = uploadFile("image",
                        imageComponent.getName(),
                        imageComponent.getUrl(),
                        imageComponent.getSize());
                list.add(code);

            }

            return this.base(component, "image", list);
        }


        @Override
        protected Map<String, Object> attachment(FormItemConvert component) {
            List<AttachmentComponent> attachmentComponents = ComponentParseAll
                    .COMPONENT_PARSE_ATTACHMENT
                    .toJava(component.getFormComponents().getValue());

            List<String> list = new ArrayList<>();
            for (AttachmentComponent attachmentComponent : attachmentComponents) {
                String code = uploadFile("attachment",
                        attachmentComponent.getName(),
                        attachmentComponent.getUrl(),
                        attachmentComponent.getSize());
                list.add(code);
            }

            return this.base(component, "attachmentV2", list);
        }

        @Override
        protected Map<String, Object> unknown(FormItemConvert component) {
            String errMsg = String.format("飞书-转换表单内容失败，无法识别的类型。name:%s,id:%s,value:%s",
                    component.getFormItem().getName(),
                    component.getFormItem().getId(),
                    component.getFormComponents().getValue());
            log.error(errMsg);
            throw new WfException(errMsg);
        }

        @Override
        protected Map<String, Object> table(FormItemConvert component) {

            Collection<Collection<FormComponent>> table = ComponentParseAll
                    .COMPONENT_PARSE_TABLE
                    .toJava(component.getFormComponents().getValue());

            List<ProcessDetail.FormItem> children = component.getFormItem().getChildren();
            Map<String, ProcessDetail.FormItem> childrenMap = children.stream()
                    .collect(Collectors.toMap(ProcessDetail.FormItem::getName, x -> x));

            List<List<Map<String, Object>>> value = table.stream()
                    .map(row -> row.stream()
                            .map(x -> this.convert(x.getComponentType()
                                    , new FormItemConvert(x, childrenMap.get(x.getName()))))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());


            return this.base(component, "fieldList", value);
        }

        private Map<String, Object> base(FormItemConvert component, String type, Object value) {
            HashMap<String, Object> map = new HashMap<>(3);
            map.put("id", component.getFormItem().getId());
            map.put("type", type);
            map.put("value", value);
            return map;
        }

        private String uploadFile(String type, String fileName, String fileUrl, Long fileSize) {
            File dir = null;
            File file = null;

            String tempDir = System.getProperty("java.io.tmpdir");
            String uuid = UUID.randomUUID().toString();
            String tempDirPath = tempDir + "/" + uuid;
            dir = new File(tempDirPath);
            boolean mkdirs = dir.mkdirs();

            if (!mkdirs) {
                String errMsg = String.format("飞书-上传审批文件创建临时文件夹失败 path:%s", tempDirPath);
                log.error(errMsg);
                throw new WfException(errMsg);
            }

            file = new File(tempDirPath + "/" + fileName);


            try {
                IOUtils.copyFromURL(new URL(fileUrl), file);

                RequestOptions reqOptions = new RequestOptions();
                reqOptions.setSupportUpload(true);

                UploadFileBody fileBody = UploadFileBody.builder()
                        .content(file)
                        .name(fileName)
                        .type(type)
                        .build();

                RawResponse rsp = larkConfig.getClient().post("/approval/openapi/v2/file/upload"
                        , fileBody
                        , AccessTokenType.Tenant, reqOptions);

                // 反序列化
                UploadFileRsp resp = UnmarshalRespUtil.unmarshalResp(rsp, UploadFileRsp.class);
                //上传失败
                if (!resp.success()) {
                    String errMsg = String.format("飞书-上传审批文件失败 name:%s,filesize:%s,filepath:%s,code:%s,msg:%s",
                            fileName,
                            fileSize,
                            fileUrl,
                            resp.getCode(),
                            resp.getMsg());
                    log.error(errMsg);
                    throw new WfException(errMsg);
                }

                return resp.getData().get("code");
            } catch (WfException wfe) {
                throw wfe;
            } catch (Exception e) {
                String errMsg = String.format("飞书-上传审批文件失败 name:%s,filesize:%s,filepath:%s",
                        fileName,
                        fileSize,
                        fileUrl);
                log.error(errMsg, e);
                throw new WfException(errMsg, e);
            } finally {
                try {
                    /*if (file != null) {
                        file.delete();
                    }*/
                    if (dir != null) {
                        boolean delete = dir.delete();
                    }
                } catch (Exception ignored) {

                }

            }
        }

        @ApiModel("飞书-上传审批文件参数")
        @Data
        @Builder
        public static class UploadFileBody {
            @SerializedName("name")
            private String name;
            @SerializedName("type")
            private String type;
            @SerializedName("content")
            private File content;
        }

        @ApiModel("飞书-上传文件返回")
        @Data
        @Builder
        public static class UploadFileRsp extends BaseResponse<Map<String, String>> {

        }


    }

    @Data
    @AllArgsConstructor
    public static class FormItemConvert {
        private FormComponent formComponents;
        private ProcessDetail.FormItem formItem;
    }
}
