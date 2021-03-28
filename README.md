# 项目运行
- 使用"mvn clean test -P${profile-id}"命令运行即可，${profile-id}对应pom.xml文件中的配置
- 如"mvn clean test -Plive-rest-v2"，"mvn clean test -Plive-wss-market"
- 测试执行完毕后，执行"allure generate target/allure-results -c"即可生成测试报告

# 项目结构
pom.xml - maven项目配置文件
testng-rest.xml - 组织rest接口用例文件
testng-wss.xml - 组织wss接口用例文件
src
├── main
│   ├── java
│   │   ├── api
│   │   │   ├── rest - rest接口调用类
│   │   │   │   └── PublicGetCandlestick.java
│   │   │   └── wss - wss接口调用类
│   │   │       └── BookInstrumentNameDepth.java
│   │   ├── config - 加载环境配置
│   │   │   └── EnvConf.java
│   │   └── util - 封装各接口协议调用方法
│   │       ├── Api.java
│   │       ├── Request.java
│   │       ├── Restful.java
│   │       └── WssClient.java
│   └── resources
│       ├── api
│       │   ├── rest - rest接口定义
│       │   │   └── PublicGetCandlestick
│       │   └── wss - wss接口定义
│       │       └── BookInstrumentNameDepth
│       └── config - 环境配置数据
│           ├── Env.properties
│           └── EnvConfig.yaml
└── test
    └── java
        ├── data - 测试数据
        │   └── DataSource.java
        ├── rest - rest接口测试类
        │   └── PublicGetCandlestickTest.java
        └── wss - wss接口测试类
            └── BookInstrumentNameDepthTest.java
