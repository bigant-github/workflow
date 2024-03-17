package org.bigant.fw.dingtalk.process;

import com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdResponse;
import com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.DingTalkConfig;
import org.bigant.fw.dingtalk.DingTalkConstant;
import org.bigant.fw.dingtalk.process.form.DingTalkFDTCF;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.process.ProcessService;
import org.bigant.wf.process.bean.ProcessDetail;
import org.bigant.wf.process.bean.ProcessPage;
import org.bigant.wf.process.bean.ProcessPageQuery;
import org.bigant.wf.process.form.FormDetailItem;
import org.bigant.wf.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表单service
 *
 * @author galen
 * date 2024/1/30 15:33
 */
@Slf4j
@AllArgsConstructor
public class DingTalkProcessService implements ProcessService {

    private DingTalkConfig dingTalkConfig;
    private UserService userService;
    @Getter
    private com.aliyun.dingtalkworkflow_1_0.Client client;
    private DingTalkFDTCF dingTalkFDTCF;

    @Override
    public List<ProcessPage> page(ProcessPageQuery processPageQuery, String userId) {

        try {
            com.aliyun.dingtalkworkflow_1_0.Client client = getClient();
            com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesHeaders listUserVisibleBpmsProcessesHeaders = new com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesHeaders();
            listUserVisibleBpmsProcessesHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();
            com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesRequest listUserVisibleBpmsProcessesRequest = new com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesRequest()
                    .setUserId(userService.getOtherUserIdByUserId(userId, this.getChannelName()))
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

            com.aliyun.dingtalkworkflow_1_0.Client client = getClient();
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


    /**
     * 钉钉查询详情
     * 接口文档地址：<a href="https://open.dingtalk.com/document/orgapp/obtain-the-form-schema">...</a>
     *
     * @param processCode 流程定义code
     * @return 详情
     */
    @Override
    public ProcessDetail detail(String processCode) {

        log.debug("钉钉-查询详情：processCode:{}", processCode);
        com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeHeaders querySchemaByProcessCodeHeaders = new com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeHeaders();
        querySchemaByProcessCodeHeaders.xAcsDingtalkAccessToken = dingTalkConfig.accessToken();

        com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeRequest querySchemaByProcessCodeRequest = new com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeRequest()
                .setProcessCode(processCode)
                .setAppUuid(null);

        try {
            QuerySchemaByProcessCodeResponseBody.QuerySchemaByProcessCodeResponseBodyResult result =
                    client.querySchemaByProcessCodeWithOptions(querySchemaByProcessCodeRequest,
                                    querySchemaByProcessCodeHeaders,
                                    new RuntimeOptions())
                            .getBody()
                            .getResult();

            List<QuerySchemaByProcessCodeResponseBody.QuerySchemaByProcessCodeResponseBodyResultSchemaContentItems> items =
                    result.getSchemaContent().getItems();

            List<FormDetailItem> form = items.stream()
                    .map(x -> dingTalkFDTCF.getByOtherType(x.getComponentName()).toFormDetail(x))
                    .collect(Collectors.toList());


            return ProcessDetail.builder()
                    .processCode(processCode)
                    .name(result.getName())
                    .iconUrl(result.getIcon())
                    .form(form)
                    .build();

        } catch (Exception _err) {
            String errMsg = String.format("钉钉-查询详情失败，processCode:%s", processCode);
            log.error(errMsg, _err);
            throw new WfException(errMsg);

        }

    }


    /**
     * 获取钉钉用户id
     */
    private String getDingTalkUserId(String userId) {
        return userService.getOtherUserIdByUserId(userId, getChannelName());
    }

    @Override
    public String getChannelName() {
        return DingTalkConstant.NAME;
    }

}
