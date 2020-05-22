package com.lesent.activiti.business;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication(exclude={
        org.activiti.spring.boot.SecurityAutoConfiguration.class
})
@MapperScan("com.lsdk.activiti.business.dao.mapper")
//@ComponentScan(value = "com.lsdk.activiti.business",excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = ActivitiServiceUtils.class)})
@ComponentScan("com.lsdk.activiti.business")
@EnableScheduling
public class ActivitiApplication {

    public static void main(String[] args){
        SpringApplication.run(ActivitiApplication.class,args);
    }

}
