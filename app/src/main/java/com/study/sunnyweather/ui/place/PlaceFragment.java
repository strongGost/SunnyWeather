package com.study.sunnyweather.ui.place;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.study.sunnyweather.R;
import com.study.sunnyweather.util.LogUtil;

public class PlaceFragment extends Fragment {

    private ImageView bgImageView;
    private FrameLayout actionBarLayout;
    private EditText searchPlaceEdit;
    private RecyclerView recycleView;
    private PlaceAdapter adapter;
    private PlaceViewModel viewModel;

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

        // 获取 ViewModel 实例
        viewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

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
        viewModel.placeLiveData.observe(getViewLifecycleOwner(), placeList -> {
            if (placeList != null) {
                recycleView.setVisibility(View.VISIBLE);
                bgImageView.setVisibility(View.GONE);
                viewModel.placeList.clear();
                viewModel.placeList.addAll(placeList);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "未查询到指定地点", Toast.LENGTH_SHORT).show();
                LogUtil.getInstance().e("网络搜索失败：", "placeList == null: " + (placeList == null));
            }
        });
    }

}
