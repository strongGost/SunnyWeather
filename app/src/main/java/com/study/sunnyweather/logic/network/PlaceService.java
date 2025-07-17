package com.study.sunnyweather.logic.network;

import com.study.sunnyweather.SunnyWeatherApplication;
import com.study.sunnyweather.logic.model.PlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit 接口文件
 *  搜索城市
 */
    public interface PlaceService {
        @GET("v2/place?&token=" + SunnyWeatherApplication.TOKEN + "&lang=zh_CN")
        Call<PlaceResponse>  searchPlaces(@Query("query") String query);
    }
