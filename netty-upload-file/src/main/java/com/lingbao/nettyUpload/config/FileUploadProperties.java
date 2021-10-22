package com.lingbao.nettyUpload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author lingbao08
 * @desc
 * @date 10/22/21 01:50
 **/

@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
@PropertySource(value = "application.yml")
public class FileUploadProperties {

    private String host;

    private int port;

}
