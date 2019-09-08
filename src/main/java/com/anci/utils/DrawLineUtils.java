package com.anci.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 输入：起始时间和温度，稳定时间和温度，变化后稳定时间和温度
 * Created by Administrator on 2019/8/31.
 */
@Slf4j
public class DrawLineUtils {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static void main(String[] args) {
        System.out.println((double) getRandomNum(-10,10)/50);
    }

    /**
     * 画圆  圆心 x0,y0
     * @param beginPoint 起点
     * @param endPoint 终点
     * @return
     */
    public static List<Point> generateCircle(Point beginPoint, Point endPoint){
        List<Point> list = new ArrayList<>();
        list.add(beginPoint);

        double y0, y1, y2;
        int x0, x1, x2;
        x1 = beginPoint.getX();
        x2 = x0 = endPoint.getX();
        y1 = beginPoint.getTemperature();
        y2 = endPoint.getTemperature();

        Date date = DateUtils.str2Date(beginPoint.getTime(),sdf);
        y0 = getY0(x1, y1, x2, y2);
        double r = y2 - y0;

        int num = (x2-x1)/2 - 1;
        for (int i = 1; i <= num; i++) {
            double y;
            int x =x1 + 2*i;
            if (y2 > y1){
                y = getRegDouble(getIncreaseY(r,x,x0,y0)+(double) getRandomNum(0,10)/48,10000000000000L);
            }else {
                y = getRegDouble(getReduceY(r,x,x0,y0)+(double)getRandomNum(0,10)/48,10000000000000L);
            }
            long addTime = date.getTime() + 2*i*60*1000; //将xi分钟转为毫秒
            String time = sdf.format(addTime);
            Point point = new Point(time, y, x);
            list.add(point);
        }
        return list;
    }

    /**
     * 画直线
     * @param beginPoint  起点
     * @param endPoint 终点
     * @return
     */
    public static List<Point> generateLine(Point beginPoint, Point endPoint){
        List<Point> list = new ArrayList<>();
        list.add(beginPoint);

        double k = getCoefficient(beginPoint, endPoint);
        int x1 = beginPoint.getX();
        int x2 = endPoint.getX();
        double y = beginPoint.getTemperature();
        Date date = DateUtils.str2Date(beginPoint.getTime(),sdf);

        int num =(x2-x1)/2 -1;
        for (int i = 1; i <= num; i++) {
            double temp = getRegDouble(y + 2 * k * i+(double)getRandomNum(-10,10)/48,10000000000000L);
            long addTime = date.getTime() + 2*i*60*1000; //将xi分钟转为毫秒
            String time = sdf.format(addTime);
            Point point = new Point(time,temp,x1 + 2*i);
            list.add(point);
        }
        return  list;
    }

    private static double getCoefficient(Point beginPoint, Point endPoint){
        return (endPoint.getTemperature()- beginPoint.getTemperature())/(endPoint.getX() - beginPoint.getX());
    }



    // 获得一个给定范围的随机整数
    public static int getRandomNum(int smallistNum, int BiggestNum) {
        Random random = new Random();
        return (Math.abs(random.nextInt()) % (BiggestNum - smallistNum + 1))+ smallistNum;
    }

    private static double getY0(int x1, double y1, int x2, double y2) {
        return (Math.pow(y1,2) +  Math.pow(x2-x1,2) - Math.pow(y2,2))/(2*(y1 - y2));
    }

    private static double getIncreaseY(double r, int x, int x0, double y0) {
        return  y0 + Math.sqrt(Math.pow(r,2)-Math.pow(x-x0,2));
    }

    private static double getReduceY(double r, int x, int x0, double y0) {
        return  y0 - Math.sqrt(Math.pow(r,2)-Math.pow(x-x0,2));
    }

    private static double  getRegDouble(double d, long num){
        return  (double) Math.round(d * num) / num;
    }

}
