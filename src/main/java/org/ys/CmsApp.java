package org.ys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *系统入口，启动App
 */
@SpringBootApplication
@MapperScan({"org.ys.core.dao","org.ys.crawler.dao"})
public class CmsApp {
    public static void main( String[] args ){
        SpringApplication.run(CmsApp.class,args);
    }
}
