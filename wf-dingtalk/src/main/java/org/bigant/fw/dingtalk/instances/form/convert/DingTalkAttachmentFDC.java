package org.bigant.fw.dingtalk.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkdrive_1_0.models.AddFileResponseBody;
import com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceResponse;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.*;
import org.bigant.wf.ComponentType;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.databean.FormDataAttachment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 钉钉附件类型转换器
 *
 * @author galen
 * date 2024/3/115:29
 */
@Slf4j
@AllArgsConstructor
public class DingTalkAttachmentFDC extends DingTalkBaseFDC {

    private final ICache cache;
    private final DingTalkConfig dingTalkConfig;
    private final DingTalkUser dingTalkUser;

    @Getter
    private com.aliyun.dingtalkworkflow_1_0.Client client;

    public static final String CACHE_KEY_SPACE_ID = DingTalkConstant.CACHE_KEY + "spaceId:";


    @Override
    public Map<String, String> toOther(FormDataItem data, String dingTalkUserId) {

        String spaceId = this.getProcessInstancesSpaces(dingTalkUserId).toString();

        String unionId = dingTalkUser.getUnionId(dingTalkUserId);

        Collection<FormDataAttachment> formDataAttachments =
                FormDataParseAll.COMPONENT_PARSE_ATTACHMENT.strToJava(data.getValue());
        JSONArray array = new JSONArray();
        for (FormDataAttachment formDataAttachment : formDataAttachments) {

            /*CommitFileResponseBody fileBody = DingTalkFile.uploadFile(unionId,
                    spaceId,
                    formDataAttachment.getName(),
                    formDataAttachment.getSize(),
                    formDataAttachment.getUrl(),
                    dingTalkConfig);

                    JSONObject jsonObject = new JSONObject();
            jsonObject.put("spaceId", fileBody.getDentry().getSpaceId());
            jsonObject.put("fileName", fileBody.getDentry().getName());
            jsonObject.put("fileSize", fileBody.getDentry().getSize());
            jsonObject.put("fileType", fileBody.getDentry().getType());
            jsonObject.put("fileId", fileBody.getDentry().getId());
            */

            AddFileResponseBody fileBody = DingTalkFileOld.updateFile(unionId,
                    spaceId,
                    formDataAttachment.getName(),
                    formDataAttachment.getUrl(),
                    dingTalkConfig);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("spaceId", fileBody.getSpaceId());
            jsonObject.put("fileName", fileBody.getFileName());
            jsonObject.put("fileSize", fileBody.getFileSize());
            jsonObject.put("fileType", fileBody.getFileType());
            jsonObject.put("fileId", fileBody.getFileId());
            array.add(jsonObject);
        }
        return toMap(data.getName(), array.toJSONString());
    }

    @Override
    public FormDataItem toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {

        String value = data.getValue();

        if (value == null || value.isEmpty()) {
            return FormDataItem.attachment(data.getName(), new ArrayList<>());
        }
        JSONArray jsonVal = JSONArray.parse(value);

        ArrayList<FormDataAttachment> attachments = new ArrayList<>();
        for (int i = 0; i < jsonVal.size(); i++) {

            JSONObject jsonFile = jsonVal.getJSONObject(i);

            attachments
                    .add(FormDataAttachment.builder()
                            .name(jsonFile.getString("fileName"))
                            .size(jsonFile.getLong("fileSize"))
                            .build());

        }

        return FormDataItem.attachment(data.getName(), attachments);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.ATTACHMENT;
    }

    @Override
    public Collection<String> getOtherType() {
        return DingTalkFormType.ATTACHMENT.getDingTalkType();
    }

    public Long getProcessInstancesSpaces(String dingTalkUserId) {

        /*TODO 暂时关掉缓存排查问题
        String spaceIdStr = cache.get(CACHE_KEY_SPACE_ID + dingTalkUserId);
        if (spaceIdStr != null) {
            return Long.valueOf(spaceIdStr);
        }*/

        log.info("钉钉-获取审批附件空间：userId:{}。", dingTalkUserId);
        com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceHeaders getAttachmentSpaceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceHeaders();

        com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceRequest getAttachmentSpaceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceRequest()
                .setUserId(dingTalkUserId)
                .setAgentId(dingTalkConfig.getAgentId());

        try {
            com.aliyun.dingtalkworkflow_1_0.Client client = getClient();
            getAttachmentSpaceHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();
            GetAttachmentSpaceResponse attachmentSpaceWithOptions = client.getAttachmentSpaceWithOptions(getAttachmentSpaceRequest, getAttachmentSpaceHeaders, new RuntimeOptions());
            Long spaceId = attachmentSpaceWithOptions.getBody().getResult().getSpaceId();

            log.debug("钉钉-获取审批附件空间成功，userId:{},spaceId:{}", dingTalkUserId, spaceId);


            cache.set(CACHE_KEY_SPACE_ID + dingTalkUserId, spaceId.toString(), 1000, TimeUnit.DAYS);

            return spaceId;

        } catch (TeaException err) {
            String errMsg = String.format("钉钉-获取审批空间详情失败，userId%s,code:%s,message:%s",
                    dingTalkUserId,
                    err.getCode(),
                    err.getMessage());
            log.error(errMsg);
            throw new WfException(errMsg, err);
        } catch (Exception _err) {
            throw new WfException(_err.getMessage(), _err);
        }
    }

}
