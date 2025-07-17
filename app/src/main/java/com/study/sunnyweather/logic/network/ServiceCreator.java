package com.study.sunnyweather.logic.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 封装, 单例类 (饿汉式)
 */
public class ServiceCreator {

    // 私有构造函数，防止外部实例化
    private ServiceCreator() {
    }
    // 类 实例
    private static ServiceCreator serviceCreator = new ServiceCreator();
    private static final String BASE_URL = "https://api.caiyunapp.com/";
//    private static final Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
    // 创建日志拦截器
    private static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    //  新增：OkHttp 客户端
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // 添加日志拦截器
            .build();

    //  更新 Retrofit 初始化
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // 加入自定义 OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /**
     * 返回单例类实例
     */
    public static ServiceCreator getInstance() { return serviceCreator; }

    public static String getBaseUrl() { return BASE_URL; }

    public static Retrofit getRetrofit() { return retrofit; }

    /**
     * 返回 Service 动态代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T create(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
