package cn.sric.util.down;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/11/11
 * @package cn.sric.util
 * @description
 **/
@Slf4j
public class NetWorkFile {

    /**
     * 网络文件  由URL转换为流的形式
     *
     * @param url
     * @return
     */
    public static InputStream sendGetForFile(String url) {
        InputStream inputStream;
        Request req = (new Request.Builder()).url(url).get().build();
        Response response;
        try {
            response = new OkHttpClient().newCall(req).execute();
            if (!response.isSuccessful()) {
                log.error("【调用HTTP请求异常】 code:{},message:{},url:{}", response.code(), response.message(), url);
                return null;
            }
            inputStream = Objects.requireNonNull(response.body()).byteStream();
            return inputStream;
        } catch (IOException e) {
            return null;
        }
    }
}
