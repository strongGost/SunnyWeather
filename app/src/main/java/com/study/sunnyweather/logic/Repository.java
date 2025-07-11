package com.study.sunnyweather.logic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.study.sunnyweather.logic.model.Place;
import com.study.sunnyweather.logic.model.PlaceResponse;
import com.study.sunnyweather.logic.network.SunnyWeatherNetwork;
import com.study.sunnyweather.util.LogUtil;

import java.util.List;

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
     * 返回 LiveData 对象
     * @param query
     * @return
     */
    public LiveData<List<Place>> searchPlaces(String query) {
        MutableLiveData<List<Place>> liveData = new MutableLiveData<>();

        SunnyWeatherNetwork.getInstance().searchPlaces(query, new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                PlaceResponse body = response.body();
                if (body != null && body.getStatus().equals("ok")) {
                    liveData.postValue(body.getPlaces());
                } else liveData.postValue(null);
                LogUtil.getInstance().d("网络请求 SearchPlaces状态码错误：", "body == null: " + (body == null));
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                LogUtil.getInstance().e("网络请求 SearchPlaces失败", t.toString());
                liveData.postValue(null);
            }
        });
        return liveData;
    }
}
