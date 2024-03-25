package org.bigant.fw.dingtalk;

import com.aliyun.dingtalkdrive_1_0.models.*;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.bigant.wf.exception.WfException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

/**
 * 钉钉上传文件旧版实现，因新版api总是提示403，故使用旧版api测试是否有问题，实现较为简陋，不宜长期使用。
 */
public class DingTalkFileOld {

    public static AddFileResponseBody updateFile(String unionId, String spaceId, String fileName, String url, DingTalkConfig dingTalkConfig) {


        GetUploadInfoResponseBody.GetUploadInfoResponseBodyStsUploadInfo stsUploadInfo;
        try {
            InputStream inputStream = new URL(url).openStream();
            GetUploadInfoResponse uploadInfo = getUploadInfo(unionId
                    , spaceId
                    , fileName
                    , (long) inputStream.available()
                    , getInputStreamMD5(inputStream)
                    , dingTalkConfig);
            stsUploadInfo = uploadInfo.getBody().getStsUploadInfo();

            CredentialsProvider credentialsProvider = new DefaultCredentialProvider(stsUploadInfo.getAccessKeyId()
                    , stsUploadInfo.getAccessKeySecret(), stsUploadInfo.getAccessToken());
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setProtocol(Protocol.HTTPS); // 注意, 需要是HTTPS
            OSSClient ossClient = new OSSClient(stsUploadInfo.getEndPoint(), credentialsProvider, clientConfiguration);
            PutObjectRequest putObjectRequest = new PutObjectRequest(stsUploadInfo.getBucket()
                    , stsUploadInfo.getMediaId()
                    , new URL(url).openStream());
            ossClient.putObject(putObjectRequest);
            // 关闭OSSClient。
            ossClient.shutdown();

            AddFileResponse addFileResponse = addFileWithOptions(fileName
                    , stsUploadInfo.getMediaId()
                    , unionId, spaceId, dingTalkConfig);
            return addFileResponse.getBody();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static GetUploadInfoResponse getUploadInfo(String uuid, String spaceId, String originalName, Long attachSize, String md5, DingTalkConfig dingTalkConfig) {


        GetUploadInfoHeaders getUploadInfoHeaders = new GetUploadInfoHeaders();
        getUploadInfoHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();
        GetUploadInfoRequest getUploadInfoRequest = new GetUploadInfoRequest()
                .setUnionId(uuid)
                .setFileName(originalName)
                .setFileSize(attachSize)
                .setMd5(md5)
                .setAddConflictPolicy("autoRename")
                .setCallerRegion("BEIJING");
        try {
            Config config = new Config();
            config.protocol = "https";
            config.regionId = "central";
            com.aliyun.dingtalkdrive_1_0.Client client = new com.aliyun.dingtalkdrive_1_0.Client(config);
            return client.getUploadInfoWithOptions(spaceId
                    , "0"
                    , getUploadInfoRequest
                    , getUploadInfoHeaders
                    , new RuntimeOptions());
        } catch (Exception err) {
            throw new WfException("钉钉-获取文件上传信息失败", err);
        }

    }

    public static String getInputStreamMD5(InputStream inputStream) {

        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");


            //Get the checksum
            return getInputStreamChecksum(md5Digest, inputStream);
        } catch (Exception e) {
            throw new WfException("钉钉-上传文件失败", e);
        }

    }

    private static String getInputStreamChecksum(MessageDigest digest, InputStream inputStream) throws IOException {
        InputStream fis = inputStream;

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }


    public static AddFileResponse addFileWithOptions(String fileName, String mediaId, String uuid, String spaceId, DingTalkConfig dingTalkConfig) throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        com.aliyun.dingtalkdrive_1_0.Client client;

        client = new com.aliyun.dingtalkdrive_1_0.Client(config);

        AddFileHeaders addFileHeaders = new AddFileHeaders();
        addFileHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();
        AddFileRequest addFileRequest = new AddFileRequest()
                .setFileType("file")
                .setFileName(fileName)
                .setMediaId(mediaId)
                .setAddConflictPolicy("autoRename")
                .setUnionId(uuid);

        return client.addFileWithOptions(spaceId
                , addFileRequest, addFileHeaders
                , new RuntimeOptions());

    }
}
