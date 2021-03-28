package api.wss;

import io.qameta.allure.Step;
import util.WssClient;

import java.net.URISyntaxException;
import java.util.HashMap;

public class BookInstrumentNameDepth {

    @Step("get instrument book info")
    public String getInstrumentBookInfo(String id, String method, String channels, String nonce) throws URISyntaxException, InterruptedException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("method", method);
        map.put("channels", channels);
        map.put("nonce", nonce);
        return WssClient.receiveMsg("/api/wss/BookInstrumentNameDepth", map);
    }

}
