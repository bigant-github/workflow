package org.bigant.fw.lark.instances;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.approval.v4.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.DateUtil;
import org.bigant.fw.lark.LarkConfig;
import org.bigant.fw.lark.LarkConstant;
import org.bigant.fw.lark.instances.form.LarkFDCF;
import org.bigant.fw.lark.instances.form.convert.LarkBaseFDC;
import org.bigant.fw.lark.process.LarkProcessService;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.instances.InstanceStatus;
import org.bigant.wf.instances.InstancesService;
import org.bigant.wf.instances.bean.*;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.process.bean.ProcessDetail;
import org.bigant.wf.process.form.FormDetailItem;
import org.bigant.wf.task.TaskStatus;
import org.bigant.wf.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 飞书审批流程service
 *
 * @author galen
 * @date 2024/2/1815:49
 */
@Slf4j
@AllArgsConstructor
public class LarkInstancesService implements InstancesService {


    private static final String SUBSCRIBE_KEY = LarkConstant.CACHE_KEY + "process:subscribe:";
    private LarkConfig larkConfig;

    private LarkProcessService larkProcessService;

    private UserService userService;

    private LarkFDCF larkFDCF;

    private ICache cache;

    private boolean needSubscribe;

    /**
     * 发起审批实例
     * 接口地址 <a href="https://open.feishu.cn/document/server-docs/approval-v4/instance/create?appId=cli_a52dc3cf4f3b500e">...</a>
     */
    @Override
    public InstanceStartResult start(InstanceStart instanceStart) {
        // 构建client
        Client client = larkConfig.getClient();

        String userId = userService.getOtherUserIdByUserId(instanceStart.getUserId(), LarkConstant.NAME);

        ProcessDetail processDetail = larkProcessService.detail(instanceStart.getProcessCode());


        String form = parseFormData(processDetail, instanceStart.getFormData());

        ArrayList<NodeApprover> nodeApprovers = new ArrayList<>();
        ArrayList<NodeCc> nodeCcs = new ArrayList<>();

        this.selectNode(instanceStart, nodeApprovers, userId, form, nodeCcs);

        // 创建请求对象
        CreateInstanceReq req = CreateInstanceReq.newBuilder()
                .instanceCreate(InstanceCreate.newBuilder()
                        .approvalCode(instanceStart.getProcessCode())
                        .userId(userId)
                        .form(form)
                        .nodeApproverUserIdList(nodeApprovers.toArray(new NodeApprover[]{}))
                        .nodeCcUserIdList(nodeCcs.toArray(new NodeCc[]{}))
                        .build())
                .build();

        CreateInstanceResp resp;
        // 发起请求
        try {
            resp = client.approval().instance().create(req);
            // 处理服务端错误
            if (!resp.success()) {
                String errMsg = String.format("飞书-发起审批实例失败。code:%s,msg:%s,reqId:%s,data:%s,form:%s",
                        resp.getCode(),
                        resp.getMsg(),
                        resp.getRequestId(),
                        Jsons.DEFAULT.toJson(instanceStart),
                        form);
                log.error(errMsg);
                throw new WfException(errMsg);
            }

        } catch (WfException e) {
            throw e;
        } catch (Exception e) {
            String errMsg = String.format("飞书-发起审批实例失败。data:%s,form:%s",
                    Jsons.DEFAULT.toJson(instanceStart),
                    form);
            log.error(errMsg);
            throw new WfException(errMsg, e);
        }
        CreateInstanceRespBody data = resp.getData();

        log.debug("飞书-发起审批实例成功。instanceCode:{}", data.getInstanceCode());


        try {
            this.subscribe(instanceStart.getProcessCode());
        } catch (Exception e) {
            log.warn("飞书-订阅审批实例失败。processCode:{}", data.getInstanceCode());
        }

        return InstanceStartResult.builder().instanceCode(data.getInstanceCode())
                .processCode(instanceStart.getProcessCode())
                .build();


    }

    private void selectNode(InstanceStart instanceStart, ArrayList<NodeApprover> nodeApprovers, String userId, String form, ArrayList<NodeCc> nodeCcs) {
        PreviewInstanceRespBody preview = null;

        //自选节点
        if (instanceStart.getSelectApproverUsers() != null
                && !instanceStart.getSelectApproverUsers().isEmpty()) {
            instanceStart.getSelectApproverUsers().forEach(nodeUser -> {

                String[] userIds = nodeUser.getUserIds().stream()
                        .map(x -> userService.getOtherUserIdByUserId(x, LarkConstant.NAME))
                        .collect(Collectors.toList())
                        .toArray(new String[]{});

                nodeApprovers.add(NodeApprover.newBuilder()
                        .key(nodeUser.getKey())
                        .value(userIds)
                        .build());
            });
        }

        //自选审批节点自动匹配
        List<InstanceStart.AuthMatchNodeUser> matchSelectApproverUsers = instanceStart.getAuthMatchSelectApproverUsers();
        if (matchSelectApproverUsers != null
                && !matchSelectApproverUsers.isEmpty()) {
            // 自选节点自动匹配
            log.debug("飞书-发起审批实例：code:{}，共{}个节点，使用自选节点自动匹配。",
                    instanceStart.getProcessCode(),
                    instanceStart.getAuthMatchSelectApproverUsers().size());

            preview = this.preview(instanceStart.getProcessCode(), userId, null, form);

            List<PreviewNode> needApproverNodes =
                    Arrays.stream(preview.getPreviewNodes())
                            .filter(PreviewNode::getIsApproverTypeFree)
                            .collect(Collectors.toList());

            for (int i = 0; i < Math.max(needApproverNodes.size(), matchSelectApproverUsers.size()); i++) {
                InstanceStart.AuthMatchNodeUser targetSelectUser = matchSelectApproverUsers.get(i);
                PreviewNode previewNode = needApproverNodes.get(i);
                String[] userIds = targetSelectUser.getUserIds().stream()
                        .map(x -> userService.getOtherUserIdByUserId(x, LarkConstant.NAME))
                        .collect(Collectors.toList())
                        .toArray(new String[]{});

                nodeApprovers.add(NodeApprover.newBuilder()
                        .key(previewNode.getNodeId())
                        .value(userIds)
                        .build());
            }
        }


        //自选抄送节点
        if (instanceStart.getSelectCcUsers() != null
                && !instanceStart.getSelectCcUsers().isEmpty()) {
            instanceStart.getSelectCcUsers().forEach(nodeUser -> {

                String[] userIds = nodeUser.getUserIds().stream()
                        .map(x -> userService.getOtherUserIdByUserId(x, LarkConstant.NAME))
                        .collect(Collectors.toList())
                        .toArray(new String[]{});

                nodeCcs.add(NodeCc.newBuilder()
                        .key(nodeUser.getKey())
                        .value(userIds)
                        .build());
            });
        }

        //自选抄送节点自动匹配
        List<InstanceStart.AuthMatchNodeUser> matchSelectCcUsers = instanceStart.getAutoMathSelectCcUsers();
        if (matchSelectCcUsers != null
                && !matchSelectCcUsers.isEmpty()) {
            // 自选节点自动匹配
            log.debug("飞书-发起审批实例：code:{}，共{}个节点，使用自选抄送节点自动匹配。",
                    instanceStart.getProcessCode(),
                    matchSelectCcUsers.size());
            if (preview == null) {
                preview = this.preview(instanceStart.getProcessCode(), userId, null, form);
            }

            List<PreviewNode> needCcNodes =
                    Arrays.stream(preview.getPreviewNodes())
                            .filter(PreviewNode::getHasCcTypeFree)
                            .collect(Collectors.toList());

            for (int i = 0; i < Math.max(needCcNodes.size(), matchSelectCcUsers.size()); i++) {
                InstanceStart.AuthMatchNodeUser targetSelectUser = matchSelectCcUsers.get(i);
                PreviewNode previewNode = needCcNodes.get(i);
                String[] userIds = targetSelectUser.getUserIds().stream()
                        .map(x -> userService.getOtherUserIdByUserId(x, LarkConstant.NAME))
                        .collect(Collectors.toList())
                        .toArray(new String[]{});

                nodeCcs.add(NodeCc.newBuilder()
                        .key(previewNode.getNodeId())
                        .value(userIds)
                        .build());
            }
        }
    }

    private String parseFormData(ProcessDetail processDetail, List<FormDataItem> formData) {
        Map<String, FormDetailItem> formItemMap =
                processDetail.getForm().stream().collect(Collectors.toMap(FormDetailItem::getName, x -> x));

        return this.parseFormValues(formData, formItemMap);
    }

    @Override
    public InstancePreviewResult preview(InstancePreview instancePreview) {

        log.debug("飞书-预览审批实例:{}", Jsons.DEFAULT.toJson(instancePreview));


        ProcessDetail processDetail = larkProcessService.detail(instancePreview.getInstanceCode());

        this.preview(instancePreview.getInstanceCode(),
                userService.getOtherUserIdByUserId(instancePreview.getUserId(), LarkConstant.NAME),
                userService.getOtherDeptIdByDeptId(instancePreview.getDeptId(), LarkConstant.NAME),
                parseFormData(processDetail, instancePreview.getFormData()));

        return null;
    }

    @Override
    public InstanceDetailResult detail(String instanceCode) {

        // 创建请求对象
        GetInstanceReq getInstanceReq = GetInstanceReq.newBuilder()
                .instanceId(instanceCode)
                .build();
        // 发起请求
        GetInstanceResp resp;
        try {
            resp = larkConfig.getClient().approval().instance().get(getInstanceReq);
        } catch (Exception e) {
            String errMsg = String.format("飞书-创建查询审批实例失败。instanceCode:%s", instanceCode);
            log.error(errMsg);
            throw new WfException(errMsg, e);
        }

        // 处理服务端错误
        if (!resp.success()) {
            String errMsg = String.format("飞书-创建查询审批实例失败。instanceCode:%s,code:%s,msg:%s,reqId:%s",
                    instanceCode,
                    resp.getCode(),
                    resp.getMsg(),
                    resp.getRequestId());
            log.error(errMsg);
            throw new WfException(errMsg);
        }

        GetInstanceRespBody body = resp.getData();
        log.debug("飞书-创建查询审批实例成功。instanceCode:{},data:{}", instanceCode, Jsons.DEFAULT.toJson(body));
        String form = body.getForm();
        String approvalCode = body.getApprovalCode();

        JSONArray formArray = JSONArray.parse(form);
        ArrayList<FormDataItem> formData = new ArrayList<>();

        ProcessDetail detail = larkProcessService.detail(approvalCode);
        Map<String, org.bigant.wf.process.form.FormDetailItem> detailItemMap =
                detail.getForm().stream().collect(Collectors.toMap(org.bigant.wf.process.form.FormDetailItem::getId, x -> x));

        for (int i = 0; i < formArray.size(); i++) {
            JSONObject jsonObj = formArray.getJSONObject(i);
            LarkBaseFDC fdc = larkFDCF.getByOtherType(jsonObj.getString("type"));
            formData.add(fdc.toFormData(new LarkBaseFDC.ToOtherParam(jsonObj, detailItemMap)));
        }

        /*
         * status 可选值
         * PENDING：审批中
         * APPROVED：通过
         * REJECTED：拒绝
         * CANCELED：撤回
         * DELETED：删除
         */
        InstanceStatus instanceStatus = null;
        String status = body.getStatus();
        switch (status) {
            case "PENDING":
                instanceStatus = InstanceStatus.RUNNING;
                break;
            case "APPROVED":
                instanceStatus = InstanceStatus.AGREED;
                break;
            case "REJECTED":
                instanceStatus = InstanceStatus.REFUSED;
                break;
            case "CANCELED":
                instanceStatus = InstanceStatus.CANCELED;
                break;
            case "DELETED":
                instanceStatus = InstanceStatus.DELETED;
                break;
            default:
                String errMsg = String.format("飞书-创建查询审批实例失败,无法翻译的实例状态。instanceCode:%s,status:%s",
                        instanceCode,
                        status);
                log.error(errMsg);
                throw new WfException(errMsg);
        }

        ArrayList<InstanceDetailResult.Task> tasks = new ArrayList<>();
        for (InstanceTask instanceTask : body.getTaskList()) {

            String userId = instanceTask.getUserId();
            String userName = instanceTask.getUserId();
            if (!(userId == null || userId.isEmpty())) {
                userId = userService.getUserIdByOtherUserId(userId, getType());
                userName = userService.getUser(userId).getUserName();
            }

            String resultTaskStatus = instanceTask.getStatus();
            TaskStatus taskStatus;
            /**
             * resultTaskStatus 可选值
             * PENDING：审批中
             * APPROVED：通过
             * REJECTED：拒绝
             * TRANSFERRED：已转交
             * DONE：完成
             */
            switch (resultTaskStatus) {
                case "PENDING":
                    taskStatus = TaskStatus.RUNNING;
                    break;
                case "APPROVED":
                    taskStatus = TaskStatus.AGREED;
                    break;
                case "REJECTED":
                    taskStatus = TaskStatus.REFUSED;
                    break;
                case "TRANSFERRED":
                    taskStatus = TaskStatus.REDIRECTED;
                    break;
                case "DONE":
                    taskStatus = TaskStatus.PAUSED;
                    break;
                default:
                    String errMsg = String.format("飞书-创建查询审批实例失败,无法翻译的任务状态。instanceCode:%s,status:%s", instanceCode, resultTaskStatus);
                    log.error(errMsg);
                    throw new WfException(errMsg);
            }


            tasks.add(InstanceDetailResult.Task.builder()
                    .taskCode(instanceTask.getId())
                    .userId(instanceTask.getUserId())
                    .userName(userName)
                    .taskStatus(taskStatus)
                    .endTime(DateUtil.timestampToLocalDateTime(instanceTask.getEndTime()))
                    .build());
        }

        return InstanceDetailResult.builder()
                .instanceCode(body.getInstanceCode())
                .processCode(approvalCode)
                .formData(formData)
                .instanceStatus(instanceStatus)
                .tasks(tasks)
                .title(body.getApprovalName())
                .deptId(userService.getDeptIdByOtherDeptId(body.getDepartmentId(), getType()))
                .userId(userService.getOtherUserIdByUserId(body.getUserId(), getType()))
                .build();
    }

    public void subscribe(String processCode) throws Exception {

        String cacheKey = SUBSCRIBE_KEY + processCode;
        if (needSubscribe && !cache.exists(cacheKey)) {
            // 构建client
            Client client = larkConfig.getClient();

            // 创建请求对象
            SubscribeApprovalReq req = SubscribeApprovalReq.newBuilder()
                    .approvalCode(processCode)
                    .build();

            // 发起请求
            SubscribeApprovalResp resp = client.approval().approval().subscribe(req);

            // 处理服务端错误
            if (!resp.success() && resp.getCode() != 1390007) {
                String errMsg = String.format("飞书-订阅审批失败。processCode:%s,errMsg:%s", processCode, resp.getMsg());
                log.error(errMsg);
                throw new WfException(errMsg);
            }

            cache.set(SUBSCRIBE_KEY + processCode, "true", 9999, TimeUnit.DAYS);
        }

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


    public String parseFormValues(List<FormDataItem> formDataItemList, Map<String, org.bigant.wf.process.form.FormDetailItem> formItemMap) {

        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (FormDataItem formDataItem : formDataItemList) {
            maps.add(this.parseFormValues(formDataItem, formItemMap.get(formDataItem.getName())));
        }

        return Jsons.DEFAULT.toJson(maps);
    }


    public Map<String, Object> parseFormValues(FormDataItem formComponents, org.bigant.wf.process.form.FormDetailItem formDetailItem) {
        if (!formComponents.getComponentType().equals(formDetailItem.getType())) {
            String errMsg = String.format("飞书-转换表单内容失败，传入表单类型与飞书平台配置类型不匹配或飞书平台设置类型系统不支持。name:%s,系统类型:%s,飞书类型:%s",
                    formDetailItem.getName(),
                    formComponents.getComponentType(),
                    formDetailItem.getType());
            log.error(errMsg);
            throw new WfException(errMsg);
        }
        return larkFDCF.getByFormType(formComponents.getComponentType())
                .toOther(new LarkBaseFDC.FormItemConvert(formComponents, formDetailItem));
    }


    @Override
    public String getType() {
        return LarkConstant.NAME;
    }


    /*@Slf4j
    @AllArgsConstructor
    public static class FormConvert extends ComponentConvertTTT<LarkBaseFDC.FormItemConvert, Map<String, Object>> {

        private LarkConfig larkConfig;

        @Override
        public Map<String, Object> convert(ComponentType type, LarkBaseFDC.FormItemConvert component) {
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
        protected Map<String, Object> text(LarkBaseFDC.FormItemConvert component) {
            return this.base(component, "input", component.getFormComponents().getValue());
        }

        @Override
        protected Map<String, Object> textarea(LarkBaseFDC.FormItemConvert component) {
            return this.base(component, "textarea", component.getFormComponents().getValue());
        }

        @Override
        protected Map<String, Object> select(LarkBaseFDC.FormItemConvert component) {

            SelectOption option = (SelectOption) component.getFormItem().getOption();

            Map<String, String> optionMap = option.optionsToMap();


            return this.base(component, "radioV2", optionMap.get(component.getFormComponents().getValue()));
        }

        @Override
        protected Map<String, Object> multiSelect(LarkBaseFDC.FormItemConvert component) {

            Collection<String> list = FormDataParseAll
                    .COMPONENT_PARSE_MULTI_SELECT
                    .strToJava(component.getFormComponents().getValue());


            MultiSelectOption option = (MultiSelectOption) component.getFormItem().getOption();

            Map<String, String> optionMap = option.optionsToMap();

            return this.base(component, "checkboxV2", list.stream()
                    .map(optionMap::get)
                    .collect(Collectors.toList()));
        }

        @Override
        protected Map<String, Object> date(LarkBaseFDC.FormItemConvert component) {

            FormDataDate formDataDate = FormDataParseAll
                    .COMPONENT_PARSE_DATE
                    .strToJava(component.getFormComponents().getValue());

            return this.base(component, "date", formDataDate.getDate().atOffset(ZoneOffset.ofHours(8))
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }

        @Override
        protected Map<String, Object> dateRange(LarkBaseFDC.FormItemConvert component) {
            FormDataDateRange dateRange = FormDataParseAll
                    .COMPONENT_PARSE_DATE_RANGE
                    .strToJava(component.getFormComponents().getValue());
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
        protected Map<String, Object> number(LarkBaseFDC.FormItemConvert component) {
            return this.base(component, "number", component.getFormComponents().getValue());
        }

        @Override
        protected Map<String, Object> amount(LarkBaseFDC.FormItemConvert component) {
            Map<String, Object> map = this.base(component, "amount", component.getFormComponents().getValue());
            map.put("currency", "CNY");
            return map;
        }

        @Override
        protected Map<String, Object> image(LarkBaseFDC.FormItemConvert component) {

            Collection<FormDataImage> formDataImages = FormDataParseAll
                    .COMPONENT_PARSE_IMAGE
                    .strToJava(component.getFormComponents().getValue());

            List<String> list = new ArrayList<>();
            for (FormDataImage formDataImage : formDataImages) {

                String code = uploadFile("image",
                        formDataImage.getName(),
                        formDataImage.getUrl(),
                        formDataImage.getSize());
                list.add(code);

            }

            return this.base(component, "image", list);
        }


        @Override
        protected Map<String, Object> attachment(LarkBaseFDC.FormItemConvert component) {
            Collection<FormDataAttachment> formDataAttachments = FormDataParseAll
                    .COMPONENT_PARSE_ATTACHMENT
                    .strToJava(component.getFormComponents().getValue());

            List<String> list = new ArrayList<>();
            for (FormDataAttachment formDataAttachment : formDataAttachments) {
                String code = uploadFile("attachment",
                        formDataAttachment.getName(),
                        formDataAttachment.getUrl(),
                        formDataAttachment.getSize());
                list.add(code);
            }

            return this.base(component, "attachmentV2", list);
        }

        @Override
        protected Map<String, Object> unknown(LarkBaseFDC.FormItemConvert component) {
            String errMsg = String.format("飞书-转换表单内容失败，无法识别的类型。name:%s,id:%s,value:%s",
                    component.getFormItem().getName(),
                    component.getFormItem().getId(),
                    component.getFormComponents().getValue());
            log.error(errMsg);
            throw new WfException(errMsg);
        }

        @Override
        protected Map<String, Object> table(LarkBaseFDC.FormItemConvert component) {

            Collection<Collection<FormData>> table = FormDataParseAll
                    .COMPONENT_PARSE_TABLE
                    .strToJava(component.getFormComponents().getValue());

            List<ProcessDetail.FormItem> children = component.getFormItem().getChildren();
            Map<String, ProcessDetail.FormItem> childrenMap = children.stream()
                    .collect(Collectors.toMap(ProcessDetail.FormItem::getName, x -> x));

            List<List<Map<String, Object>>> value = table.stream()
                    .map(row -> row.stream()
                            .map(x -> this.convert(x.getComponentType()
                                    , new LarkBaseFDC.FormItemConvert(x, childrenMap.get(x.getName()))))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());


            return this.base(component, "fieldList", value);
        }

        private Map<String, Object> base(LarkBaseFDC.FormItemConvert component, String type, Object value) {
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
                IOUtils.copyFile(new URL(fileUrl), file);

                RequestOptions reqOptions = new RequestOptions();
                reqOptions.setSupportUpload(true);

                LarkFile.UploadFileBody fileBody = LarkFile.UploadFileBody.builder()
                        .content(file)
                        .name(fileName)
                        .type(type)
                        .build();

                RawResponse rsp = larkConfig.getClient().post("/approval/openapi/v2/file/upload"
                        , fileBody
                        , AccessTokenType.Tenant, reqOptions);

                // 反序列化
                LarkFile.UploadFileRsp resp = UnmarshalRespUtil.unmarshalResp(rsp, LarkFile.UploadFileRsp.class);
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
                    if (file != null) {
                        file.delete();
                    }
                    if (dir != null) {
                        boolean delete = dir.delete();
                    }
                } catch (Exception ignored) {

                }

            }
        }


    }*/

}
