package xyz.ttooc.spring.starter.wechat.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class WeChatAppletService {
    private String baseUrl;
    private String appId;
    private String appSecretKey;
}
