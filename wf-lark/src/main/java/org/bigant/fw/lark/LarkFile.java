package org.bigant.fw.lark;

import com.google.gson.annotations.SerializedName;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.response.BaseResponse;
import com.lark.oapi.core.response.RawResponse;
import com.lark.oapi.core.token.AccessTokenType;
import com.lark.oapi.core.utils.UnmarshalRespUtil;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.exception.WfException;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * 飞书文件相关
 *
 * @author galen
 * @date 2024/2/2116:54
 */
@Slf4j
@AllArgsConstructor
public class LarkFile {

    LarkConfig larkConfig;

    public String uploadFile(String type, String fileName, String fileUrl, Long fileSize) {
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
