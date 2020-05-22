package com.lesent.activiti.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class StartRunner implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(StartRunner.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info(" ================  Start Activiti Application Successfully ========================");
    }
}
