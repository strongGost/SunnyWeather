package com.study.sunnyweather.logic;

import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.study.sunnyweather.logic.dao.PlaceDao;
import com.study.sunnyweather.logic.model.DailyResponse;
import com.study.sunnyweather.logic.model.Place;
import com.study.sunnyweather.logic.model.PlaceResponse;
import com.study.sunnyweather.logic.model.RealtimeResponse;
import com.study.sunnyweather.logic.model.Weather;
import com.study.sunnyweather.logic.network.SunnyWeatherNetwork;
import com.study.sunnyweather.util.LogUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 仓库层， 单例类
 */
public class Repository {
    private Repository() {}
    private static final Repository repository = new Repository();
    public static Repository getInstance() { return repository; }

    /**
     * 暴露不可变 LiveData 给外部, 所以 MutableLiveData 向上转型 返回 LiveData.
     * @param query
     * @return
     */
    public LiveData<List<Place>> searchPlaces(String query) {
        // 存储 响应中的数据
        MutableLiveData<List<Place>> liveData = new MutableLiveData<>();

        // 发起网络请求
        SunnyWeatherNetwork.getInstance().searchPlaces(query, new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                PlaceResponse body = response.body();
                if (body != null && "ok".equals(body.getStatus())) {
                    liveData.setValue(body.getPlaces());
                } else
                    liveData.setValue(null);
                LogUtil.getInstance().d("网络请求地点：", "body == null: " + (body == null));
                LogUtil.getInstance().d("网络请求回调函数：", "主线程中？: " + (Looper.myLooper() == Looper.getMainLooper()));
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    /**
     *  获取天气数据
     *      两个请求都返回响应后，处理返回数据
     *      CompletableFuture: java8.+ 用于异步编程
     * @param lng
     * @param lat
     * @return
     */
/*    public LiveData<Weather> refreshWeather(String lng, String lat) {
        // 相应数据
        MutableLiveData<Weather> weatherData = new MutableLiveData<>();

        SunnyWeatherNetwork.getInstance().getRealtimeWeather(lng, lat, new Callback<RealtimeResponse>() {
            @Override
            public void onResponse(Call<RealtimeResponse> call, Response<RealtimeResponse> response) {

            }

            @Override
            public void onFailure(Call<RealtimeResponse> call, Throwable t) {

            }
        });
        SunnyWeatherNetwork.getInstance().getDailyWeather(lng, lat, new Callback<DailyResponse>() {
            @Override
            public void onResponse(Call<DailyResponse> call, Response<DailyResponse> response) {

            }

            @Override
            public void onFailure(Call<DailyResponse> call, Throwable t) {

            }
        });
    }*/

    public LiveData<Weather> refreshWeather(String lng, String lat) {
        MutableLiveData<Weather> weatherData = new MutableLiveData<>();

        final RealtimeResponse[] realtimeHolder = new RealtimeResponse[1];
        final DailyResponse[] dailyHolder = new DailyResponse[1];
        // 记录 请求 是否响应成功
        final AtomicBoolean realtimeReady = new AtomicBoolean(false);
        final AtomicBoolean dailyReady = new AtomicBoolean(false);

        SunnyWeatherNetwork.getInstance().getRealtimeWeather(lng, lat, new Callback<RealtimeResponse>() {
            @Override
            public void onResponse(Call<RealtimeResponse> call, Response<RealtimeResponse> response) {
                realtimeHolder[0] = response.body();
                Log.e("网络实时: ", new Gson().toJson(response.body()));
                realtimeReady.set(true);
                maybePostCombinedResult();
            }

            @Override
            public void onFailure(Call<RealtimeResponse> call, Throwable t) {
                // 你可以根据需求设置错误处理
                t.printStackTrace();
            }

            // 检查是否两个都响应成功
            private void maybePostCombinedResult() {
                if (dailyReady.get() && realtimeHolder[0] != null && dailyHolder[0] != null) {
                    weatherData.postValue(new Weather(realtimeHolder[0].getResult().getRealTime(), dailyHolder[0].getResult().getDaily()));
                }
            }
        });

        SunnyWeatherNetwork.getInstance().getDailyWeather(lng, lat, new Callback<DailyResponse>() {
            @Override
            public void onResponse(Call<DailyResponse> call, Response<DailyResponse> response) {
                dailyHolder[0] = response.body();
                dailyReady.set(true);
                maybePostCombinedResult();
                Log.e("网络实时2: ", new Gson().toJson(response.body()));

            }

            @Override
            public void onFailure(Call<DailyResponse> call, Throwable t) {
                // 同样可加入错误处理
                t.printStackTrace();
            }

            // 检查是否两个都响应成功
            private void maybePostCombinedResult() {
                if (realtimeReady.get() && realtimeHolder[0] != null && dailyHolder[0] != null) {
                    weatherData.postValue(new Weather(realtimeHolder[0].getResult().getRealTime(), dailyHolder[0].getResult().getDaily()));
                }
            }
        });

        return weatherData;
    }


    public void savePlace(Place place) {
        PlaceDao.getInstance().savePlace(place);
    }
    public Place getSavedPlace() {
        return PlaceDao.getInstance().getSavedPlace();
    }
    public boolean isPlaceSaved() {
        return PlaceDao.getInstance().isPlaceSaved();
    }

}
