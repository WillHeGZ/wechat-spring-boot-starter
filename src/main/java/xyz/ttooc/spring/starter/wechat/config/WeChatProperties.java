package xyz.ttooc.spring.starter.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.we-chat")
public class WeChatProperties {
    private final Applet applet = new Applet();

    @Data
    public static class Applet {
        private String baseUrl;
        private String appId;
        private String appSecretKey;
    }
}
