package com.study.sunnyweather.ui.place;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.study.sunnyweather.R;
import com.study.sunnyweather.logic.model.Place;
import com.study.sunnyweather.logic.model.Weather;
import com.study.sunnyweather.ui.weather.WeatherActivity;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private List<Place> placeList;
    private PlaceFragment fragment;
    public PlaceAdapter(PlaceFragment fragment, List<Place> placeList) {
        this.fragment = fragment;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getBindingAdapterPosition();
                Place place = placeList.get(position);

                Activity activity = fragment.getActivity();
                if (activity instanceof WeatherActivity) {
                    /* 在天气界面，更新 所选城市天气信息 */
                    ((WeatherActivity) activity).drawerLayout.closeDrawers();
                    ((WeatherActivity) activity).viewModel.locationLat = place.getLocation().getLat();
                    ((WeatherActivity) activity).viewModel.locationLng = place.getLocation().getLng();
                    ((WeatherActivity) activity).viewModel.placeName = place.getName();
                    ((WeatherActivity) activity).refreshWeather();
                } else {
                    /* 跳转到天气界面 */
                    Intent intent = new Intent(parent.getContext(), WeatherActivity.class);
                    intent.putExtra("location_lng", place.getLocation().getLng());
                    intent.putExtra("location_lat", place.getLocation().getLat());
                    intent.putExtra("place_name", place.getName());
                    fragment.startActivity(intent);
                    if (activity != null) activity.finish();
                    Log.d("测试", "Activity: "+activity);
                }
                // 保存选中的城市
                fragment.viewModel.savePlace(place);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.placeName.setText(place.getName());
        holder.placeAddress.setText(place.getAddress());
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView placeName;
        private TextView placeAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeAddress = (TextView) itemView.findViewById(R.id.placeAddress);
        }
    }
}
