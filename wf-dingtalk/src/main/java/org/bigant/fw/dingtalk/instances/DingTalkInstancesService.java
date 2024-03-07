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
import org.bigant.fw.dingtalk.instances.form.DingTalkFDCF;
import org.bigant.fw.dingtalk.instances.form.convert.DingTalkBaseFDC;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.instances.InstanceStatus;
import org.bigant.wf.instances.InstancesService;
import org.bigant.wf.instances.bean.*;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.task.TaskStatus;
import org.bigant.wf.user.UserService;
import org.bigant.wf.user.vo.User;

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
    private DingTalkFDCF dingTalkFDCF;
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

        String dingTalkUserId = userService.getOtherUserIdByUserId(instanceStart.getUserId(), this.getType());

        StartProcessInstanceHeaders startProcessInstanceHeaders = new
                StartProcessInstanceHeaders();

        ArrayList<StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues> valuesDetailsDetails
                = new ArrayList<>();

        //转换表单值
        HashMap<String, String> formComponents = this.parseFormValue(instanceStart.getFormData(), dingTalkUserId);

        for (Map.Entry<String, String> formComponent : formComponents.entrySet()) {
            StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues value =
                    new StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues()
                            .setName(formComponent.getKey())
                            .setValue(formComponent.getValue());
            valuesDetailsDetails.add(value);
        }


        //匹配自选审批节点
        List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> selectActioners = new ArrayList<>();

        if (instanceStart.getSelectApproverUsers() != null
                && !instanceStart.getSelectApproverUsers().isEmpty()) {

            selectActioners = this.parseTargetSelectUsers(instanceStart);

        } else if (instanceStart.getAuthMatchSelectApproverUsers() != null
                && !instanceStart.getAuthMatchSelectApproverUsers().isEmpty()) {

            //自选节点自动匹配
            selectActioners = this.parseTargetSelectUsersAuthMatch(instanceStart, formComponents);
        }

        List<String> ccList = new ArrayList<>();

        /*List<String> ccUsers = instanceStart.getCcUsers();
        if (ccList != null && !ccList.isEmpty()) {
            ccList = ccUsers.stream()
                    .map(x -> userService.getOtherUserIdByUserId(x, getType()))
                    .collect(Collectors.toList());
        }*/

        StartProcessInstanceRequest startProcessInstanceRequest = new StartProcessInstanceRequest()
                .setOriginatorUserId(dingTalkUserId)
                .setProcessCode(instanceStart.getProcessCode())
                .setDeptId(instanceStart.getDeptId() != null && !instanceStart.getDeptId().isEmpty() ?
                        Long.parseLong(userService.getOtherDeptIdByDeptId(instanceStart.getDeptId(), this.getType()))
                        : null)
                /*.setMicroappAgentId(41605932L)*/
                /*.setApprovers(java.util.Arrays.asList(approvers0))*/
                .setCcList(ccList)
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
            log.debug("发起审批实例成功：processCode:{}，instanceCode:{}", instanceStart.getProcessCode(), instanceId);

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

    /**
     * 查询审批示例详情
     * 接口地址：https://open.dingtalk.com/document/orgapp/obtains-the-details-of-a-single-approval-instance-pop
     *
     * @param instanceCode
     * @return
     */
    @Override
    public InstanceDetailResult detail(String instanceCode) {

        log.debug("钉钉-查询审批示例详情。instanceCode:{}", instanceCode);

        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders =
                new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();

        getProcessInstanceHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();

        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest =
                new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
                        .setProcessInstanceId(instanceCode);

        try {


            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult result =
                    client.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders
                                    , new RuntimeOptions())
                            .getBody()
                            .getResult();

            log.debug("钉钉-查询审批示例成功。instanceCode:{}", instanceCode);

            //将钉钉返回数据转换为InstanceDetailResult

            //钉钉userId获取系统userId
            String userId = userService.getUserIdByOtherUserId(result.getOriginatorUserId(), getType());
            //钉钉部门Id获取系统部门Id
            String deptId = userService.getOtherDeptIdByDeptId(result.getOriginatorDeptId(), getType());


            String status = result.getStatus();
            String resultStatus = result.getResult();


            InstanceStatus instanceStatus = null;
            /*
             * status 状态
             * NEW：新创建
             * RUNNING：审批中
             * TERMINATED：被终止
             * COMPLETED：完成
             * CANCELED：取消
             */

            /*
             * resultStatus 状态 status为COMPLETED且result为agree时，表示审批单完结并审批通过。
             * agree：同意
             * refuse：拒绝
             */
            switch (status) {
                case "NEW":
                    instanceStatus = InstanceStatus.WAITING;
                    break;
                case "RUNNING":
                    instanceStatus = InstanceStatus.RUNNING;
                    break;
                case "COMPLETED":
                    switch (resultStatus) {
                        case "agree":
                            instanceStatus = InstanceStatus.AGREED;
                            break;
                        case "refuse":
                            instanceStatus = InstanceStatus.REFUSED;
                            break;
                        default:
                            String errMsg = String.format("钉钉-查询审批示例详情，无法识别的实例状态 resultStatus:%s", resultStatus);
                            log.error(errMsg);
                            throw new WfException(errMsg);
                    }
                    break;
                case "TERMINATED":
                case "CANCELED":
                    instanceStatus = InstanceStatus.CANCELED;
                    break;
                default:
                    String errMsg = String.format("钉钉-查询审批示例详情，无法识别的实例状态 status:%s", status);
                    log.error(errMsg);
                    throw new WfException(errMsg);
            }


            List<FormDataItem> formDataItemList = new ArrayList<>();
            //将form数据转换成系统form实体
            for (GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue
                    : result.getFormComponentValues()) {

                DingTalkBaseFDC fdc = dingTalkFDCF.getByOtherType(formComponentValue.getComponentType());
                FormDataItem formDataItem = fdc.toFormData(formComponentValue);
                formDataItemList.add(formDataItem);

            }

            List<InstanceDetailResult.Task> tasks = new ArrayList<>();
            //将task转换为系统task实体
            for (GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultTasks task : result.getTasks()) {

                String taskUserId = userService.getUserIdByOtherUserId(task.getUserId(), getType());
                User taskUser = userService.getUser(taskUserId);

                TaskStatus taskStatus;

                String instancesTaskStatus = task.getStatus();

                String instancesTaskResultStatus = task.getResult();



                /*
                 *  instancesTaskStatus
                 *  NEW：未启动
                 *  RUNNING：处理中
                 *  PAUSED：暂停
                 *  CANCELED：取消
                 *  COMPLETED：完成
                 *  TERMINATED：终止
                 */
                /*
                 *  instancesTaskResultStatus
                 *  AGREE：同意
                 *  REFUSE：拒绝
                 *  REDIRECTED：转交
                 */

                switch (instancesTaskStatus) {
                    case "NEW":
                        taskStatus = TaskStatus.WAITING;
                        break;
                    case "RUNNING":
                        taskStatus = TaskStatus.RUNNING;
                        break;
                    case "PAUSED":
                        taskStatus = TaskStatus.PAUSED;
                        break;
                    case "CANCELED":
                        taskStatus = TaskStatus.CANCELED;
                        break;
                    case "COMPLETED":
                        switch (instancesTaskResultStatus) {
                            case "AGREE":
                                taskStatus = TaskStatus.AGREED;
                                break;
                            case "REFUSE":
                                taskStatus = TaskStatus.REFUSED;
                                break;
                            case "REDIRECTED":
                                taskStatus = TaskStatus.REDIRECTED;
                                break;
                            default:
                                String errMsg = String.format("钉钉-查询审批示例详情，无法识别的任务状态 resultStatus:%s", status);
                                log.error(errMsg);
                                throw new WfException(errMsg);
                        }
                    case "TERMINATED":
                        taskStatus = TaskStatus.CANCELED;
                        break;
                    default:
                        String errMsg = String.format("钉钉-查询审批示例详情，无法识别的任务状态 status:%s", status);
                        log.error(errMsg);
                        throw new WfException(errMsg);
                }


                tasks.add(InstanceDetailResult.Task.builder()
                        .taskCode(task.getTaskId().toString())
                        .userId(taskUserId)
                        .userName(taskUser.getUserName())
                        .taskStatus(taskStatus)
                        .build());

            }


            return InstanceDetailResult.builder()
                    .title(result.getTitle())
                    .instanceCode(instanceCode)
                    .userId(userId)
                    .deptId(deptId)
                    .instanceStatus(instanceStatus)
                    .formData(formDataItemList)
                    .tasks(tasks)
                    .build();


        } catch (TeaException err) {

            String errMsg = String.format("钉钉-获取审批示例详情失败。instanceCode:%s,errorCode:%s,message:%s",
                    instanceCode,
                    err.code,
                    err.message);

            log.error(errMsg, errMsg);
            throw new WfException(errMsg, err);

        } catch (Exception _err) {
            String errMsg = String.format("钉钉-获取审批示例详情失败。instanceCode:%s", instanceCode);
            log.error(errMsg, _err);
            throw new WfException(errMsg, _err);
        }
    }

    /**
     * 解析自选节点用户自动匹配
     */
    private List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> parseTargetSelectUsersAuthMatch(
            InstanceStart instanceStart,
            HashMap<String, String> formComponents) {

        List<InstanceStart.AuthMatchNodeUser> targetSelectUsersAuthMatch = instanceStart.getAuthMatchSelectApproverUsers();
        log.debug(" 发起审批实例：code:{}，共{}个节点，使用自选节点自动匹配。", instanceStart.getProcessCode(), targetSelectUsersAuthMatch.size());

        List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> users
                = new ArrayList<>(targetSelectUsersAuthMatch.size());

        ProcessForecastResponseBody.ProcessForecastResponseBodyResult forecast = this.preview(instanceStart.getProcessCode(),
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
                    .map(x -> userService.getOtherUserIdByUserId(x, this.getType()))
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

        List<InstanceStart.NodeUser> nodeUsers = instanceStart.getSelectApproverUsers();

        log.debug(" 发起审批实例：{}，共{}个节点，使用自选节点。", instanceStart.getProcessCode(), nodeUsers.size());


        List<StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners> users
                = new ArrayList<>(nodeUsers.size());

        for (InstanceStart.NodeUser nodeUser : nodeUsers) {

            List<String> userIds = nodeUser.getUserIds()
                    .stream()
                    .map(x -> userService.getOtherUserIdByUserId(x, this.getType()))
                    .collect(Collectors.toList());

            log.debug(" 发起审批实例：{}，节点Key：{}，共{}个用户：{}。"
                    , instanceStart.getProcessCode()
                    , nodeUser.getKey()
                    , userIds.size(), userIds);

            StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners user
                    = new StartProcessInstanceRequest.StartProcessInstanceRequestTargetSelectActioners()
                    .setActionerKey(nodeUser.getKey())
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
     */
    private ProcessForecastResponseBody.ProcessForecastResponseBodyResult preview(String processCode,
                                                                                  String userId,
                                                                                  String deptId,
                                                                                  Map<String, String> formComponents) {

        log.debug("钉钉-预览审批流：processCode:{}，userId:{}，deptId:{}，formComponents:{}。",
                processCode,
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
                .setProcessCode(processCode)
                .setDeptId(Integer.valueOf(userService.getOtherDeptIdByDeptId(deptId, getType())))
                .setUserId(userService.getOtherUserIdByUserId(userId, getType()))
                .setFormComponentValues(values);

        com.aliyun.dingtalkworkflow_1_0.Client client = getClient();
        processForecastHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();

        try {

            ProcessForecastResponseBody.ProcessForecastResponseBodyResult result =
                    client.processForecastWithOptions(processForecastRequest, processForecastHeaders, new RuntimeOptions())
                            .getBody().getResult();

            log.debug("钉钉-预览审批流成功。processCode:{}", processCode);

            /*if (log.isDebugEnabled()) {

                log.debug(" 预测是否有自定义审批节点结果：code:{}，userId:{}，deptId:{}，formComponents:{}，result:{}",
                        processCode,
                        userId,
                        deptId,
                        formComponents,
                        JSONObject.toJSONString(result));

                this.getApproverListByForecast(result)
                        .forEach(x -> log.debug(" 预测是否有自定义审批节点结果需要自选的节点：code:{}，key:{}",
                                processCode,
                                x.getActorKey()));

            }*/

            return result;
        } catch (Exception err) {
            String errMsg = String.format("钉钉-预览审批流失败：code:%s,userId:%s,deptId:%s,formComponents:%s,errorMsg:%s",
                    processCode,
                    userId,
                    deptId,
                    formComponents,
                    err.getMessage());
            log.error(errMsg, err);
            throw new WfException(errMsg, err);
        }
    }


    /**
     * 获取需要自选的节点
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
     * @param formDataItemList
     * @param dingTalkUserId
     * @return
     */
    private HashMap<String, String> parseFormValue(List<FormDataItem> formDataItemList, String dingTalkUserId) {
        HashMap<String, String> formMap = new HashMap<>(formDataItemList.size());


        for (FormDataItem formDataItem : formDataItemList) {
            formMap.putAll(this.parseFormValue(formDataItem, dingTalkUserId));
        }

        return formMap;

    }


    /**
     * 解析将form数据转换为Map
     *
     * @param formDataItem
     * @param dingTalkUserId
     * @return
     */
    private Map<String, String> parseFormValue(FormDataItem formDataItem, String dingTalkUserId) {
        return dingTalkFDCF.getByFormType(formDataItem.getComponentType()).toOther(formDataItem, dingTalkUserId);
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
                .setUserId(userService.getOtherUserIdByUserId(userId, getType()))
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
