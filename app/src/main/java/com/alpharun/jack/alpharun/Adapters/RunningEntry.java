package com.alpharun.jack.alpharun.Adapters;

/**
 * Created by Jack on 16/03/2017.
 */

public class RunningEntry {
    private double distance;
    private int time;
    private int id;
    private String weatherCondition;
    private double pace;
    private int temp;

    public RunningEntry(double distance, int time, int Id, String weather, double pace, int temp){
        this.distance = distance;
        this.time = time;
        this.id = Id;
        this.weatherCondition = weather;
        this.pace = pace;
        this.temp = temp;
    }

    public double getDistance(){
        return this.distance;
    }

    public String getRunTime(){
        return Integer.toString(this.time);
    }

    public int getRunId(){
        return this.id;
    }

    public String getWeatherCondition(){
        return this.weatherCondition;
    }

    public double getRunPace(){
        return this.pace;
    }

    public int getTemp(){
        return this.temp;
    }


}
