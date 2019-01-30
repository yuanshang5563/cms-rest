package org.ys.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.ys.core.service.RedisService;

/**
 * 系统初始化控制类
 * @author Administrator
 *
 */
@SuppressWarnings("rawtypes")
@Controller
public class InitializingController implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(InitializingController.class);
    @Autowired
    private RedisService redisService;

    /**
     * 系统初始化
     */
    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("---------系统初始化开始---------------");
        redisService.initSystemCach();
        logger.info("---------系统初始化结束---------------");
    }

}
