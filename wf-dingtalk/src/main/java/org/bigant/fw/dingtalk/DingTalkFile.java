package org.bigant.fw.dingtalk;

import com.aliyun.dingtalkdrive_1_0.models.*;
import com.aliyun.dingtalkstorage_1_0.Client;
import com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
     * @param md5
     * @param spaceId
     * @param dingTalkConfig
     */
    public static void uploadFile(String unionId, String fileName, Long size, String md5, String spaceId, String filePath, DingTalkConfig dingTalkConfig) throws IOException {
        log.debug("上传文件，unionId:{}，fileName:{}，size:{}，md5:{}，spaceId:{}，filePath:{}", unionId, fileName, size, md5, size, filePath);
        uploadFile(unionId, fileName, size, md5, spaceId, new URL(filePath).openStream(), dingTalkConfig);

    }

    /**
     * 上传文件
     *
     * @param unionId
     * @param fileName
     * @param size
     * @param md5
     * @param spaceId
     * @param dingTalkConfig
     */
    public static void uploadFile(String unionId, String fileName, Long size, String md5, String spaceId, InputStream is, DingTalkConfig dingTalkConfig) {
        log.debug("上传文件，unionId:{}，fileName:{}，size:{}，md5:{}，spaceId:{}", unionId, fileName, size, md5, size);
        GetUploadInfoResponseBody uploadInfo = getUploadInfo(unionId, fileName, size, md5, spaceId, dingTalkConfig);

        // 阿里云账号的临时accessKeyId。
        String accessKeyId = "<accessKeyId>";
        // 阿里云账号的临时accessKeySecret。
        String accessKeySecret = "<accessKeySecret>";
        // 临时访问密钥。
        String securityToken = "<accessToken>";
        // OSS访问域名。
        String endpoint = "<endpoint>";
        // OSS存储空间。
        String bucket = "<bucket>";
        // 对应OSS Object Key，可用于刷新token以及调用添加文件（夹）接口添加文件记录。
        String ossKey = "<mediaId>";
        GetUploadInfoResponseBody.GetUploadInfoResponseBodyStsUploadInfo stsUploadInfo = uploadInfo.getStsUploadInfo();

        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(stsUploadInfo.getAccessKeyId(),
                stsUploadInfo.getAccessKeySecret(),
                stsUploadInfo.getAccessToken());

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTPS); // 注意, 需要是HTTPS
        OSSClient ossClient = new OSSClient(endpoint, credentialsProvider, clientConfiguration);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, ossKey, new File("<path_to_file>"));

        ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();

    }

    /**
     * 获取文件上传信息
     *
     * @param unionId
     * @param fileName
     * @param size
     * @param md5
     * @param spaceId
     * @param dingTalkConfig
     * @return
     */
    public static GetUploadInfoResponseBody getUploadInfo(String unionId, String fileName, Long size, String md5, String spaceId, DingTalkConfig dingTalkConfig) {

        log.debug("获取文件上传信息，unionId:{}，fileName:{}，size:{}，md5:{}，spaceId:{}", unionId, fileName, size, md5, size);

        com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoHeaders getFileUploadInfoHeaders = new com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoHeaders();
        getFileUploadInfoHeaders.xAcsDingtalkAccessToken = "<your access token>";
        com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest.GetFileUploadInfoRequestOptionPreCheckParam optionPreCheckParam = new com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest.GetFileUploadInfoRequestOptionPreCheckParam()
                .setMd5("md5")
                .setSize(512L)
                .setParentId("0")
                .setName("dentry_name");
        com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest.GetFileUploadInfoRequestOption option = new com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest.GetFileUploadInfoRequestOption()
                .setStorageDriver("DINGTALK")
                .setPreCheckParam(optionPreCheckParam)
                .setPreferRegion("ZHANGJIAKOU")
                .setPreferIntranet(true);
        com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest getFileUploadInfoRequest = new com.aliyun.dingtalkstorage_1_0.models.GetFileUploadInfoRequest()
                .setUnionId("cHtUxxxxx")
                .setProtocol("HEADER_SIGNATURE")
                .setMultipart(false)
                .setOption(option);
        try {

            com.aliyun.dingtalkstorage_1_0.Client client = createClient();
            GetFileUploadInfoResponse fileUploadInfoWithOptions = client.getFileUploadInfoWithOptions(spaceId, getFileUploadInfoRequest, getFileUploadInfoHeaders, new RuntimeOptions());
            fileUploadInfoWithOptions.getBody().getHeaderSignatureInfo();
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
        /*log.debug("获取文件上传信息，unionId:{}，fileName:{}，size:{}，md5:{}，spaceId:{}", unionId, fileName, size, md5, size);


        GetUploadInfoHeaders getUploadInfoHeaders = new GetUploadInfoHeaders();

        GetUploadInfoRequest getUploadInfoRequest = new GetUploadInfoRequest()
                .setUnionId(unionId)
                .setFileName(fileName)
                .setFileSize(size)
                .setMd5(md5)
                .setAddConflictPolicy("autoRename")
                .setCallerRegion("BEIJING");
        try {

            Config config = new Config();
            config.protocol = "https";
            config.regionId = "central";
            com.aliyun.dingtalkdrive_1_0.Client client = new com.aliyun.dingtalkdrive_1_0.Client(config);

            getUploadInfoHeaders.xAcsDingtalkAccessToken = dingTalkConfig.getAccessToken();

            GetUploadInfoResponse uploadInfoWithOptions = client.getUploadInfoWithOptions(spaceId,
                    "0",
                    getUploadInfoRequest,
                    getUploadInfoHeaders,
                    new RuntimeOptions());

            return uploadInfoWithOptions.getBody();
        } catch (TeaException err) {
            log.error("获取文件上传信息异常", err);
            throw err;
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            log.error("获取文件上传信息异常", err);
            throw err;
        }*/
    }

    /**
     * 上传文件
     *
     * @param fileName
     * @param mediaId
     * @param unionId
     * @param spaceId
     * @return
     */
    public static AddFileResponse addFileWithOptions(String fileName, String mediaId, String unionId, String spaceId, DingTalkConfig dingTalkConfig) {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        com.aliyun.dingtalkdrive_1_0.Client client = null;
        try {
            client = new com.aliyun.dingtalkdrive_1_0.Client(config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            AddFileHeaders addFileHeaders = new AddFileHeaders();
            addFileHeaders.xAcsDingtalkAccessToken = dingTalkConfig.getAccessToken();
            AddFileRequest addFileRequest = new AddFileRequest()
                    .setFileType("file")
                    .setFileName(fileName)
                    .setMediaId(mediaId)
                    .setAddConflictPolicy("autoRename")
                    .setUnionId(unionId);

            assert client != null;
            return client.addFileWithOptions(spaceId,
                    addFileRequest,
                    addFileHeaders,
                    new RuntimeOptions());

        } catch (TeaException err) {
            throw err;
        } catch (Exception _err) {
            throw new TeaException(_err.getMessage(), _err);
        }
    }


    public static Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkstorage_1_0.Client(config);
    }
}
