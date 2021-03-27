package rest;

import api.rest.PublicGetCandlestick;
import data.DataSource;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.equalTo;

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

    @DataProvider
    public Object[][] dataProvider(Method method) {
        DataSource data = new DataSource();
        Object[][] obj = data.dataSource().get(method.getName());
        return obj;
    }

    @Test(dataProvider = "dataProvider", description = "check candlestick right or not")
    public void testGetCandlestick(String instrumentName, String timeFrame, int code, String method) {
        publicGetCandlestick.getCandlestick(instrumentName, timeFrame)
                .then()
                .statusCode(200)
                .body("code", equalTo(code))
                .body("method", equalTo(method))
                .body("result.instrument_name", equalTo(instrumentName))
                .body("result.interval", equalTo(timeFrame));
    }
}
