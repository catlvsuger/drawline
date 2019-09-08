package com.anci.utils;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/9/1.
 */
    public class ExcelUtils {

    /**
     * 生成Excel 并放到指定位置
     *@param filepath 文件路径(要绝对路径)
     *@param filename 文件名称 (如: demo.xls  记得加.xls哦)
     *@param titlelist 标题名称list
     *@param zdlist 字段list
     *@param datalist 数据list (这里也可以改成List<Map<String,String>>  格式的数据)
     *@return 是否正常生成
     *@throws IOException
     *@author:
     *2018年11月24日 上午11:40:39
     * (titlelist  和  zdlist  顺序要一直, 要一一对应)
     */
    public static boolean createExcel(String filepath, String filename, List<String> titlelist, List<String> zdlist, List<Map<String,String>> datalist) throws IOException {
        boolean success = false;

            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            // 建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("sheet1");
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row0 = sheet.createRow(0);
            HSSFCellStyle cellStyle = wb.createCellStyle();
            //设置水平居中
            //cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //设置垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//            //设置下边框
//            cellStyle.setBorderBottom(BorderStyle.THIN);
//            //设置上边框
//            cellStyle.setBorderTop(BorderStyle.THIN);
//            //设置走边框
//            cellStyle.setBorderLeft(BorderStyle.THIN);
//            //设置右边框
//            cellStyle.setBorderRight(BorderStyle.THIN);
            //设置字体
            HSSFFont font = wb.createFont();
            font.setFontName("宋体");//设置字体名称
            font.setFontHeightInPoints((short)12);//设置字号
            font.setItalic(false);//设置是否为斜体
            font.setBold(false);//设置是否加粗
            //font.setColor(IndexedColors.RED.index);//设置字体颜
            cellStyle.setFont(font);
            HSSFDataFormat df = wb.createDataFormat();
            // 添加表头
            for(int i = 0;i<titlelist.size();i++){
                HSSFCell cell = row0.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(titlelist.get(i));
            }
            //添加表中内容
            for(int row = 0;row<datalist.size();row++){//数据行
                //创建新行
                HSSFRow newrow = sheet.createRow(row+1);//数据从第二行开始
                //获取该行的数据
                Map<String,String> data = datalist.get(row);
                for(int col = 0;col<zdlist.size();col++){//列
                    //数据从第一列开始
                    //创建单元格并放入数据
                    HSSFCell cell = newrow.createCell(col);
                    String value = data != null && data.get(zdlist.get(col)) != null ? String.valueOf(data.get(zdlist.get(col))) : "";
                    Boolean isNum = value.matches("^(-?\\d+)(\\.\\d+)?$");//data是否为数值
                    if (isNum) {
                        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));//保留两位小数点
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(Double.parseDouble(value));
                    }else {
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(value);
                    }
                }
            }
        try(FileOutputStream output=new FileOutputStream(filepath+filename)) {
            //判断是否存在目录. 不存在则创建
            isChartPathExist(filepath);
            //输出Excel文件
            wb.write(output);//写入磁盘
            success = true;
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }


    /**
     * 判断文件夹是否存在，如果不存在则新建
     *
     * @param dirPath 文件夹路径
     */
    private static void isChartPathExist(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
