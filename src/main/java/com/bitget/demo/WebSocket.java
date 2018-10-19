package com.bitget.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


@Slf4j
public class WebSocket extends WebSocketClient {

    private URI uri;
    private String accessKey;
    private String secretKey;


    public static String KLINE = "kline_%s_%s";
    public static String DEPTH = "order_book_%s";
    public static String TRADE = "trade_history_%s";
    public static String TICKER = "ticker_%s";
    public static String PERIOD[] = {"1min", "3min", "5min", "15min", "30min", "1hour", "4hour", "12hour", "1day", "1week", "1month"};


    public WebSocket(URI uri, String accessKey, String secretKey) {
        super(uri, new Draft_17());
        this.uri = uri;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public void onOpen(ServerHandshake shake) {
        //包含v1为账户订单请求，否则是行情请求

        String event = "sub";
//      String topic = String.format(DEPTH, "ethusdt", TYPE[0]);
        String topic = String.format(KLINE, "btc_usdt", PERIOD[0]);
//      String topic = TICKERS;
        sendWs(event, topic);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendWs("ping", null);
            }
        }, 5000, 5000);
    }

    @Override
    public void onMessage(String arg0) {
        if (arg0 != null) {
            log.info("receive message " + arg0);
            JSONObject message = JSON.parseObject(arg0);
            String code = (String)message.get("code");
            if (code != null && !code.equalsIgnoreCase("0")) {
                log.info("订阅错误： {}", message.get("msg"));
            }
        }
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            String message = new String(ZipUtil.decompress(bytes.array()), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(message);
            if (!StringUtils.isEmpty(message)) {
                log.info("receive topic:{}, data: {}", jsonObject.get("topic"), jsonObject.get("data"));
            }
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception arg0) {
        String message = "";
        try {
            message = new String(arg0.getMessage().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("has error ,the message is :" + message);
        arg0.printStackTrace();
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {

        log.info("connection close ");
        log.info(arg0 + "   " + arg1 + "  " + arg2);
    }

    /**
     * 发送账户订单请求鉴权
     */
    public void addAuth() {
        Map<String, String> map = new HashMap<>();
        ApiSignature as = new ApiSignature();
        try {

            String theHost = uri.getHost() + ":" + uri.getPort();
            //组合签名map
            as.createSignature(accessKey, secretKey, "GET", theHost, uri.getPath(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put(ApiSignature.op, ApiSignature.opValue);
        map.put("cid", "111");

        String req = JSON.toJSONString(map);
        send(req);
    }

    /**
     * 行情请求
     *
     * @param event
     * @param topic
     */
    public void sendWs(String event, String topic) {
        JSONObject req = new JSONObject();
        req.put("event", event);
        req.put("topic", topic);
        send(req.toString());
    }

}
