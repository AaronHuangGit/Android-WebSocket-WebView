package com.example.websocketviewtest;

import android.util.Log;
import android.webkit.JavascriptInterface;
import com.websocket.WebSocket;
import com.websocket.client.WebSocketClient;
import com.websocket.drafts.Draft;
import com.websocket.drafts.Draft_17;
import com.websocket.handshake.ServerHandshake;
import com.websocket.util.Base64;

import java.net.URI;
import java.util.Random;

/**
 * Created by AaronHuang on 16/6/21.
 *
 * @version 3.0.1
 * @auchor ran.huang
 */
public class WebSocketJavaScriptObject {
    private String mId;
    private Draft mDraft; //websocket draft草案版本,10 17 75 76
    private static final String TAG = "aaron";
    /**
     * websocket连接后调用javascript对应的方法名
     */
    private static String EVENT_ON_OPEN = "onopen";
    /**
     * websocket客户端接收到服务器消息调用javascript对应的方法名
     */
    private static String EVENT_ON_MESSAGE = "onmessage";
    /**
     * websocket连接关闭时调用javascript对应的方法名
     */
    private static String EVENT_ON_CLOSE = "onclose";
    /**
     * websocket事件异常调用javascript对应的方法名
     */
    private static String EVENT_ON_ERROR = "onerror";

    private static String BLANK_MESSAGE = "";

    public static final String UTF8_CHARSET = "UTF-8";

    private WebSocketClient mWebSocketClient;
    private WebSocketWebView mWebSocketWebView;


    public WebSocketJavaScriptObject(WebSocketWebView webSocketWebView) {
        init(webSocketWebView);
        mDraft = new Draft_17();
    }
    public WebSocketJavaScriptObject(WebSocketWebView webSocketWebView, Draft draft) {
        init(webSocketWebView);
        mDraft = draft;

    }

    private void init(WebSocketWebView webSocketWebView) {
        mWebSocketWebView = webSocketWebView;
        mId = getRandomUniqueId();
    }

    @JavascriptInterface
    public String getId() {
        return mId;
    }

    public WebSocket getWebSocket(String url) {
        connectWebSocketClient(url);
        return mWebSocketClient.getConnection();
    }

    private void connectWebSocketClient(String url) {
        mWebSocketClient = new WebSocketClient(URI.create(url), mDraft) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                WebSocketJavaScriptObject.this.invokeJsOpen();
            }

            @Override
            public void onMessage(String message) {
                WebSocketJavaScriptObject.this.invokeJsMessage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                WebSocketJavaScriptObject.this.invokeJsClose();
            }

            @Override
            public void onError(Exception ex) {
                WebSocketJavaScriptObject.this.invokeJsError(ex);
            }
        };
        mWebSocketClient.connect();
    }

    @JavascriptInterface
    public void close() {
        mWebSocketWebView.post(new Runnable() {
            @Override
            public void run() {
                if (mWebSocketClient.isOpen()) {
                    mWebSocketClient.close();
                    invokeJsClose();
                    Log.d(TAG, "websocket closed");
                } else {
                    Log.d(TAG, "needn't close, webSocket is closed");
                }
            }
        });
    }

    @JavascriptInterface
    public void send(String message) {
        mWebSocketWebView.post(new Runnable() {
            @Override
            public void run() {
                if (mWebSocketClient.isOpen()) {
                    Log.d(TAG, "send message");
                    mWebSocketClient.send(message);
                } else{
                    Log.d(TAG, "send fail, webSocket is closed");
                }
            }
        });
    }

    public void invokeJsMessage(String msg) {
        final String data = msg;
        Log.v(TAG, "Received a message: " + msg);
        mWebSocketWebView.post(new Runnable() {
            public void run() {
                mWebSocketWebView.loadUrl(buildJavaScriptData(EVENT_ON_MESSAGE, data));
            }
        });
    }

    public void invokeJsOpen() {
        Log.v(TAG, "Connected!");
        mWebSocketWebView.post(new Runnable() {
            public void run() {
                mWebSocketWebView.loadUrl(buildJavaScriptData(EVENT_ON_OPEN, BLANK_MESSAGE));
            }
        });
    }

    public void invokeJsClose() {
        mWebSocketWebView.post(new Runnable() {
            public void run() {
                mWebSocketWebView.loadUrl(buildJavaScriptData(EVENT_ON_CLOSE, BLANK_MESSAGE));
            }
        });
    }

    public void invokeJsError(Throwable t) {
        final String msg = t.getMessage();
        Log.v(TAG, "Error: " + msg);
        t.printStackTrace();
        mWebSocketWebView.post(new Runnable() {
            public void run() {
                mWebSocketWebView.loadUrl(buildJavaScriptData(EVENT_ON_ERROR, msg));
            }
        });
    }

    /**
     * Builds text for javascript engine to invoke proper event method with
     * proper data.
     *
     * @param event websocket event (onOpen, onMessage etc.)
     * @param msg   Text message received from websocket server
     * @return
     */
    private String buildJavaScriptData(String event, String msg) {
        String b64EncodedMsg = "Error!";
        try {
            if (msg != null) {
                b64EncodedMsg = Base64.encodeBytes(msg.getBytes(UTF8_CHARSET));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String invokeJavaScriptData = "javascript:WebSocket." + event + "(" + "{" + "\"_target\":\"" + mId + "\","
                + "\"_data\":'" + msg + "'" + "}" + ")";
        Log.d(TAG, "invoke javaScript: " + invokeJavaScriptData);
        return invokeJavaScriptData;
    }

    private String getRandomUniqueId() {
        return "WEBSOCKET." + new Random().nextInt(100);
    }


}
