package com.lingbao.nettyUpload.controller;

import com.lingbao.nettyUpload.client.FileUploadClientComponent;
import com.lingbao.nettyUpload.module.FileUploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author lingbao08
 * @desc
 * @date 10/22/21 01:33
 **/
@RestController
public class FileUploadController {

    @Autowired
    private FileUploadClientComponent fileUploadClientComponent;

    @RequestMapping("/upload")
    public String upload() {
        String fileName = "/Users/lingbao08/tt/ddd.docx";
        File file = new File(fileName);
        FileUploadFile uploadFile = FileUploadFile.builder()
                .file(file).fileMd5(file.getName())
                .starPos(0) // 文件开始位置
                .build();

        fileUploadClientComponent.connect(uploadFile);

        return "1";

    }
}
