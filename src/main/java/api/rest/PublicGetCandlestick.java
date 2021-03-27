package api.rest;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import util.Request;

import java.util.HashMap;

public class PublicGetCandlestick extends Request {

    @Step("get candlestick")
    public Response getCandlestick(String instrumentName, String timeFrame) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("instrument_name", instrumentName);
        map.put("timeframe", timeFrame);
        return getResponseFromYaml("/api/rest/PublicGetCandlestick", map);
    }
}
