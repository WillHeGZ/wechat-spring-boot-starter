package xyz.ttooc.spring.starter.wechat.service.vm;

import lombok.Data;

@Data
public class CodeToSessionResponse {
    private String openid;
    private String session_key;
    private String unionid;
}
