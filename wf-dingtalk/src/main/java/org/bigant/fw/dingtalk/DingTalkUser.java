package org.bigant.fw.dingtalk;

import com.alibaba.fastjson2.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.taobao.api.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.exception.WfException;

import java.util.concurrent.TimeUnit;

/**
 * 钉钉用户
 *
 * @author galen
 * date 2024/2/514:52
 */
@AllArgsConstructor
@Slf4j
public class DingTalkUser {

    private DingTalkConfig dingTalkConfig;
    private ICache cache;

    private static final String CACHE_KEY = DingTalkConstant.CACHE_KEY + "instances:openId:";
    private static final DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");

    /**
     * 获取钉钉用户详情
     * 接口地址：
     *
     */
    public OapiV2UserGetResponse.UserGetResponse getUserInfo(String dingTalkUserId) {

        log.debug("获取钉钉用户详情,userId:{}", dingTalkUserId);

        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(dingTalkUserId);
        req.setLanguage("zh_CN");

        OapiV2UserGetResponse rsp = null;
        try {
            rsp = client.execute(req, dingTalkConfig.accessToken());

            if (!rsp.isSuccess()) {
                log.error("钉钉-获取用户详情失败,userid:{}，errcode:{} errmsg:{}", dingTalkUserId, rsp.getErrcode(), rsp.getErrmsg());
                throw new WfException("钉钉-获取钉钉用户详情失败,errcode:" + rsp.getErrcode() + ",errmsg:" + rsp.getErrmsg());
            }

            log.debug("钉钉-获取用户详情成功,userid:{},data:{}", dingTalkUserId, JSONObject.toJSONString(rsp.getResult()));
            return rsp.getResult();
        } catch (ApiException e) {
            log.error("钉钉-获取用户详情失败，userId:{}", dingTalkUserId, e);
            throw new WfException("获取钉钉用户详情失败", e);
        }

    }

    public String getUnionId(String dingTalkUserId) {
        String spaceId = cache.get(CACHE_KEY + dingTalkUserId);
        if (spaceId != null) {
            return spaceId;
        }

        String unionId = this.getUserInfo(dingTalkUserId).getUnionid();
        cache.set(CACHE_KEY + dingTalkUserId, unionId, 1000, TimeUnit.DAYS);
        return unionId;
    }


}
