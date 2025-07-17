package com.study.sunnyweather.ui.place;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.sunnyweather.MainActivity;
import com.study.sunnyweather.R;
import com.study.sunnyweather.logic.model.Place;
import com.study.sunnyweather.ui.weather.WeatherActivity;
import com.study.sunnyweather.util.LogUtil;

public class PlaceFragment extends Fragment {

    private ImageView bgImageView;
    private FrameLayout actionBarLayout;
    private EditText searchPlaceEdit;
    private RecyclerView recycleView;
    private PlaceAdapter adapter;
    public PlaceViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bgImageView = (ImageView) view.findViewById(R.id.bgImageView);
        actionBarLayout = (FrameLayout) view.findViewById(R.id.actionBarLayout);
        searchPlaceEdit = (EditText) view.findViewById(R.id.searchPlaceEdit);
        recycleView = (RecyclerView) view.findViewById(R.id.recycleView);
        Log.d("fragment", "onViewCreated: ");

        // 获取 ViewModel 实例
        viewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        Log.d("fragment", ": " + viewModel);

        // 主界面、且上次城市记录保留
        if ((getActivity() instanceof MainActivity) && viewModel.isPlaceSaved()) {
            Place place = viewModel.getSavedPlace();
            Intent intent = new Intent(getContext(), WeatherActivity.class);
            intent.putExtra("location_lng", place.getLocation().getLng());
            intent.putExtra("location_lat", place.getLocation().getLat());
            intent.putExtra("place_name", place.getName());
            startActivity(intent);
            getActivity().finish();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()); // this 代表 Fragment
        recycleView.setLayoutManager(layoutManager);
        adapter = new PlaceAdapter(this, viewModel.placeList);
        recycleView.setAdapter(adapter);

        // 搜索框监听
        searchPlaceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.getInstance().d("调式: ", "Fragment  edittext");
                String content = s.toString();
                if (!content.isEmpty()) {
                    viewModel.searchPlaces(content);
                } else {
                    // 内容为空，显示背景图、隐藏 RecycleView
                    bgImageView.setVisibility(View.VISIBLE);
                    recycleView.setVisibility(View.GONE);
                    viewModel.placeList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 观察
        viewModel.placeLiveData.observe(getViewLifecycleOwner(), placeData -> {
            if (placeData != null) {
                // 获取的数据不为空，显示 RecycleView、隐藏背景图、更新缓存数据
                recycleView.setVisibility(View.VISIBLE);
                bgImageView.setVisibility(View.GONE);
                viewModel.placeList.clear();
                viewModel.placeList.addAll(placeData);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "未查询到指定地点", Toast.LENGTH_SHORT).show();
                LogUtil.getInstance().e("网络搜索失败：", "placeList == null: " + (placeData == null));
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("fragment", "onDestory: ");
    }
}
