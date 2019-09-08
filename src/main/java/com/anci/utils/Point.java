package com.anci.utils;

import lombok.Data;

/**
 * Created by Administrator on 2019/8/31.
 */
@Data
public class Point {
    private double temperature;
    private String time;
    private int x;
    private String shape;


    public Point(){}

    public Point(String time, double temperature){
        this.time = time;
        this.temperature = temperature;
    }

    public Point(String time, double temperature,int x){
        this.time = time;
        this.temperature = temperature;
        this.x = x;
    }
}
