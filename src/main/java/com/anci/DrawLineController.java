package com.anci;

import com.anci.utils.Constants;
import com.anci.utils.DateUtils;
import com.anci.utils.DrawLineUtils;
import com.anci.utils.ExcelUtils;
import com.anci.utils.Line;
import com.anci.utils.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/9/1.
 */
@RestController
@Slf4j
public class DrawLineController {
    /**
     *
     * @param lineList List<Line>
     * @return
     */
    @PostMapping(value = "/generateLine")
    public Map<String,List<Point>> generateLine(@RequestBody List<Line> lineList)   {
        log.info("****** lineList:" +lineList);
        Map<String,List<Point>> resultMap = new LinkedHashMap<>();
        List<String> temperatureList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("测试时间");
        lineList.forEach(line -> {
            boolean flag = true;
            String name = line.getName();
            nameList.add(name);
            List<Point> points = line.getPoints();
            Point begin = points.get(0);
            //初始化X轴
            begin.setX(0);
            points.forEach(point -> {
                Date beginTime = DateUtils.str2Date(begin.getTime(),DrawLineUtils.sdf);
                Date date = DateUtils.str2Date(point.getTime(),DrawLineUtils.sdf);
                point.setX(DateUtils.dateDiffForMinutes(beginTime, date));
            });
            //划线
            int size = points.size()-1;
            List<Point> pointList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if ("LINE".equals(points.get(i).getShape())){
                    pointList.addAll(DrawLineUtils.generateLine(points.get(i), points.get(i + 1)));
                } else if ("CIRCLE".equals(points.get(i).getShape())){
                    pointList.addAll(DrawLineUtils.generateCircle(points.get(i), points.get(i + 1)));
                }
            }
            pointList.add(points.get(size));
            //获取时间list，创建excel用
            if (timeList.size() == pointList.size()){
                flag = false;
            }
            for (Point point:pointList) {

                //获取温度list，创建excel用
                temperatureList.add(String.valueOf(point.getTemperature()));
                if (flag) {
                    timeList.add(point.getTime());
                }
            }
            resultMap.put(name,pointList);
        });
        createExcel(nameList,timeList,temperatureList);
        return resultMap;
    }

    public static void createExcel(List<String> nameList, List<String> timeList,List<String> temList) {
        List<Map<String,String>> dataList = new ArrayList<>();

        int pointNum = timeList.size();
        int temNum = temList.size();
        for (int i = 0; i < pointNum; i++) {
            Map<String, String> dataMap = new LinkedHashMap<>();
            dataMap.put(nameList.get(0),timeList.get(i));
            int titleIndex = 1;
            for (int j = i; j < temNum; j += pointNum) {
                dataMap.put(nameList.get(titleIndex),temList.get(j));
                titleIndex++;
            }
            dataList.add(dataMap);
        }
        String fileName = timeList.get(0).split(" ")[0] +"_ "+ nameList.get(1) +"-"+ nameList.get(nameList.size()-1) + "_export.xlsx";
        try {
            ExcelUtils.createExcel(getExcelPath() ,fileName,nameList,nameList,dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getExcelPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return "G:\\test\\";
        }else {
            return Constants.FILE_PATH;
        }
    }

}
