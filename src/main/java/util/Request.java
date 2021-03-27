package util;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Request extends Api {

    /**
     * 重写getDefaultRequestSpecification方法
     *
     * @return 模式复用
     */
    @Override
    public RequestSpecification getDefaultRequestSpecification() {
        RequestSpecification requestSpecification = super.getDefaultRequestSpecification();
        requestSpecification.contentType(ContentType.URLENC);
        return requestSpecification;
    }
}
