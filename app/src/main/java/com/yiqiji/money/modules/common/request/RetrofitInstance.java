package com.yiqiji.money.modules.common.request;

import com.yiqiji.frame.core.Constants;

import retrofit.Retrofit;

public class RetrofitInstance {
    private static Retrofit instance;
    private static Retrofit instanceRetrofit;

    private RetrofitInstance() {
    }

    public static Retrofit get() {
        if (instance == null) {
            instance = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                    // 增加返回值为String的支持
                    // .addConverterFactory(ScalarsConverterFactory.create())
                    // 增加返回值为Gson的支持(以实体类返回)
//					.addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ResponseConverterFactory.create())
                    // //增加返回值为Oservable<T>的支持
                    // .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return instance;
    }

    /**
     * get方法配置
     *
     * @return
     */
    public static Retrofit getRetrofitGetMeth() {
        instanceRetrofit = new Retrofit.Builder().addConverterFactory(ResponseConverterFactory.create())
                // //增加返回值为Oservable<T>的支持
                // .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return instanceRetrofit;
    }
}
