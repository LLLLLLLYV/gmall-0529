package com.atguigu.gmall.manager.mamager.controller;

import com.atguigu.gmall.manager.mamager.components.FastDFSTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Slf4j
@RequestMapping("/file")
@Controller
public class FileController {
    @Autowired
    FastDFSTemplate fastDFSTemplate;
    /**
     * 接收上传的文件
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if(!file.isEmpty()) {
            /*文件名.扩展名*/
            String fileName = file.getOriginalFilename();
            StorageClient storageClient = fastDFSTemplate.getStorageClient();
            String exe = StringUtils.substringAfterLast(fileName, ".");
            try {
                String[] strings = storageClient.upload_file(file.getBytes(), exe, null);
                String path = fastDFSTemplate.getPath(strings);
                return path;
            } catch (MyException e) {
                log.error("文件上传出错：{}",e);
            }
        }
        return "/images/error.png";
    }
}
