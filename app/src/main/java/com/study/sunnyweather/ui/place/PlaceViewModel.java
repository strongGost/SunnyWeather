package com.study.sunnyweather.ui.place;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.study.sunnyweather.logic.Repository;
import com.study.sunnyweather.logic.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceViewModel extends ViewModel {
    private MutableLiveData<String> searchLiveData = new MutableLiveData<>();

    // 缓存城市数据
    public ArrayList<Place> placeList = new ArrayList<>();

    // 转换函数， 将仓库层返回的 LiveData 转为 可供 Activity 观察的 LiveData 对象
    public LiveData<List<Place>> placeLiveData = Transformations.switchMap(
            searchLiveData,
            query -> Repository.getInstance().searchPlaces(query)
    );

    /**
     * 当 searchPlaces 调用时， 就会执行转换函数
     * @param query
     */
    public void searchPlaces(String query) {
        searchLiveData.setValue(query);
    }
}
