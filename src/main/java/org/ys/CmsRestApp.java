package org.ys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */
@SpringBootApplication
@MapperScan({"org.ys.core.dao","org.ys.crawler.dao"})
public class CmsRestApp {
    public static void main( String[] args ){
        SpringApplication.run(CmsRestApp.class,args);
    }
}
