package org.bigant.fw.lark;


import com.lark.oapi.core.request.EventReq;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.event.CustomEventHandler;
import com.lark.oapi.event.EventDispatcher;
import com.lark.oapi.service.im.ImService;
import com.lark.oapi.service.im.v1.model.P2MessageReceiveV1;
import com.lark.oapi.ws.Client;

import java.nio.charset.StandardCharsets;

/**
 * 飞书长链接回调
 *
 * @author galen
 * @date 2024/3/1213:42
 */
public class LarkEvent {

    private LarkCallback larkCallback;
    private EventDispatcher eventDispatcher;
    private LarkConfig larkConfig;
    private Client client;

    public LarkEvent(String appId, String appSecret, LarkCallback larkCallback) {
        this.larkCallback = larkCallback;
        this.eventDispatcher = EventDispatcher.newBuilder("", "")
                .onP2MessageReceiveV1(new ImService.P2MessageReceiveV1Handler() {
                    @Override
                    public void handle(P2MessageReceiveV1 event) throws Exception {

                        System.out.printf("[ onP2MessageReceiveV1 access ], data: %s\n", Jsons.DEFAULT.toJson(event.getEvent()));
                    }
                })
                .onCustomizedEvent("message", new CustomEventHandler() {
                    @Override
                    public void handle(EventReq event) throws Exception {
                        System.out.printf("[ onCustomizedEvent access ], type: message, data: %s\n", new String(event.getBody(), StandardCharsets.UTF_8));
                    }
                })
                .build();

        this.client = new Client.Builder(appId, appSecret)
                .eventHandler(eventDispatcher)
                .build();
    }

    public void start() {
        client.start();
    }


}
