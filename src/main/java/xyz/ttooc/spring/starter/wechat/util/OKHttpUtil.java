package xyz.ttooc.spring.starter.wechat.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class OKHttpUtil {

    public String postFormData(String url, String appId, String sign) throws IOException {
        log.info("http post form data , url : {} , appId : {} , sign : {}", url, appId, sign);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("appid", appId)
                .add("sign", sign)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        String responseStr = client.newCall(request).execute().body().string();

        log.info("http post form data response : {}", responseStr);
        return responseStr;
    }

    public String get(String url, Map<String, String> params) throws IOException {
        log.info("ok http client get by : {} , {}", url, params);
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String urlParams = buildUrlParams(params);
        Request request = new Request.Builder()
                .url(url + "?" + urlParams)
                .get()
                .build();
        String response = client.newCall(request).execute().body().string();
        return response;
    }

    private static String buildUrlParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (result.length() > 0)
                result.append("&");
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        return result.toString();
    }
}
