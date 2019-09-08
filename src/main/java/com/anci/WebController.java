package com.anci;

import com.anci.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2019/9/1.
 */
@Controller
@Slf4j
public class WebController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    /**
     * 下载文件
     * 请求地址：http://localhost:8081/home/excel/*.excel
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ISO-8859-1 ==> UTF-8 进行编码转换
        String fileName = request.getParameter("fileName");
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            fileName = fileName.replace("..", "");
            if (fileName.endsWith(",")) {
                fileName = fileName.substring(0, fileName.length() - 1);
            }
            String downloadFilePath = Constants.FILE_PATH + File.separator + fileName + ".xlsx";
            File file = new File(downloadFilePath);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开            
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"),"iso-8859-1"));
                inputStream = new BufferedInputStream(new FileInputStream(file));
                outputStream = response.getOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                response.flushBuffer();
            }

        } catch (Exception e) {
            log.info("文件下载失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
