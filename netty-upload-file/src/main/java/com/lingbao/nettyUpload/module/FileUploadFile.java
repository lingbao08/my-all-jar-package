package com.lingbao.nettyUpload.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * 上传文件实体
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-18 22:52
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadFile {

    /**
     * 文件
     */
    private File file;

    /**
     * 文件名
     */
    private String fileMd5;

    /**
     * 开始位置
     */
    private int starPos;

    /**
     * 文件字节数组
     */
    private byte[] bytes;

    /**
     * 结尾位置
     */
    private int endPos;

}
