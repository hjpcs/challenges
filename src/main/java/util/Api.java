package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.gson.Gson;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import config.EnvConf;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.CoreConnectionPNames;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static io.restassured.config.HttpClientConfig.httpClientConfig;

public class Api {

    /**
     * 构造函数，信任任何https证书
     */
    public Api() {
        useRelaxedHTTPSValidation();
    }

    /**
     * 模式复用
     *
     * @return 返回一个默认的模式复用
     */
    public RequestSpecification getDefaultRequestSpecification() {
        return given().log().all()
                .config(RestAssuredConfig.config().httpClient(httpClientConfig()
                        .setParam(ClientPNames.CONN_MANAGER_TIMEOUT, Long.valueOf(5000))  // HttpConnectionManager connection return time
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000) // Remote host connection time
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 5000)  // Remote host response time
                ));
    }


    /**
     * 从json模板文件中读取数据，并更新部分数据
     *
     * @param path json文件路径
     * @param map  用于更新的数据
     * @return 对应接口post请求的body
     */
    public static String template(String path, HashMap<String, Object> map) {
        DocumentContext documentContext = JsonPath.parse(Api.class.getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString();
    }

    /**
     * 更新接口请求数据
     *
     * @param restful Restful类的实体
     * @param map     用于更新的数据
     * @return 更新过的Restful类的实体
     */
    public Restful updateApiFromMap(Restful restful, HashMap<String, Object> map) {
        // map为空直接返回restful
        if (map == null) {
            return restful;
        }
        // 如果接口是get请求，用map中的数据将restful中的get请求参数和值更新
        if (restful.method.toLowerCase().contains("get")) {
            map.entrySet().forEach(entry -> {
                restful.query.replace(entry.getKey(), entry.getValue().toString());
            });
        }
        // 如果接口是post请求
        if (restful.method.toLowerCase().contains("post")) {
            // 用map的值更新form中的数据
            if (restful.form.size() > 0) {
                map.entrySet().forEach(entry -> {
                    restful.form.replace(entry.getKey(), entry.getValue().toString());
                });
            }
            // 用map的值更新body中的数据，json串比较小时适用，比较大时还是读取json模板文件并更新其中某些字段方便
            if (restful.body != null) {
                restful.body = updateJsonBody(restful, map);
            }

            // 如果map中key包含body，用body的value更新restful的body
            // 并不推荐直接更新body，这样测试类中body的数据可读性会非常差
            if (map.containsKey("body")) {
                restful.body = map.get("body").toString();
            }
            // 如果map中key包含file，取出file(json文件)的路径，使用template方法读取出数据
            if (map.containsKey("file")) {
                String filePath = map.get("file").toString();
                map.remove("file");
                restful.body = template(filePath, map);
            }
            // 用map中的值更新file中的数据
            if (map.containsKey("media_file")) {
                String filePath = map.get("media_file").toString();
                try {
                    String resource = Objects.requireNonNull(Api.class.getClassLoader().getResource(filePath)).getFile();
                    map.replace("media_file", resource);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.entrySet().forEach(entry -> {
                    restful.file.replace(entry.getKey(), new File(entry.getValue().toString()));
                });
            }
        }
        // 如果接口是delete请求
        if (restful.method.toLowerCase().contains("delete")) {
            restful.body = updateJsonBody(restful, map);
        }

        // 如果接口是put请求
        if (restful.method.toLowerCase().contains("put")) {
            restful.body = updateJsonBody(restful, map);
        }
        return restful;
    }

    /**
     * 更新yaml文件中body内容
     *
     * @param restful Restful类的实体
     * @param map     用于更新的数据
     * @return 更新过后的body(json形式的字符串)
     */
    public String updateJsonBody(Restful restful, HashMap<String, Object> map) {
        // 将String类型的body转换为map
        Gson gson = new Gson();
        Map<String, Object> conversion = new HashMap<String, Object>();
        conversion = gson.fromJson(restful.body, conversion.getClass());
        Map<String, Object> finalConversion = conversion;
        // 用map中的值更新body转换后的map中的值，再将map转换回json串，再将json串转换为String类型并赋给body
        map.entrySet().forEach(entry -> {
            finalConversion.replace(entry.getKey(), entry.getValue().toString());
        });
        return new Gson().toJson(finalConversion);
    }

    /**
     * 读取yaml文件内容
     *
     * @param path yaml文件路径
     * @return 从yaml文件中读取数据后的Restful类实体
     */
    public Restful getApiFromYaml(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Restful restful = mapper.readValue(Api.class.getResourceAsStream(path), Restful.class);
            restful.url = EnvConf.getInstance().config.get(EnvConf.env).get(EnvConf.type).get("url") + restful.url;
            return restful;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 往requestSpecification中添加请求数据后，发送请求，得到响应内容
     *
     * @param path yaml文件路径
     * @param map  用于更新的数据
     * @return 请求返回的Response实体
     */
    public Response getResponseFromYaml(String path, HashMap<String, Object> map) {
        // 先从yaml文件中读取出数据更新restful
        Restful restful = getApiFromYaml(path);
        // 然后使用map中的数据继续更新restful中的请求体
        restful = updateApiFromMap(restful, map);
        // 获取默认的模式复用对象
        RequestSpecification requestSpecification = getDefaultRequestSpecification();

        // 如果query不为空，即get请求，将请求参数添加到requestSpecification.queryParam中
        if (restful.query.size() > 0) {
            restful.query.entrySet().forEach(entry -> {
                requestSpecification.queryParam(entry.getKey(), entry.getValue());
            });
        }
        // 如果form不为空，即不以body传参的post请求，将请求参数添加到requestSpecification.formParam中
        if (restful.form.size() > 0) {
            restful.form.entrySet().forEach(entry -> {
                requestSpecification.formParam(entry.getKey(), entry.getValue());
            });
        }
        // 如果body不为空，即以body传参的post请求，将请求体添加到requestSpecification中
        if (restful.body != null) {
            requestSpecification.body(restful.body);
        }

        if (restful.file.size() > 0) {
            restful.file.entrySet().forEach(entry -> {
                if (entry.getValue() != null) {
                    requestSpecification.multiPart(entry.getKey(), entry.getValue());
                }
            });
        }

        // 发送请求获得response
        return requestSpecification
                .when()
                .request(restful.method, restful.url)
                .then()
                .log().all().extract().response();
    }


}


















