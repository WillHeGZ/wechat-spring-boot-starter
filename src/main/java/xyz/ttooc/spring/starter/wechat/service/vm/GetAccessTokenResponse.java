package xyz.ttooc.spring.starter.wechat.service.vm;

import lombok.Data;

@Data
public class GetAccessTokenResponse {
    private String access_token;
    private Long expires_in;
}
