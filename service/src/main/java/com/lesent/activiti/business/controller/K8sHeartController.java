package com.lesent.activiti.business.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class K8sHeartController {

    private static final Logger logger = LoggerFactory.getLogger(K8sHeartController.class);

    @RequestMapping
    public String heart(){
        logger.info(" ================== heart beat ================= ");
        return "SUCCESS";
    }

}
