package com.study.sunnyweather.logic.model;

/**
 * 天气 信息
 */
public class Weather {
    RealtimeResponse.RealTime realTime; // 今天 天气数据
    DailyResponse.Daily daily;  // 未来几天 天气数据、生活指数

    public Weather(RealtimeResponse.RealTime realTime, DailyResponse.Daily daily) {
        this.realTime = realTime;
        this.daily = daily;
    }

    public RealtimeResponse.RealTime getRealTime() {
        return realTime;
    }

    public void setRealTime(RealtimeResponse.RealTime realTime) {
        this.realTime = realTime;
    }

    public DailyResponse.Daily getDaily() {
        return daily;
    }

    public void setDaily(DailyResponse.Daily daily) {
        this.daily = daily;
    }
}
