package cn.jjy.reggie.controller;

import cn.jjy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @Author: jiajunyu
 * @CreateTime: 2022-11-08  10:58
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        OutputStream outputStream = null;
        String name = multipartFile.getOriginalFilename();
        String[] split = name.split("\\.");
        name = split[split.length - 1];

        try {
            log.info(name);

            byte[] bytes = multipartFile.getBytes();
            String path;

            File saveFile = new File(basePath);
            //如果路径下没有该文件夹则创建文件夹
            if (saveFile.isDirectory()) {
                log.info("目录已存在");
            } else {
                saveFile.mkdir();
            }
            path = basePath + UUID.randomUUID() + "." + name;
            File file = new File(path);

            outputStream = new FileOutputStream(file);

            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }

        return R.success("上传成功");
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        FileInputStream io = null;

        ServletOutputStream out = response.getOutputStream();
        try {
            response.setHeader("Content-Disposition","attachment;filename="+name);

            response.setContentType("image/jpeg");
            File file = new File(basePath + name);
            io = new FileInputStream(file);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = io.read(bytes)) != -1) {
                out.write(bytes,0,len);
                out.flush();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            io.close();
            out.close();
        }
    }



}
