package com.example.myapplication.ui.weather;

public class WeatherDataModel {

    String condition,description,max_temp,min_temp,time;

    public WeatherDataModel() {
    }

    public WeatherDataModel(String condition, String description, String max_temp, String min_temp, String time) {
        this.condition = condition;
        this.description = description;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.time = time;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(String max_temp) {
        this.max_temp = max_temp;
    }

    public String getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(String min_temp) {
        this.min_temp = min_temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
