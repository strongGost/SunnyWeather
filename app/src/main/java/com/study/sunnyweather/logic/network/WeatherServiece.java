package com.study.sunnyweather.logic.network;

import com.study.sunnyweather.SunnyWeatherApplication;
import com.study.sunnyweather.logic.model.DailyResponse;
import com.study.sunnyweather.logic.model.RealtimeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit 接口文件
 *  实时天气、未来天气
 */
public interface WeatherServiece {
    @GET("v2.6/" + SunnyWeatherApplication.TOKEN +"/{lng},{lat}/realtime")
    Call<RealtimeResponse> getRealtimeWeather(@Path("lng") String lng, @Path("lat") String lat);

    @GET("v2.6/" + SunnyWeatherApplication.TOKEN +"/{lng},{lat}/daily")
    Call<DailyResponse> getDailyWeather(@Path("lng") String lng, @Path("lat") String lat);
}
