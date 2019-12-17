package com.example.weatherforecast.db;

import org.litepal.crud.LitePalSupport;

public class Weather extends LitePalSupport {

    private String city;//城市
    private String date;//日期
    private String high;//高温
    private String low;//低温
    private String fengxiang;//风向
    private String fengli;//风力
    private String type;//天气

    public Weather(String city, String date, String high, String low, String fengxiang, String fengli, String type) {
        this.city = city;
        this.date = date;
        this.high = high;
        this.low = low;
        this.fengxiang = fengxiang;
        this.fengli = fengli;
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
