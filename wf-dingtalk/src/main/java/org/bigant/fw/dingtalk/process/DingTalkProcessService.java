package org.bigant.fw.dingtalk.process;

import com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.AllArgsConstructor;
import org.bigant.fw.dingtalk.DingTalkConfig;
import org.bigant.fw.dingtalk.DingTalkConstant;
import org.bigant.wf.process.ProcessService;
import org.bigant.wf.process.bean.ProcessDetail;
import org.bigant.wf.process.bean.ProcessPage;
import org.bigant.wf.process.bean.ProcessPageQuery;
import org.bigant.wf.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表单service
 *
 * @author galen
 * @date 2024/1/30 15:33
 */
@AllArgsConstructor
public class DingTalkProcessService implements ProcessService {

    private DingTalkConfig dingTalkConfig;
    private UserService userService;

    @Override
    public List<ProcessPage> page(ProcessPageQuery processPageQuery, String userId) {

        try {
            com.aliyun.dingtalkworkflow_1_0.Client client = createClient();
            com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesHeaders listUserVisibleBpmsProcessesHeaders = new com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesHeaders();
            listUserVisibleBpmsProcessesHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();
            com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesRequest listUserVisibleBpmsProcessesRequest = new com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesRequest()
                    .setUserId(userService.getUserId(userId, this.getType()))
                    .setMaxResults(100L)
                    .setNextToken(0L);
            return client.listUserVisibleBpmsProcessesWithOptions(listUserVisibleBpmsProcessesRequest,
                            listUserVisibleBpmsProcessesHeaders,
                            new RuntimeOptions())
                    .getBody()
                    .getResult()
                    .getProcessList()
                    .stream()
                    .map(x -> ProcessPage.builder()
                            .code(x.getProcessCode())
                            .title(x.getName())
                            .iconUrl(x.getIconUrl())
                            .build())
                    .collect(Collectors.toList());
        } catch (TeaException err) {
            throw err;
        } catch (Exception _err) {
            throw new TeaException(_err.getMessage(), _err);
        }
    }

    @Override
    public List<ProcessPage> allPage(ProcessPageQuery query) {
        try {

            com.aliyun.dingtalkworkflow_1_0.Client client = createClient();
            com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdHeaders getManageProcessByStaffIdHeaders =
                    new com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdHeaders();

            getManageProcessByStaffIdHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();

            com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdRequest getManageProcessByStaffIdRequest =
                    new com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdRequest()
                            .setUserId(this.getDingTalkUserId(dingTalkConfig.getManagerUserId()));

            GetManageProcessByStaffIdResponse manageProcessByStaffIdWithOptions =
                    client.getManageProcessByStaffIdWithOptions(getManageProcessByStaffIdRequest
                            , getManageProcessByStaffIdHeaders
                            , new RuntimeOptions());

            return manageProcessByStaffIdWithOptions.getBody().getResult()
                    .stream().map(x -> ProcessPage.builder()
                            .code(x.getProcessCode())
                            .title(x.getFlowTitle())
                            .iconUrl(x.getIconUrl())
                            .updateTime(LocalDateTime.parse(x.getGmtModified(), DateTimeFormatter.ISO_DATE_TIME))
                            .build())
                    .collect(Collectors.toList());
        } catch (TeaException err) {
            throw err;

        } catch (Exception _err) {
            throw new TeaException(_err.getMessage(), _err);
        }
    }

    @Override
    public ProcessDetail detail(String code) {
        return null;
    }


    /**
     * 获取钉钉用户id
     *
     * @param userId
     * @return
     */
    private String getDingTalkUserId(String userId) {
        return userService.getUserId(userId, getType());
    }

    @Override
    public String getType() {
        return DingTalkConstant.NAME;
    }


    /**
     * 使用 Token 初始化账号Client
     *
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dingtalkworkflow_1_0.Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkworkflow_1_0.Client(config);
    }
}
