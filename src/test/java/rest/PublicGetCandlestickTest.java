package rest;

import api.rest.PublicGetCandlestick;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PublicGetCandlestickTest {

    PublicGetCandlestick publicGetCandlestick;

    @BeforeClass
    public void setUp() {
        if (publicGetCandlestick == null) {
            publicGetCandlestick = new PublicGetCandlestick();
        }
    }

    @AfterClass
    public void tearDown() {
    }

    @Test(description = "check candlestick right or not")
    public void testGetCandlestick() {
        publicGetCandlestick.getCandlestick("BTC_USDT", "5m")
                .then()
                .statusCode(200);
    }
}
