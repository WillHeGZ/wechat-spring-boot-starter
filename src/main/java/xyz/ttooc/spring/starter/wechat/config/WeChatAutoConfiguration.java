package xyz.ttooc.spring.starter.wechat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.ttooc.spring.starter.wechat.service.WeChatAppletService;

@Configuration
@ConditionalOnClass(WeChatProperties.class)
@EnableConfigurationProperties(WeChatProperties.class)
public class WeChatAutoConfiguration {
    private WeChatProperties weChatProperties;

    @Autowired
    public WeChatAutoConfiguration(WeChatProperties weChatProperties) {
        this.weChatProperties = weChatProperties;
    }

    @Bean
    public WeChatAppletService weChatAppletService() {
        WeChatAppletService weChatAppletService = new WeChatAppletService();
        weChatAppletService.setAppId(weChatProperties.getApplet().getAppId());
        weChatAppletService.setBaseUrl(weChatProperties.getApplet().getBaseUrl());
        weChatAppletService.setAppSecretKey(weChatProperties.getApplet().getAppSecretKey());
        return weChatAppletService;
    }
}
