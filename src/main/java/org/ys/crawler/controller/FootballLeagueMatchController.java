package org.ys.crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.constant.LeidataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.crawler.downloader.FootballLeagueMatchDownloader;
import org.ys.crawler.pipeLine.FootballLeagueMatchPipeline;
import org.ys.crawler.processor.LeagueMatchPageProcessor;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

@RequestMapping("/crawler/footballLeagueMatchController")
@RestController
public class FootballLeagueMatchController {
    @Autowired
    private LeagueMatchPageProcessor leagueMatchPageProcessor;
    @Autowired
    private FootballLeagueMatchPipeline footballLeagueMatchPipeline;
    @Autowired
    private FootballLeagueMatchDownloader footballLeagueMatchDownloader;

    //private String startUrl = "https://www.hao123.com/?tn=94158081_hao_pg";

    @GetMapping("/startLeagueMatchCrawler")
    public HttpResult startLeagueMatchCrawler(){
        QueueScheduler scheduler = new QueueScheduler();
        scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
        Request request = new Request(LeidataCrawlerConstant.LEIDATA_CRAWLER_STATS_URL);
        System.out.println("------------1---------");
        Spider.create(leagueMatchPageProcessor)
                .setDownloader(footballLeagueMatchDownloader)
                .addPipeline(footballLeagueMatchPipeline)
                .addRequest(request)
                .setScheduler(scheduler)
                .thread(1)
                .start();
        System.out.println("------------2---------");
        return HttpResult.ok("执行成功！");
    }
}
