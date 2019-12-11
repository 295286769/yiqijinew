package com.yiqiji.money.modules.common.request;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit.Converter;

public class MyConverterFactory<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    MyConverterFactory(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            new JsonParser().parse(response);//效验json
            // ResultResponse 只解析result字段
            ResultResponse resultResponse = gson.fromJson(response, ResultResponse.class);
            if (resultResponse.getCode() == 0 || resultResponse.getCode() == 50033 || resultResponse.getCode() == 20010 || resultResponse.getCode() == 50006 || resultResponse.getCode() == 20012) {
                // result==0表示成功返回，继续用本来的Model类解析
                return gson.fromJson(response, type);
            } else {
//                if(resultResponse.getCode()>20013){
//                    ToastUtils.DiyToast();
//                }
                // ErrResponse 将msg解析为异常消息文本
                ErrResponse errResponse = gson.fromJson(response, ErrResponse.class);
                throw new ResultException(resultResponse.getCode(), errResponse.getMsg());
            }
        } catch (JsonParseException e) {
            ResultResponse resultResponse = new ResultResponse();
            resultResponse.setCode(10000);
            ErrResponse errResponse = new ErrResponse();
            errResponse.setMsg("请求出错");
            throw new ResultException(resultResponse.getCode(), errResponse.getMsg());
        } finally {
        }
    }


}
