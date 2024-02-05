# 钉钉sdk版本

由于钉钉sdk的个性设计，有些接口提供新版sdk，有些接口提供旧版sdk，所以需要在项目中引入两个版本的sdk
## 新版sdk
```xml
        <dependency>
            <groupId>com.aliyun</groupId>
            <artifactId>dingtalk</artifactId>
            <version>2.0.15</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-api</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```

## 旧版sdk
```xml
        <dependency>
            <groupId>com.aliyun</groupId>
            <artifactId>alibaba-dingtalk-service-sdk</artifactId>
            <version>2.0.0</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-api</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```