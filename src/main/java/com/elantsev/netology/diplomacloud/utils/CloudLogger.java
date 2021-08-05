package com.elantsev.netology.diplomacloud.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class CloudLogger {

    private static final Logger logger = LogManager.getLogger("filesLog");

    @Value("${logger.value:all}")
    private String value;

    @PostConstruct
    private void init() {
        if (!(value.toLowerCase().equals("all") || (value.toLowerCase().equals("errors")))) {
            value = "all";
        }
        logger.info("Logger started. Log's level: " + value);
    }

    public void logError(String error) {
        logger.error(error);
    }

    public void logInfo(String info) {
        if(value.toLowerCase().equals("all")) {
            logger.info(info);
        }
    }
}
