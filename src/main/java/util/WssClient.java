package util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.gson.Gson;
import config.EnvConf;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


public class WssClient extends WebSocketClient {

    public static LinkedList<String> msgs = new LinkedList<>();

    public WssClient(URI serverURI) {
        super(serverURI);
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("opened connection");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String message) {
        saveMsg(message);
        System.out.println("received: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println(
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void saveMsg(String msg) {
        msgs.add(msg);
    }

    public static String receiveMsg(String path, HashMap<String, Object> map) throws InterruptedException, URISyntaxException {

        String uri = EnvConf.getInstance().config.get(EnvConf.env).get(EnvConf.type).get("url");
        WssClient c = new WssClient(new URI(uri));
        c.connectBlocking();

        String sendMsg = getSendMsg(path);
        sendMsg = updateSendMsg(sendMsg, map);
        c.send(sendMsg);

        Thread.sleep(1000);
        c.close();

        Gson gson = new Gson();
        return gson.toJson(msgs);
    }

    public static String getSendMsg(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Restful restful = mapper.readValue(WssClient.class.getResourceAsStream(path), Restful.class);
            return restful.body;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String updateSendMsg(String sendMsg, HashMap<String, Object> map) {
        // 将String类型的snedMsg转换为map
        Gson gson = new Gson();
        Map<String, Object> conversion = new HashMap<>();
        conversion = gson.fromJson(sendMsg, conversion.getClass());
        Map<String, Object> finalConversion = conversion;
        // 用map中的值更新body转换后的map中的值，再将map转换回json串，再将json串转换为String类型并赋给body
        map.entrySet().forEach(entry -> {
            finalConversion.replace(entry.getKey(), entry.getValue().toString());
        });

        return new Gson().toJson(finalConversion);
    }

}