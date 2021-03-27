package util;

import java.io.File;
import java.util.HashMap;

public class Restful {
    public String url; //请求地址
    public String method; //请求方法
    public HashMap<String, String> headers; //请求头
    public HashMap<String, String> query = new HashMap<>(); //get请求参数
    public HashMap<String, String> form = new HashMap<>(); //key-value表单
    public String body; //json请求内容
    public HashMap<String, File> file = new HashMap<>(); //上传文件
}
