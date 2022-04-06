package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.ys.common.constant.CrawlerConstant;
import org.ys.core.model.CoreParameter;
import org.ys.core.service.CoreParameterService;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * 基础爬虫controller，定义通用方法
 */
public class BaseCrawlerController {
    @Autowired
    protected CoreParameterService coreParameterService;

    /**
     * 获取线程数
     * @return
     */
    protected int getThreadCount(){
        CoreParameter crawThreadCountParam = null;
        try {
            crawThreadCountParam = coreParameterService.queryCoreParameterByParamCode(CrawlerConstant.CRAW_THREAD_COUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null == crawThreadCountParam || StringUtils.isEmpty(crawThreadCountParam.getParamValue())){
            return 1;
        }else{
            return Integer.parseInt(crawThreadCountParam.getParamValue());
        }
    }

    /**
     * 获取积分爬取轮数控制
     * @return
     */
    protected int getIntegralRoundCount(){
        CoreParameter crawTIntegralRoundCountParam = null;
        try {
            crawTIntegralRoundCountParam = coreParameterService.queryCoreParameterByParamCode(CrawlerConstant.CRAW_INTEGRAL_ROUND_COUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null == crawTIntegralRoundCountParam || StringUtils.isEmpty(crawTIntegralRoundCountParam.getParamValue())){
            return 1;
        }else{
            return Integer.parseInt(crawTIntegralRoundCountParam.getParamValue());
        }
    }

    /**
     * 获取过滤器
     * @return
     */
    protected QueueScheduler getQueueScheduler() {
        QueueScheduler scheduler = new QueueScheduler();
        scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
        return scheduler;
    }
}
