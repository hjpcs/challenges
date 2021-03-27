package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class EnvConf {

    public HashMap<String, HashMap<String, HashMap<String, String>>> config;

    private static EnvConf envConf;

    public static String env; // 环境变量
    public static String type; // 类型变量

    public EnvConf() {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = this.getClass().getResourceAsStream("/config/Env.properties");
            properties.load(in);
            env = (String) properties.getProperty("env");
            type = (String) properties.getProperty("type");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从yaml文件中读取数据
     *
     * @param path 文件路径
     * @return 一个EnvConf对象
     */
    public static EnvConf load(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(EnvConf.class.getResourceAsStream(path), EnvConf.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 单例模式
     *
     * @return 一个EnvConf对象
     */
    public static EnvConf getInstance() {
        if (envConf == null) {
            envConf = load("/config/EnvConfig.yaml");
        }
        return envConf;
    }

}
