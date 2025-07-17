package com.study.sunnyweather.ui.place;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.study.sunnyweather.logic.Repository;
import com.study.sunnyweather.logic.model.Place;
import com.study.sunnyweather.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class PlaceViewModel extends ViewModel {
    // 要搜索的城市名称
    private MutableLiveData<String> searchLiveData = new MutableLiveData<>();

    // 缓存当前界面上的城市数据， 避免 屏幕旋转时丢失
    public ArrayList<Place> placeList = new ArrayList<>();

    /** 返回获取到的城市数据，
     * 观察 searchLiveData 的值, 发生变化后 调用仓库层 searchPlaces() 获取数据,
     * 然后 将仓库层返回的 LiveData 转为 可供 Activity 观察的 LiveData 对象
     * */
    public LiveData<List<Place>> placeLiveData = Transformations.switchMap(
            searchLiveData,
            query -> Repository.getInstance().searchPlaces(query)
    );

    /**
     * 存储 content
     * @param query 要搜索的城市
     */
    public void searchPlaces(String query) {
        /* if — 解决了  和  屏幕旋转 导致 Fragment 的 onViewCreated 重新创建， 触发 searchPlaces的 问题 */
        if (!query.equals(searchLiveData.getValue()))
            searchLiveData.setValue(query);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        LogUtil.getInstance().d("ViewModel被销毁: ", "onCleared");
    }

    public void savePlace(Place place) {
        Repository.getInstance().savePlace(place);
    }
    public Place getSavedPlace() {
        return Repository.getInstance().getSavedPlace();
    }
    public boolean isPlaceSaved() {
        return Repository.getInstance().isPlaceSaved();
    }
}
