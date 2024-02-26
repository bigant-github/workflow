package org.bigant.fw.dingtalk;

import com.alibaba.fastjson2.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.taobao.api.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.exception.WfException;

/**
 * 钉钉用户
 *
 * @author galen
 * @date 2024/2/514:52
 */
@AllArgsConstructor
@Slf4j
public class DingTalkUser {

    /**
     * 获取钉钉用户详情
     *
     * @param dingTalkUserId
     * @param dingTalkConfig
     * @return
     */
    public static OapiV2UserGetResponse.UserGetResponse getUserInfo(String dingTalkUserId, DingTalkConfig dingTalkConfig) {

        log.debug("获取钉钉用户详情,userId:{}", dingTalkUserId);

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(dingTalkUserId);
        req.setLanguage("zh_CN");

        OapiV2UserGetResponse rsp = null;
        try {
            rsp = client.execute(req, dingTalkConfig.accessToken());

            if (!rsp.isSuccess()) {
                log.error("获取钉钉用户详情失败,userid:{}，errcode:{} errmsg:{}", dingTalkUserId, rsp.getErrcode(), rsp.getErrmsg());
                throw new WfException("获取钉钉用户详情失败,errcode:" + rsp.getErrcode() + ",errmsg:" + rsp.getErrmsg());
            }

            log.debug("获取钉钉用户详情成功,userid:{},data:{}", dingTalkUserId, JSONObject.toJSONString(rsp.getResult()));
            return rsp.getResult();
        } catch (ApiException e) {
            log.error("获取钉钉用户详情失败，userId:{}", dingTalkUserId, e);
            throw new WfException("获取钉钉用户详情失败", e);
        }


    }


}
