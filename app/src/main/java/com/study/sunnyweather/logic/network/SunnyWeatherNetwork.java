package com.study.sunnyweather.logic.network;

import com.study.sunnyweather.logic.model.DailyResponse;
import com.study.sunnyweather.logic.model.PlaceResponse;
import com.study.sunnyweather.logic.model.RealtimeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求 封装, 单例（饿汉式）
 */
public class SunnyWeatherNetwork {
    private SunnyWeatherNetwork() {}
    private static final SunnyWeatherNetwork network = new SunnyWeatherNetwork();
    public static SunnyWeatherNetwork getInstance() {
        return network;
    }

    // 动态代理对象 _地点
    private static final PlaceService placeService = ServiceCreator.create(PlaceService.class);

    /**
     * 搜索地点请求
     * @param query 查询地点
     * @param callback  回调函数
     */
    public void searchPlaces(String query, Callback<PlaceResponse> callback) {
        // enqueue: 异步执行, 请求完成后 触发 callback, 在仓库层处理 响应请求
        placeService.searchPlaces(query).enqueue(callback);
    }

    private static final WeatherServiece weatherService = ServiceCreator.create(WeatherServiece.class);

    /**
     * 实时天气
     * @param lng   经度
     * @param lat   纬度
     * @param callback
     */
    public void getRealtimeWeather(String lng, String lat, Callback<RealtimeResponse> callback) {
        weatherService.getRealtimeWeather(lng, lat).enqueue(callback);
    }
    public void getDailyWeather(String lng, String lat, Callback<DailyResponse> callback) {
        weatherService.getDailyWeather(lng, lat).enqueue(callback);
    }
}
