package org.bigant.fw.dingtalk;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkstorage_1_0.Client;
import com.aliyun.dingtalkstorage_1_0.models.CommitFileResponseBody;
import com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoResponse;
import com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.exception.WfException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 钉钉云盘文件上传
 *
 * @author galen
 * @date 2024/2/515:15
 */
@Slf4j
public class DingTalkFile {


    /**
     * 上传文件
     *
     * @param unionId
     * @param fileName
     * @param size
     * @param spaceId
     * @param dingTalkConfig
     * @return
     */
    public static CommitFileResponseBody uploadFile(String unionId,
                                                    String spaceId,
                                                    String fileName,
                                                    Long size,
                                                    String filePath,
                                                    DingTalkConfig dingTalkConfig) {
        log.debug("根据上传文件，unionId:{}，spaceId:{}，fileName:{}，size:{}，filePath:{}", unionId, fileName, size, size, filePath);
        try {
            return uploadFile(unionId, spaceId, fileName, size, new URL(filePath).openStream(), dingTalkConfig);
        } catch (IOException e) {

            String errMsg = String.format("上传文件，unionId:%s，spaceId:%s，fileName:%s，size:%s，filePath:%s", unionId, fileName, size, size, filePath);

            log.error(errMsg, e);

            throw new WfException(errMsg, e);
        }

    }

    /**
     * 上传文件
     *
     * @param unionId
     * @param fileName
     * @param size
     * @param spaceId
     * @param dingTalkConfig
     * @return
     */
    public static CommitFileResponseBody uploadFile(String unionId, String spaceId, String fileName, Long size, InputStream is, DingTalkConfig dingTalkConfig) {
        log.debug("根据流上传文件，unionId:{}，fileName:{}，size:{}，spaceId:{}", unionId, fileName, size, size);
        GetFileUploadInfoResponseBody uploadInfo = getUploadInfo(unionId, spaceId, fileName, size, dingTalkConfig);
        GetFileUploadInfoResponseBody.GetFileUploadInfoResponseBodyHeaderSignatureInfo headerSignatureInfo = uploadInfo.getHeaderSignatureInfo();

        try {

            // 从接口返回信息中拿到resourceUrls
            String resourceUrl = headerSignatureInfo.getResourceUrls().get(0);
            // 从接口返回信息中拿到headers
            Map<String, String> headers = headerSignatureInfo.getHeaders();
            URL url = new URL(resourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setUseCaches(false);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.connect();
            OutputStream out = connection.getOutputStream();
            byte[] b = new byte[1024];
            int temp;
            while ((temp = is.read(b)) != -1) {
                out.write(b, 0, temp);
            }
            out.flush();
            out.close();
            int responseCode = connection.getResponseCode();
            connection.disconnect();


            if (responseCode == 200) {
                log.debug("上传文件成功，unionId:{}，spaceId:{}，fileName:{}，size:{}", unionId, spaceId, fileName, size);
                return commitFile(unionId, spaceId, fileName, size, dingTalkConfig, uploadInfo);
            } else {

                String errMsg = String.format("上传文件失败，unionId:%s，fileName:%s，size:%s，spaceId:%s，responseCode:%s",
                        unionId,
                        fileName,
                        size,
                        size,
                        responseCode);
                log.error(errMsg);
                throw new TeaException(errMsg, new RuntimeException(errMsg));

            }
        } catch (Exception e) {
            String errMsg = String.format("上传文件失败，unionId:%s，fileName:%s，size:%s，spaceId:%s，responseCode:%s，err",
                    unionId,
                    fileName,
                    size,
                    size,
                    e.getMessage());

            log.error(errMsg);
            throw new WfException(errMsg, e);
        }
    }

    /**
     * 提交文件
     *
     * @param unionId
     * @param fileName
     * @param size
     * @param spaceId
     * @param dingTalkConfig
     * @param uploadInfo
     * @return
     * @throws Exception
     */
    private static CommitFileResponseBody commitFile(String unionId, String spaceId, String fileName, Long size, DingTalkConfig dingTalkConfig, GetFileUploadInfoResponseBody uploadInfo) throws Exception {
        log.debug("提交文件，unionId:{}，spaceId:{}，fileName:{}，size:{}", unionId, fileName, size, spaceId);
        Client client = createClient();
        com.aliyun.dingtalkstorage_1_0.models.CommitFileHeaders commitFileHeaders = new com.aliyun.dingtalkstorage_1_0.models.CommitFileHeaders();
        commitFileHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();

        com.aliyun.dingtalkstorage_1_0.models.CommitFileRequest.CommitFileRequestOption option = new com.aliyun.dingtalkstorage_1_0.models.CommitFileRequest.CommitFileRequestOption()
                .setSize(size)
                .setConflictStrategy("AUTO_RENAME");

        com.aliyun.dingtalkstorage_1_0.models.CommitFileRequest commitFileRequest = new com.aliyun.dingtalkstorage_1_0.models.CommitFileRequest()
                .setUnionId(unionId)
                .setUploadKey(uploadInfo.getUploadKey())
                .setName(fileName)
                .setParentId("0")
                .setOption(option);
        try {
            CommitFileResponseBody body = client.commitFileWithOptions(spaceId, commitFileRequest, commitFileHeaders, new RuntimeOptions()).getBody();
            log.debug("提交文件成功，unionId:{}，fileName:{}，size:{}，spaceId:{}，body:{}", unionId, fileName, size, spaceId, JSONObject.toJSONString(body));
            return body;
        } catch (TeaException err) {
            log.error("提交文件失败，unionId:{}，fileName:{}，size:{}，spaceId:{}，errMsg:{}", unionId, fileName, size, spaceId, err.message);
            throw err;
        } catch (Exception _err) {
            log.error("提交文件失败，unionId:{}，fileName:{}，size:{}，spaceId:{}，errMsg:{}", unionId, fileName, size, spaceId, _err.getMessage());

            TeaException err = new TeaException(_err.getMessage(), _err);
            throw err;

        }
    }


    /**
     * 获取文件上传信息
     *
     * @param unionId
     * @param fileName
     * @param size
     * @param spaceId
     * @param dingTalkConfig
     * @return
     */
    public static GetFileUploadInfoResponseBody getUploadInfo(String unionId, String spaceId, String fileName, Long size, DingTalkConfig dingTalkConfig) {

        log.debug("获取文件上传信息，unionId:{}，fileName:{}，size:{}，spaceId:{}", unionId, fileName, size, size);

        com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoHeaders getFileUploadInfoHeaders = new com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoHeaders();
        getFileUploadInfoHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();

        com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest.GetFileUploadInfoRequestOptionPreCheckParam optionPreCheckParam = new com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest.GetFileUploadInfoRequestOptionPreCheckParam()
                .setMd5("md5")
                .setSize(size)
                .setParentId("0")
                .setName(fileName);
        com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest.GetFileUploadInfoRequestOption option = new com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest.GetFileUploadInfoRequestOption()
                .setStorageDriver("DINGTALK")
                .setPreCheckParam(optionPreCheckParam)
                .setPreferRegion("ZHANGJIAKOU")
                .setPreferIntranet(true);
        com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest getFileUploadInfoRequest = new com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest()
                .setUnionId(unionId)
                .setProtocol("HEADER_SIGNATURE")
                .setMultipart(false)
                .setOption(option);

        try {
            com.aliyun.dingtalkstorage_1_0.Client client = createClient();
            GetFileUploadInfoResponse fileUploadInfoWithOptions = client.getFileUploadInfoWithOptions(spaceId, getFileUploadInfoRequest, getFileUploadInfoHeaders, new RuntimeOptions());
            log.debug("钉钉-获取文件上传信息成功，unionId:{}，fileName:{}，size:{}，spaceId:{}，body:{}", unionId, fileName, size, size, JSONObject.toJSONString(fileUploadInfoWithOptions.getBody()));
            return fileUploadInfoWithOptions.getBody();
        } catch (Exception err) {
            String errMsg = String.format("钉钉-获取文件上传信息失败，unionId:%s，fileName:%s，size:%s，spaceId:%s", unionId, fileName, size, size);
            log.error(errMsg);
            throw new WfException(errMsg, err);
        }

    }


    public static Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkstorage_1_0.Client(config);
    }
}
