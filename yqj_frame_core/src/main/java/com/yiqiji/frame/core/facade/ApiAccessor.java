package com.yiqiji.frame.core.facade;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/***
 * http请求类
 *
 * @author aeeiko
 */
public class ApiAccessor {
    private static final int READ_TIMEOUT = 15;
    private static final int CONNECTION_TIMEOUT = 5;
    private static final int WRITE_TIMEOUT = 10;
    private static MediaType Str = MediaType
            .parse("application/x-www-form-urlencoded;charset=UTF-8");

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static String baseUrl = "";


    /***
     * 获取服务返回数据的方法（同步）
     *
     * @param action
     * @param map
     * @return
     * @throws Exception
     */
    public static String getHttpResponse(String action,
                                         HashMap<String, String> map) throws Exception {
        OkHttpClient client = createHttpClient();
        RequestBody requestBody = RequestBody.create(Str, FacadeUtil.getSignParamasString(map));
        Request.Builder okBuilder = new Request.Builder();
        okBuilder.url(Constants.BASE_URL + action);
        LogUtil.log_service(Constants.BASE_URL + action);
        okBuilder.post(requestBody);
        Request okRequest = okBuilder.build();
        Response response = client.newCall(okRequest).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return new String(response.body().bytes(), "utf-8");
    }


    /***
     * 获取服务返回数据的方法（同步）
     *
     * @param action
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public static String getHttpResponse(String action,
                                         JSONObject jsonObject) throws Exception {
        OkHttpClient client = createHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
        Request.Builder okBuilder = new Request.Builder();
        String url = Constants.BASE_URL + action +"?"+ FacadeUtil.getSignParamasString();
        LogUtil.log_service(url);
        okBuilder.url(url);
        okBuilder.post(requestBody);
        Request okRequest = okBuilder.build();
        Response response = client.newCall(okRequest).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return new String(response.body().bytes(), "utf-8");
    }

    /***
     * 获取服务返回数据的方法（同步）
     *
     * @param action
     * @param jsonArray
     * @return
     * @throws Exception
     */
    public static String getHttpResponse(String action,
                                         JSONArray jsonArray) throws Exception {
        OkHttpClient client = createHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, jsonArray.toString());
        Request.Builder okBuilder = new Request.Builder();
        String url = Constants.BASE_URL + action +"?"+ FacadeUtil.getSignParamasString();
        LogUtil.log_service(url);
        okBuilder.url(url);
        okBuilder.post(requestBody);
        Request okRequest = okBuilder.build();
        Response response = client.newCall(okRequest).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return new String(response.body().bytes(), "utf-8");
    }


    private static final String LOCK="LOCK";
    private static OkHttpClient mOkHttpClient = null;

    private static OkHttpClient createHttpClient() {
        if(mOkHttpClient==null){
            synchronized (LOCK){
                mOkHttpClient=new OkHttpClient();
                mOkHttpClient.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
                mOkHttpClient.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
                mOkHttpClient.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
            }
        }
        return mOkHttpClient;
    }
}
