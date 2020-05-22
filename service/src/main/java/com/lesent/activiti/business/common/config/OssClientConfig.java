package com.lesent.activiti.business.common.config;


import com.lsdk.activiti.business.utils.OSSClientUtilLsdk;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "oss")
public class OssClientConfig {

    private String endpoint ;
    private String accessKeyId;
    private String accessKeySecret ;
    private String bucketName;//

    @Bean
    public OSSClientUtilLsdk ossClientUtilLsdk(){
        return new OSSClientUtilLsdk(endpoint,accessKeyId,accessKeySecret,bucketName);
    }

}
