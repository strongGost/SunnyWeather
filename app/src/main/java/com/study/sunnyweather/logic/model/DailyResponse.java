package com.study.sunnyweather.logic.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class DailyResponse {
    private String status;
    private Result result;

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

    public DailyResponse(String status, Result result) {
        this.status = status;
        this.result = result;
    }

    public static class Result {
        Daily daily;

        public Daily getDaily() {
            return daily;
        }

        public void setDaily(Daily daily) {
            this.daily = daily;
        }

        public Result(Daily daily) {
            this.daily = daily;
        }
    }
    public static class Daily {
        public List<Temperature> getTemperature() {
            return temperature;
        }

        public void setTemperature(List<Temperature> temperature) {
            this.temperature = temperature;
        }

        public List<Skycon> getSkycon() {
            return skycon;
        }

        public void setSkycon(List<Skycon> skycon) {
            this.skycon = skycon;
        }

        public LifeIndex getLifeIndex() {
            return lifeIndex;
        }

        public void setLifeIndex(LifeIndex lifeIndex) {
            this.lifeIndex = lifeIndex;
        }

        public Daily(List<Temperature> temperature, List<Skycon> skycon, LifeIndex lifeIndex) {
            this.temperature = temperature;
            this.skycon = skycon;
            this.lifeIndex = lifeIndex;
        }

        List<Temperature> temperature;
        List<Skycon> skycon;
        @SerializedName("life_index") LifeIndex lifeIndex;
    }
    public static class Temperature {
        public Float getMax() {
            return max;
        }

        public void setMax(Float max) {
            this.max = max;
        }

        public Float getMin() {
            return min;
        }

        public void setMin(Float min) {
            this.min = min;
        }

        public Temperature(Float max, Float min) {
            this.max = max;
            this.min = min;
        }

        Float max;
        Float min;
    }
    public static class Skycon {
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Skycon(String value, Date date) {
            this.value = value;
            this.date = date;
        }

        String value;   // 全天主要 天气现象
        Date date;  // 日期
    }
    public static class LifeIndex {
        public List<LifeDescription> getColdRisk() {
            return coldRisk;
        }

        public void setColdRisk(List<LifeDescription> coldRisk) {
            this.coldRisk = coldRisk;
        }

        public List<LifeDescription> getCarWashing() {
            return carWashing;
        }

        public void setCarWashing(List<LifeDescription> carWashing) {
            this.carWashing = carWashing;
        }

        public List<LifeDescription> getUltraviolet() {
            return ultraviolet;
        }

        public void setUltraviolet(List<LifeDescription> ultraviolet) {
            this.ultraviolet = ultraviolet;
        }

        public List<LifeDescription> getDressing() {
            return dressing;
        }

        public void setDressing(List<LifeDescription> dressing) {
            this.dressing = dressing;
        }

        public LifeIndex(List<LifeDescription> coldRisk, List<LifeDescription> carWashing, List<LifeDescription> ultraviolet, List<LifeDescription> dressing) {
            this.coldRisk = coldRisk;
            this.carWashing = carWashing;
            this.ultraviolet = ultraviolet;
            this.dressing = dressing;
        }

        List<LifeDescription> coldRisk; // 感冒指数
        List<LifeDescription> carWashing;   // 洗车指数
        List<LifeDescription> ultraviolet;  // 紫外线指数
        List<LifeDescription> dressing; // 穿衣指数
    }
    public static class LifeDescription {
        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public LifeDescription(String desc) {
            this.desc = desc;
        }

        String desc;    // 指数类型
    }
}
