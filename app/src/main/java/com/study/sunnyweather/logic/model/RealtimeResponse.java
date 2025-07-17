package com.study.sunnyweather.logic.model;

import com.google.gson.annotations.SerializedName;

/**
 * 实时天气响应模型
 *  内部类：防止和其他接口的 数据模型类 有同名冲突情况
 *  注意： Gson 解析，嵌套类 需用 public static 修饰，否则为 null
 */
public class RealtimeResponse {
    private String status;
    private Result result;

    public RealtimeResponse(String status, Result result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        public RealTime getRealTime() {
            return realTime;
        }

        public void setRealTime(RealTime realTime) {
            this.realTime = realTime;
        }

        public Result(RealTime realTime) {
            this.realTime = realTime;
        }

        @SerializedName("realtime")
        RealTime realTime;
    }

    public static class RealTime {
        public RealTime(String skycon, Float temperature, AirQuality airQuality) {
            this.skycon = skycon;
            this.temperature = temperature;
            this.airQuality = airQuality;
        }

        public String getSkycon() {
            return skycon;
        }

        public void setSkycon(String skycon) {
            this.skycon = skycon;
        }

        public Float getTemperature() {
            return temperature;
        }

        public void setTemperature(Float temperature) {
            this.temperature = temperature;
        }

        public AirQuality getAirQuality() {
            return airQuality;
        }

        public void setAirQuality(AirQuality airQuality) {
            this.airQuality = airQuality;
        }

        String skycon;  // 天气现象
        Float temperature;  // 地表 2 米气温
        @SerializedName("air_quality") AirQuality airQuality;
    }

    public static class AirQuality {
        public AQI getAqi() {
            return aqi;
        }

        public void setAqi(AQI aqi) {
            this.aqi = aqi;
        }

        public AirQuality(AQI aqi) {
            this.aqi = aqi;
        }

        AQI aqi;
    }
    public static class AQI {
        Float chn;  // 国标 AQI

        public Float getChn() {
            return chn;
        }

        public void setChn(Float chn) {
            this.chn = chn;
        }

        public AQI(Float chn) {
            this.chn = chn;
        }
    }

}
