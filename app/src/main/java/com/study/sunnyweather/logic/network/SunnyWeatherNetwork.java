package com.study.sunnyweather.logic.network;

import com.study.sunnyweather.logic.model.PlaceResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求 封装, 单例（饿汉式）
 */
public class SunnyWeatherNetwork {
    private SunnyWeatherNetwork() {}
    private static final SunnyWeatherNetwork network = new SunnyWeatherNetwork();

    // 动态代理对象 _地点
    private static final PlaceService placeService = ServiceCreator.create(PlaceService.class);

    public static SunnyWeatherNetwork getInstance() {
        return network;
    }

    /**
     * 地点请求
     * @param query 查询地点
     * @param callback  回调函数
     */
    public void searchPlaces(String query, Callback<PlaceResponse> callback) {
        placeService.searchPlaces(query).enqueue(callback);
    }
}
