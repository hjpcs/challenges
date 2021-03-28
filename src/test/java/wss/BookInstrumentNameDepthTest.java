package wss;

import api.wss.BookInstrumentNameDepth;
import data.DataSource;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.URISyntaxException;


public class BookInstrumentNameDepthTest {

    BookInstrumentNameDepth bookInstrumentNameDepth;

    @BeforeClass
    public void setUp() {
        if (bookInstrumentNameDepth == null) {
            bookInstrumentNameDepth = new BookInstrumentNameDepth();
        }
    }

    @AfterClass
    public void tearDown() {
    }

    @DataProvider
    public Object[][] dataProvider(Method method) {
        DataSource data = new DataSource();
        return data.dataSource().get(method.getName());
    }

    @Test(dataProvider = "dataProvider", description = "check book info right or not")
    public void testGetInstrumentBookInfo(String id, String method, String channels, String nonce) throws URISyntaxException, InterruptedException {
        String msg = bookInstrumentNameDepth.getInstrumentBookInfo(id, method, channels, nonce);
        System.out.println(msg);
        // todo: 如何断言wss接口获得的结果
    }
}
