package com.study.sunnyweather.ui.weather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.study.sunnyweather.R;
import com.study.sunnyweather.logic.model.DailyResponse;
import com.study.sunnyweather.logic.model.RealtimeResponse;
import com.study.sunnyweather.logic.model.Sky;
import com.study.sunnyweather.logic.model.Weather;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {
    public WeatherViewModel viewModel;
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
    private SwipeRefreshLayout swipeRefresh;
    private Button navBtn;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        ViewCompat.setOnApplyWindowInsetsListener(nowLayout, (v, insets) -> {
            // 处理系统栏边距
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* 背景图 与 状态栏 融合
        * View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN： 允许布局延伸到 状态栏区域
        * View.SYSTEM_UI_FLAG_LAYOUT_STABLE：保持布局稳定，就算状态栏、导航栏出现或消失，也别动我的布局尺寸和位置！
        * */
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        // 设置 状态栏为透明色
        getWindow().setStatusBarColor(Color.TRANSPARENT);

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
            swipeRefresh.setRefreshing(false);
        });

        // 设置刷新进度条样式、监听事件
        swipeRefresh.setColorSchemeResources(R.color.purple_500);
        refreshWeather();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWeather();
            }
        });
    }

    /* 刷新天气 */
    public void refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat);
        swipeRefresh.setRefreshing(true);
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
            TextView dateInfo = (TextView) view.findViewById(R.id.dateInfo);
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
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navBtn = (Button) findViewById(R.id.navBtn);

        // 打开抽屉
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        // 抽屉滑动事件
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                /* 菜单被隐藏时，也要隐藏输入法 */
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
}