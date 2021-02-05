package cn.sric.util;

import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author sric
 */
public class OkHttp {
    static OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS).build();

    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String get(String url, String cookie) {
        if (url.contains("?")) {
            url = url + "&cookie=" + cookie;
        } else {
            url = url + "?cookie=" + cookie;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String post(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    public static String post(String url, Map<String, Object> map) {
        List<String> name = new ArrayList<>();
        List<String> value = new ArrayList<>();
        map.keySet().forEach(key -> {
            name.add(key);
            value.add(map.get(key).toString());
        });
        FormBody formBody = new FormBody(name, value);
        String response = "";
        try {
            response = post(url, formBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

//    /**
//     * @param url  请求地址
//     * @param json 上报内容
//     * @return 返回值
//     */
//    public String okHttpPostMethod(String url, String json) {
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(JSON, json);
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String get(String url, JSONObject jsonObject) {
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        //1 . 拿到OkHttpClient对象
//        OkHttpClient client = new OkHttpClient();
//        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
//        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
//        //3 . 构建Request,将FormBody作为Post方法的参数传入
//        Request request = new Request.Builder()
//                .url(url).post(requestBody)
//                .build();
//
//        try {
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}