package xyz.ttooc.spring.starter.wechat.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import xyz.ttooc.spring.starter.wechat.service.vm.CodeToSessionResponse;
import xyz.ttooc.spring.starter.wechat.service.vm.GetAccessTokenResponse;
import xyz.ttooc.spring.starter.wechat.service.vm.ResponseBase;
import xyz.ttooc.spring.starter.wechat.util.AES;
import xyz.ttooc.spring.starter.wechat.util.OKHttpUtil;
import xyz.ttooc.spring.starter.wechat.util.WxPKCS7Encoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class WeChatAppletService {
    private String baseUrl;
    private String appId;
    private String appSecretKey;
    private final static Integer WE_CHAT_SUCCESS_RETURN_CODE = 0;

    private final OKHttpUtil okHttpUtil;
    private final ObjectMapper objectMapper;

    public WeChatAppletService() {
        this.okHttpUtil = new OKHttpUtil();
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public CodeToSessionResponse code2Session(String wxCode) throws IOException {
        log.info("code to session request : {}", wxCode);
        String url = baseUrl.concat("/sns/jscode2session");
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("appid", appId);
        paramsMap.put("secret", appSecretKey);
        paramsMap.put("js_code", wxCode);
        paramsMap.put("grant_type", "authorization_code");

        String response = okHttpUtil.get(url, paramsMap);
        ResponseBase responseBase = objectMapper.readValue(response, ResponseBase.class);
        if (responseBase.getErrcode() == WE_CHAT_SUCCESS_RETURN_CODE) {
            return objectMapper.readValue(response, CodeToSessionResponse.class);
        } else {
            throw new RuntimeException(responseBase.getErrmsg());
        }
    }

    private GetAccessTokenResponse getAccessToken() throws IOException {
        log.info("get access token");
        String url = baseUrl.concat("/cgi-bin/token");
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("grant_type", "client_credential");
        paramsMap.put("appid", appId);
        paramsMap.put("secret", appSecretKey);
        String response = okHttpUtil.get(url, paramsMap);

        ResponseBase responseBase = objectMapper.readValue(response, ResponseBase.class);
        if (responseBase.getErrcode() == WE_CHAT_SUCCESS_RETURN_CODE) {
            return objectMapper.readValue(response, GetAccessTokenResponse.class);
        } else {
            throw new RuntimeException(responseBase.getErrmsg());
        }
    }

    public String decrypt(String encryptedData, String sessionKey, String iv) {
        try {
            log.debug("decrypt by : {},{},{}", encryptedData, sessionKey, iv);
            String result = "";

            AES aes = new AES();
            byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
            if (null != resultByte && resultByte.length > 0) {
                result = new String(WxPKCS7Encoder.decode(resultByte));
                JsonNode jsonNode = objectMapper.readTree(result);
                if (appId.equalsIgnoreCase(jsonNode.get("watermark").get("appid").asText())) {
                    return result;
                } else {
                    throw new RuntimeException("解密数据app id不匹配");
                }
            } else {
                throw new RuntimeException("decrypt failure");
            }
        } catch (Throwable t) {
            log.error("throw error", t);
            throw new RuntimeException(t.getMessage());
        }
    }
}
