package org.bigant.fw.dingtalk.instances;


import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkworkflow_1_0.Client;
import com.aliyun.dingtalkworkflow_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.DingTalkConfig;
import org.bigant.fw.dingtalk.DingTalkConstant;
import org.bigant.fw.dingtalk.form.component.DingTalkCCF;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.instances.InstancesService;
import org.bigant.wf.instances.bean.*;
import org.bigant.wf.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 审批实例service
 *
 * @author galen
 * @date 2024/1/3114:53
 */
@AllArgsConstructor
@Data
@Slf4j
public class DingTalkInstancesService implements InstancesService {

    private DingTalkConfig dingTalkConfig;
    private DingTalkCCF ccf;
    private UserService userService;
    private com.aliyun.dingtalkworkflow_1_0.Client client;


    /**
     * 发起审批实例
     * 对应接口文档地址：https://open.dingtalk.com/document/orgapp/create-an-approval-instance
     *
     * @param instanceStart
     * @return
     */
    @Override
    public InstanceStartResult start(InstanceStart instanceStart) {

        log.debug("发起审批实例：{}", JSONObject.toJSONString(instanceStart));

        String dingTalkUserId = userService.getUserId(instanceStart.getUserId(), this.getType());

        StartProcessInstanceHeaders startProcessInstanceHeaders = new
                StartProcessInstanceHeaders();

        ArrayList<StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues> valuesDetailsDetails
                = new ArrayList<>();

        //转换表单值
        HashMap<String, String> formComponents = this.parseFormValue(instanceStart.getFormComponents(), dingTalkUserId);

        for (Map.Entry<String, String> formComponent : formComponents.entrySet()) {
            StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues value =
                    new StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues()
                            .setName(formComponent.getKey())
                            .setValue(formComponent.getValue());
            valuesDetailsDetails.add(value);
        }


        //匹配自选审批节点
        List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> selectActioners = null;
        if (instanceStart.getTargetSelectUsers() != null && instanceStart.getTargetSelectUsers().size() > 0) {
            selectActioners = this.parseTargetSelectUsers(instanceStart);
        } else if (instanceStart.getTargetSelectUsersAuthMatch() != null && instanceStart.getTargetSelectUsersAuthMatch().size() > 0) {
            //自选节点自动匹配
            selectActioners = this.parseTargetSelectUsersAuthMatch(instanceStart, formComponents);
        }


        StartProcessInstanceRequest startProcessInstanceRequest = new StartProcessInstanceRequest()
                .setOriginatorUserId(dingTalkUserId)
                .setProcessCode(instanceStart.getProcessCode())
                .setDeptId(instanceStart.getDeptId() != null && instanceStart.getDeptId().length() > 0 ?
                        Long.parseLong(userService.getDeptId(instanceStart.getDeptId(), this.getType()))
                        : null)
                /*.setMicroappAgentId(41605932L)*/
                /*.setApprovers(java.util.Arrays.asList(approvers0))*/
                /*.setCcList(java.util.Arrays.asList("26652461xxxx5992"))*/
                /*.setCcPosition("START")*/
                .setTargetSelectActioners(selectActioners)
                .setFormComponentValues(valuesDetailsDetails);

        try {
            Client client = getClient();
            startProcessInstanceHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();
            StartProcessInstanceResponse processInstanceResponse = client.startProcessInstanceWithOptions(startProcessInstanceRequest,
                    startProcessInstanceHeaders,
                    new RuntimeOptions());

            String instanceId = processInstanceResponse.getBody().getInstanceId();
            log.debug("发起审批实例成功：code:{}，instanceId:{}", instanceStart.getProcessCode(), instanceId);

            return InstanceStartResult.builder()
                    .processCode(instanceStart.getProcessCode())
                    .instanceCode(instanceId)
                    .build();
        } catch (TeaException err) {
            throw err;
        } catch (Exception _err) {
            throw new TeaException(_err.getMessage(), _err);
        }
    }

    @Override
    public InstancePreviewResult preview(InstancePreview instancePreview) {
        return null;
    }

    @Override
    public InstanceDetailResult detail(String instanceCode) {

        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();
        getProcessInstanceHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();

        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest =
                new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
                        .setProcessInstanceId(instanceCode);
        try {
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues getProcessInstanceResponseBodyResultFormComponentValues = client.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders
                            , new RuntimeOptions())
                    .getBody()
                    .getResult()
                    .getFormComponentValues()
                    .get(0);
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }
        return null;
    }

    /**
     * 解析自选节点用户自动匹配
     *
     * @param instanceStart
     * @param formComponents
     * @return
     */
    private List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> parseTargetSelectUsersAuthMatch(InstanceStart instanceStart, HashMap<String, String> formComponents) {
        List<InstanceStart.TargetSelectUserAuthMatch> targetSelectUsersAuthMatch = instanceStart.getTargetSelectUsersAuthMatch();
        log.debug(" 发起审批实例：code:{}，共{}个节点，使用自选节点自动匹配。", instanceStart.getProcessCode(), targetSelectUsersAuthMatch.size());

        List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> users
                = new ArrayList<>(targetSelectUsersAuthMatch.size());

        ProcessForecastResponseBody.ProcessForecastResponseBodyResult forecast = this.forecast(instanceStart.getProcessCode(),
                instanceStart.getUserId(),
                instanceStart.getDeptId(),
                formComponents);

        //获取需要输入的审批节点
        List<ProcessForecastResponseBody.ProcessForecastResponseBodyResultWorkflowActivityRulesWorkflowActor> approverListByForecast
                = this.getApproverListByForecast(forecast);

        if (approverListByForecast.size() != targetSelectUsersAuthMatch.size()) {
            String errorMsg = String.format("自选节点自动匹配的流程节点与预测结果需要的数量不匹配，输入数量:%s，需要数量:%s"
                    , targetSelectUsersAuthMatch.size()
                    , approverListByForecast.size());
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        for (int i = 0; i < targetSelectUsersAuthMatch.size(); i++) {

            List<String> userIds = targetSelectUsersAuthMatch.get(i).getUserIds()
                    .stream()
                    .map(x -> userService.getUserId(x, this.getType()))
                    .collect(Collectors.toList());

            ProcessForecastResponseBody.ProcessForecastResponseBodyResultWorkflowActivityRulesWorkflowActor actor
                    = approverListByForecast.get(i);

            log.debug(" 自选节点自动匹配结果：code:{}，节点Key：{}，共{}个用户：{}。",
                    instanceStart.getProcessCode(),
                    actor.getActorKey(),
                    userIds.size(),
                    userIds);

            StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners user
                    = new StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners()
                    .setActionerKey(actor.getActorKey())
                    .setActionerUserIds(userIds);

            users.add(user);

        }


        return users;
    }


    /**
     * 解析自选节点用户
     *
     * @param instanceStart
     * @return
     */
    private List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> parseTargetSelectUsers(InstanceStart instanceStart) {

        List<InstanceStart.TargetSelectUser> targetSelectUsers = instanceStart.getTargetSelectUsers();

        log.debug(" 发起审批实例：{}，共{}个节点，使用自选节点。", instanceStart.getProcessCode(), targetSelectUsers.size());


        List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> users
                = new ArrayList<>(targetSelectUsers.size());

        for (InstanceStart.TargetSelectUser targetSelectUser : targetSelectUsers) {

            List<String> userIds = targetSelectUser.getUserIds()
                    .stream()
                    .map(x -> userService.getUserId(x, this.getType()))
                    .collect(Collectors.toList());

            log.debug(" 发起审批实例：{}，节点Key：{}，共{}个用户：{}。"
                    , instanceStart.getProcessCode()
                    , targetSelectUser.getKey()
                    , userIds.size(), userIds);

            StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners user
                    = new StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners()
                    .setActionerKey(targetSelectUser.getKey())
                    .setActionerUserIds(userIds);

            users.add(user);
        }

        return users;

    }


    @Override
    public String getType() {
        return DingTalkConstant.NAME;
    }

    /**
     * 预测是否有自定义审批节点
     *
     * @param formComponents
     * @return
     * @throws Exception
     */
    private ProcessForecastResponseBody.ProcessForecastResponseBodyResult forecast(String code, String userId, String deptId, Map<String, String> formComponents) {
        log.debug(" 预测是否有自定义审批节点：code:{}，userId:{}，deptId:{}，formComponents:{}。",
                code,
                userId,
                deptId,
                formComponents);

        ProcessForecastHeaders processForecastHeaders = new ProcessForecastHeaders();

        ArrayList<ProcessForecastRequest.ProcessForecastRequestFormComponentValues> values =
                new ArrayList<>();

        for (Map.Entry<String, String> formComponent : formComponents.entrySet()) {

            ProcessForecastRequest.ProcessForecastRequestFormComponentValues value =
                    new ProcessForecastRequest.ProcessForecastRequestFormComponentValues()
                            .setName(formComponent.getKey())
                            .setValue(formComponent.getValue());
            values.add(value);
        }


        ProcessForecastRequest processForecastRequest = new ProcessForecastRequest()
                .setProcessCode(code)
                .setDeptId(Integer.valueOf(userService.getDeptId(deptId, getType())))
                .setUserId(userService.getUserId(userId, getType()))
                .setFormComponentValues(values);

        try {
            com.aliyun.dingtalkworkflow_1_0.Client client = getClient();
            processForecastHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();

            ProcessForecastResponseBody.ProcessForecastResponseBodyResult result =
                    client.processForecastWithOptions(processForecastRequest, processForecastHeaders, new RuntimeOptions())
                            .getBody().getResult();

            if (log.isDebugEnabled()) {

                log.debug(" 预测是否有自定义审批节点结果：code:{}，userId:{}，deptId:{}，formComponents:{}，result:{}",
                        code,
                        userId,
                        deptId,
                        formComponents,
                        JSONObject.toJSONString(result));

                this.getApproverListByForecast(result)
                        .forEach(x -> log.debug(" 预测是否有自定义审批节点结果需要自选的节点：code:{}，key:{}",
                                code,
                                x.getActorKey()));

            }

            return result;
        } catch (TeaException err) {
            log.error(" 预测是否有自定义审批节点：code:{}，userId:{}，deptId:{}，formComponents:{}，errCode:{},errorMsg:{}",
                    code,
                    userId,
                    deptId,
                    formComponents,
                    err.getCode(),
                    err.getMessage(),
                    err);
            throw err;
        } catch (Exception _err) {
            log.error(" 预测是否有自定义审批节点：{}，userId:{}，deptId:{}，formComponents:{}，err:{}", code, userId, deptId, formComponents, _err);
            throw new TeaException(_err.getMessage(), _err);
        }
    }


    /**
     * 预测是否有自定义审批节点
     *
     * @param formComponents
     * @return
     * @throws Exception
     */
    private ProcessForecastResponseBody.ProcessForecastResponseBodyResult forecast(String code, String userId, String deptId, List<FormComponent> formComponents) {

        log.debug(" 预测是否有自定义审批节点：code:{}，userId:{}，deptId:{}，formComponents:{}。",
                code,
                userId,
                deptId,
                formComponents);

        return this.forecast(code, userId, deptId, this.parseFormValue(formComponents, userId));

    }


    /**
     * 根据预测结果获取需要的审批人
     *
     * @param result
     * @return
     */
    private List<ProcessForecastResponseBody.ProcessForecastResponseBodyResultWorkflowActivityRulesWorkflowActor> getApproverListByForecast(
            ProcessForecastResponseBody.ProcessForecastResponseBodyResult result) {


        return result.getWorkflowActivityRules().stream()
                .filter(x -> "target_select".equals(x.getActivityType()))
                .map(ProcessForecastResponseBody.ProcessForecastResponseBodyResultWorkflowActivityRules::getWorkflowActor)
                .filter(x -> "approver".equals(x.getActorType()))
                .collect(Collectors.toList());
    }


    /**
     * 解析表单值,主要是解决个平台数据格式不一样的问题
     *
     * @param formComponents
     * @param dingTalkUserId
     * @return
     */
    private HashMap<String, String> parseFormValue(List<FormComponent> formComponents, String dingTalkUserId) {
        HashMap<String, String> formMap = new HashMap<>(formComponents.size());


        for (FormComponent formComponent : formComponents) {
            formMap.putAll(this.parseFormValue(formComponent, dingTalkUserId));
        }

        return formMap;

    }


    /**
     * 解析将form数据转换为Map
     *
     * @param formComponent
     * @param dingTalkUserId
     * @return
     */
    private Map<String, String> parseFormValue(FormComponent formComponent, String dingTalkUserId) {
        return ccf.getConvert(formComponent.getComponentType()).toOther(formComponent, dingTalkUserId);
    }
/*    private HashMap<String, String> parseFormValue(FormComponent formComponent, String userId) {
        HashMap<String, String> formMap = new HashMap<>(1);

        switch (formComponent.getComponentType()) {
            case DATE:
                formMap.put(formComponent.getName(), ComponentParseAll.COMPONENT_PARSE_DATE.toJava(formComponent.getValue()).toDateStr());
                break;
            case DATE_RANGE:
                DateRangeComponent rangeComponent = ComponentParseAll.COMPONENT_PARSE_DATE_RANGE.toJava(formComponent.getValue());
                String begin = rangeComponent.getDateFormat().getParse().format(rangeComponent.getBegin());
                String end = rangeComponent.getDateFormat().getParse().format(rangeComponent.getEnd());
                formMap.put(formComponent.getName(), JSONArray.toJSONString(new String[]{begin, end}));
                break;
            case ATTACHMENT:

                String spaceId = null;
                String unionId = null;
                spaceId = this.getProcessInstancesSpaces(userId).toString();

                unionId = DingTalkUser.getUserInfo(userId, dingTalkConfig).getUnionid();

                List<AttachmentComponent> attachmentComponents =
                        ComponentParseAll.COMPONENT_PARSE_ATTACHMENT.toJava(formComponent.getValue());
                JSONArray array = new JSONArray();
                for (AttachmentComponent attachmentComponent : attachmentComponents) {

                    CommitFileResponseBody fileBody = DingTalkFile.uploadFile(unionId,
                            spaceId,
                            attachmentComponent.getName(),
                            attachmentComponent.getSize(),
                            attachmentComponent.getUrl(),
                            dingTalkConfig);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("spaceId", fileBody.getDentry().getSpaceId());
                    jsonObject.put("fileName", fileBody.getDentry().getName());
                    jsonObject.put("fileSize", fileBody.getDentry().getSize());
                    jsonObject.put("fileType", fileBody.getDentry().getType());
                    jsonObject.put("fileId", fileBody.getDentry().getId());
                    array.add(jsonObject);
                }
                formMap.put(formComponent.getName(), array.toJSONString());
                break;
            case TABLE:

                Collection<Collection<FormComponent>> tableValue =
                        ComponentParseAll.COMPONENT_PARSE_TABLE.toJava(formComponent.getValue());

                JSONArray table = new JSONArray();
                for (Collection<FormComponent> componentList : tableValue) {

                    JSONArray row = new JSONArray();
                    for (FormComponent component : componentList) {
                        HashMap<String, String> value = this.parseFormValue(component, userId);

                        for (Map.Entry<String, String> entry : value.entrySet()) {
                            JSONObject json = new JSONObject();
                            json.put("name", entry.getKey());
                            json.put("value", entry.getValue());
                            row.add(json);
                        }

                    }

                    table.add(row);
                }
                formMap.put(formComponent.getName(), table.toJSONString());
                break;
            default:
                formMap.put(formComponent.getName(), formComponent.getValue());
                break;
        }

        return formMap;
    }*/


    /**
     * 获取流程实例的空间
     *
     * @param userId
     * @return
     */
    private Long getProcessInstancesSpaces(String userId) {

        log.debug(" 获取流程实例的空间：userId:{}。",
                userId);
        com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceHeaders getAttachmentSpaceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceHeaders();

        com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceRequest getAttachmentSpaceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceRequest()
                .setUserId(userService.getUserId(userId, getType()))
                .setAgentId(dingTalkConfig.getAgentId());

        try {
            com.aliyun.dingtalkworkflow_1_0.Client client = getClient();
            getAttachmentSpaceHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();
            GetAttachmentSpaceResponse attachmentSpaceWithOptions = client.getAttachmentSpaceWithOptions(getAttachmentSpaceRequest, getAttachmentSpaceHeaders, new RuntimeOptions());
            Long spaceId = attachmentSpaceWithOptions.getBody().getResult().getSpaceId();

            log.debug(" 获取钉钉审批空间详情成功，userId:{},spaceId:{}", userId, spaceId);

            return spaceId;

        } catch (TeaException err) {

            log.error(" 获取钉钉审批空间详情失败，userId{},code:{},message:{}", userId, err.getCode(), err.getMessage());
            throw err;
        } catch (Exception _err) {
            throw new TeaException(_err.getMessage(), _err);
        }
    }


    /*private static com.aliyun.dingtalkworkflow_1_0.Client getClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkworkflow_1_0.Client(config);
    }*/


}
