package xyz.ttooc.spring.starter.wechat.service.vm;

import lombok.Data;

@Data
public class ResponseBase {
    private Integer errcode;
    private String errmsg;
}
