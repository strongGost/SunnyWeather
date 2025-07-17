package com.study.sunnyweather.ui.weather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.study.sunnyweather.R;
import com.study.sunnyweather.logic.model.DailyResponse;
import com.study.sunnyweather.logic.model.RealtimeResponse;
import com.study.sunnyweather.logic.model.Sky;
import com.study.sunnyweather.logic.model.Weather;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {
    private WeatherViewModel viewModel;
    private ScrollView weatherLayout;
    private RelativeLayout nowLayout;
    private FrameLayout titleLayout;
    private TextView placeName;
    private LinearLayout bodyLayout;
    private TextView currentTemp;
    private TextView currentSky;
    private TextView currentAQI;
    private LinearLayout forecastLayout;
    private ImageView coldRiskImg;
    private TextView coldRiskText;
    private ImageView dressingImg;
    private TextView dressingText;
    private ImageView ultravioletImg;
    private TextView ultravioletText;
    private ImageView carWashingImg;
    private TextView carWashingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather);


        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        Intent intent = getIntent();

        if (viewModel.locationLng.isEmpty())
            viewModel.locationLng = intent.getStringExtra("location_lng");
        if (viewModel.locationLat.isEmpty())
            viewModel.locationLat = intent.getStringExtra("location_lat");
        if (viewModel.placeName.isEmpty())
            viewModel.placeName = intent.getStringExtra("place_name");

        viewModel.weatherLiveData.observe(this, weather -> {
            if (weather != null)
                ShowWeatherInfo(weather);
            else {
                Toast.makeText(this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat);
        initView();
    }

    private void ShowWeatherInfo(Weather weather) {
        // 设置 信息
        placeName.setText(viewModel.placeName);
        // 获取 weather 的 未来天气、生活指数
        RealtimeResponse.RealTime realTime = weather.getRealTime();
        Log.e("网络实时sdf : ", realTime.toString());
        DailyResponse.Daily daily = weather.getDaily();

        /* 填充 now.xml 当前 天气信息*/
        currentTemp.setText((int) realTime.getTemperature().floatValue() + "℃");
        currentSky.setText(Sky.getSky(realTime.getSkycon()).getInfo());
        currentAQI.setText("空气指数" + (int) realTime.getAirQuality().getAqi().getChn().floatValue());
        nowLayout.setBackgroundResource(Sky.getSky(realTime.getSkycon()).getBg());

        /* 填充 forecast.xml 布局中的 数据 */
        forecastLayout.removeAllViews();    // 移除 所有子视图
        int days = daily.getSkycon().size();
        for (int i = 0; i < days; i++) {
            // 获取 子项控件 所需信息
            DailyResponse.Skycon skycon = daily.getSkycon().get(i);
            DailyResponse.Temperature temperature = daily.getTemperature().get(i);

            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            // 找 子项 控件
            TextView dateInfo = (TextView)  view.findViewById(R.id.dateInfo);
            ImageView skyIcon = (ImageView) view.findViewById(R.id.skyIcon);
            TextView skyInfo = (TextView) view.findViewById(R.id.skyInfo);
            TextView temperatureInfo = (TextView) view.findViewById(R.id.temperatureInfo);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            // 显示信息
            dateInfo.setText(simpleDateFormat.format(skycon.getDate()));
            Sky sky = Sky.getSky(skycon.getValue());
            skyIcon.setImageResource(sky.getIcon());
            skyInfo.setText(sky.getInfo());
            temperatureInfo.setText(String.format("%d ~ %d ℃", (int) temperature.getMin().floatValue(), (int) temperature.getMax().floatValue()));
            forecastLayout.addView(view);

            /* 填充 life_index.xml  生活指数 */
            DailyResponse.LifeIndex lifeIndex = daily.getLifeIndex();
            coldRiskText.setText(lifeIndex.getColdRisk().get(0).getDesc());
            dressingText.setText(lifeIndex.getDressing().get(0).getDesc());
            ultravioletText.setText(lifeIndex.getUltraviolet().get(0).getDesc());
            carWashingText.setText(lifeIndex.getCarWashing().get(0).getDesc());
            weatherLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        weatherLayout = (ScrollView) findViewById(R.id.weatherLayout);
        nowLayout = (RelativeLayout) findViewById(R.id.nowLayout);
        titleLayout = (FrameLayout) findViewById(R.id.titleLayout);
        placeName = (TextView) findViewById(R.id.placeName);
        bodyLayout = (LinearLayout) findViewById(R.id.bodyLayout);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        currentSky = (TextView) findViewById(R.id.currentSky);
        currentAQI = (TextView) findViewById(R.id.currentAQI);
        forecastLayout = (LinearLayout) findViewById(R.id.forecastLayout);
        coldRiskImg = (ImageView) findViewById(R.id.coldRiskImg);
        coldRiskText = (TextView) findViewById(R.id.coldRiskText);
        dressingImg = (ImageView) findViewById(R.id.dressingImg);
        dressingText = (TextView) findViewById(R.id.dressingText);
        ultravioletImg = (ImageView) findViewById(R.id.ultravioletImg);
        ultravioletText = (TextView) findViewById(R.id.ultravioletText);
        carWashingImg = (ImageView) findViewById(R.id.carWashingImg);
        carWashingText = (TextView) findViewById(R.id.carWashingText);
    }
}