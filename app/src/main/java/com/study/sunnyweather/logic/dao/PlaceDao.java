package com.study.sunnyweather.logic.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.study.sunnyweather.SunnyWeatherApplication;
import com.study.sunnyweather.logic.model.Place;

/**
 * 记录上次选中的城市，单例类 （饿汉式）
 */
public class PlaceDao {
    private static PlaceDao instance = new PlaceDao();
    private PlaceDao() {}
    public static PlaceDao getInstance() {return instance;}

    /**
     * @return SharedPreferences 实例
     */
    private SharedPreferences sharedPreferences() {
         return SunnyWeatherApplication.getContext().getSharedPreferences("sunny_weather", Context.MODE_PRIVATE);
    }

    public void savePlace(Place place) {
        sharedPreferences().edit().putString("place", new Gson().toJson(place)).commit();
    }
    public Place getSavedPlace() {
        String placeJson = sharedPreferences().getString("place", "");
        return new Gson().fromJson(placeJson, Place.class);
    }

    public boolean isPlaceSaved() {
        return sharedPreferences().contains("place");
    }
}
