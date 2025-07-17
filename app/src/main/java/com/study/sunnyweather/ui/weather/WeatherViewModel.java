package com.study.sunnyweather.ui.weather;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.study.sunnyweather.logic.Repository;
import com.study.sunnyweather.logic.model.Location;
import com.study.sunnyweather.logic.model.Weather;

public class WeatherViewModel extends ViewModel {
    // 地点
    private MutableLiveData<Location> locationLiveData = new MutableLiveData<>();

    // 防止手机屏幕旋转丢失数据
    public String locationLng = "";
    public String locationLat = "";
    public String placeName = "";

    public LiveData<Weather> weatherLiveData = Transformations.switchMap(
            locationLiveData,
            location -> Repository.getInstance().refreshWeather(locationLng, locationLat)
    );

    public void refreshWeather(String lng, String lat) {
        locationLiveData.setValue(new Location(lng, lat));
    }


}
