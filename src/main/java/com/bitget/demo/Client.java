package com.bitget.demo;


import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.DefaultWebSocketClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Service
@Scope(SCOPE_PROTOTYPE)
public class Client {

    @Value("${uri.protocol}")
    String protocol;

    @Value("${uri.host}")
    String host;

    @Value("${uri.market.path}")
    String market;

    @Value("${uri.port}")
    String port;


    @Value("${accessKey}")
    String accessKey;

    @Value("${secretKey}")
    String secretKey;


    public void connect(String path) {
        WebSocket webSocket = null;
        try {
            URI uri = new URI(protocol + host + ":" + port + path);

            webSocket = new WebSocket(uri, accessKey, secretKey);

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) {
                }
            }};


            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            webSocket.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        webSocket.connect();
    }
}
