package com.study.sunnyweather.logic.model;

import com.study.sunnyweather.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 将 Api 返回的
 */
public class Sky {

    private String info;

    public Sky(String info, int icon, int bg) {
        this.info = info;   // 天气 文字
        this.icon = icon;   // 天气 图标
        this.bg = bg;       // 天气 背景
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    private int icon;
    private int bg;

    private static final Map<String, Sky> skyMap = new HashMap<>();

    static {
        skyMap.put("CLEAR_DAY", new Sky("晴", R.drawable.ic_clear_day, R.drawable.bg_clear_day));
        skyMap.put("CLEAR_NIGHT", new Sky("晴", R.drawable.ic_clear_night, R.drawable.bg_clear_night));
        skyMap.put("PARTLY_CLOUDY_DAY", new Sky("多云", R.drawable.ic_partly_cloud_day, R.drawable.bg_partly_cloudy_day));
        skyMap.put("PARTLY_CLOUDY_NIGHT", new Sky("多云", R.drawable.ic_partly_cloud_night, R.drawable.bg_partly_cloudy_night));
        skyMap.put("CLOUDY", new Sky("阴", R.drawable.ic_cloudy, R.drawable.bg_cloudy));
        skyMap.put("WIND", new Sky("大风", R.drawable.ic_cloudy, R.drawable.bg_wind));
        skyMap.put("LIGHT_RAIN", new Sky("小雨", R.drawable.ic_light_rain, R.drawable.bg_rain));
        skyMap.put("MODERATE_RAIN", new Sky("中雨", R.drawable.ic_moderate_rain, R.drawable.bg_rain));
        skyMap.put("HEAVY_RAIN", new Sky("大雨", R.drawable.ic_heavy_rain, R.drawable.bg_rain));
        skyMap.put("STORM_RAIN", new Sky("暴雨", R.drawable.ic_storm_rain, R.drawable.bg_rain));
        skyMap.put("THUNDER_SHOWER", new Sky("雷阵雨", R.drawable.ic_thunder_shower, R.drawable.bg_rain));
        skyMap.put("SLEET", new Sky("雨夹雪", R.drawable.ic_sleet, R.drawable.bg_rain));
        skyMap.put("LIGHT_SNOW", new Sky("小雪", R.drawable.ic_light_snow, R.drawable.bg_snow));
        skyMap.put("MODERATE_SNOW", new Sky("中雪", R.drawable.ic_moderate_snow, R.drawable.bg_snow));
        skyMap.put("HEAVY_SNOW", new Sky("大雪", R.drawable.ic_heavy_snow, R.drawable.bg_snow));
        skyMap.put("STORM_SNOW", new Sky("暴雪", R.drawable.ic_heavy_snow, R.drawable.bg_snow));
        skyMap.put("HAIL", new Sky("冰雹", R.drawable.ic_hail, R.drawable.bg_snow));
        skyMap.put("LIGHT_HAZE", new Sky("轻度雾霾", R.drawable.ic_light_haze, R.drawable.bg_fog));
        skyMap.put("MODERATE_HAZE", new Sky("中度雾霾", R.drawable.ic_moderate_haze, R.drawable.bg_fog));
        skyMap.put("HEAVY_HAZE", new Sky("重度雾霾", R.drawable.ic_heavy_haze, R.drawable.bg_fog));
        skyMap.put("FOG", new Sky("雾", R.drawable.ic_fog, R.drawable.bg_fog));
        skyMap.put("DUST", new Sky("浮尘", R.drawable.ic_fog, R.drawable.bg_fog));
    }

    public static Sky getSky(String skycon) {
        Sky result = skyMap.get(skycon);
        if (result != null) {
            return result;
        } else {
            return skyMap.get("CLEAR_DAY"); // 默认值
        }
    }
}
