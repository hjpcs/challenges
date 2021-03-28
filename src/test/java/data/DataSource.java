package data;

import java.util.HashMap;
import java.util.Map;

public class DataSource {
    /**
     * 根据测试方法的名称，来使用不同的数据源。
     *
     * @return 不同测试方法的数据源
     */
    public Map<String, Object[][]> dataSource() {
        Map<String, Object[][]> dataMap = new HashMap<>();

        Object[][] testGetCandlestick = new Object[][]{
                {"BTC_USDT", "1m", 0, "public/get-candlestick"},
                {"BTC_USDT", "5m", 0, "public/get-candlestick"},
                {"BTC_USDT", "15m", 0, "public/get-candlestick"},
                {"BTC_USDT", "30m", 0, "public/get-candlestick"},
                {"BTC_USDT", "1h", 0, "public/get-candlestick"},
                {"BTC_USDT", "4h", 0, "public/get-candlestick"},
                {"BTC_USDT", "6h", 0, "public/get-candlestick"},
                {"BTC_USDT", "12h", 0, "public/get-candlestick"},
                {"BTC_USDT", "1D", 0, "public/get-candlestick"},
                {"BTC_USDT", "7D", 0, "public/get-candlestick"},
                {"BTC_USDT", "14D", 0, "public/get-candlestick"},
                {"BTC_USDT", "1M", 0, "public/get-candlestick"}
        };
        dataMap.put("testGetCandlestick", testGetCandlestick);

        Object[][] testGetInstrumentBookInfo = new Object[][]{
                {"11", "subscribe", "book.ETH_CRO.10", "1587523073344"}
        };
        dataMap.put("testGetInstrumentBookInfo", testGetInstrumentBookInfo);
        return dataMap;
    }

}
