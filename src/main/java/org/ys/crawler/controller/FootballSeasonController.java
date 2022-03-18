package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.constant.LeidataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.pipeLine.FootballSeasonPipeline;
import org.ys.crawler.processor.FootballSeasonPageProcessor;
import org.ys.crawler.service.FootballLeagueMatchService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/crawler/footballSeasonController")
@RestController
public class FootballSeasonController {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballSeasonPageProcessor footballSeasonPageProcessor;
    @Autowired
    private FootballSeasonPipeline footballSeasonPipeline;

    @GetMapping("/startSeasonCrawler")
    public HttpResult startSeasonCrawler() throws Exception{
        List<FootballLeagueMatch> matches = footballLeagueMatchService.queryAll();
        List<Request> requests = new ArrayList<Request>();
        if(null != matches && matches.size() > 0){
            for (FootballLeagueMatch match : matches) {
                if(StringUtils.isEmpty(match.getLeagueMatchUrl())){
                    continue;
                }
                String middleUrl = match.getLeagueMatchUrl().replace(LeidataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                ,LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_MIDDLE_WORD);
                String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl;
                Request request = new Request(url);
                request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_LEAGUE_MATCH_ID,match.getFootballLeagueMatchId());
                requests.add(request);
            }
        }
        if(requests.size() > 0){
            QueueScheduler scheduler = new QueueScheduler();
            scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
            Spider spider = Spider.create(footballSeasonPageProcessor).setScheduler(scheduler);
            spider.addPipeline(footballSeasonPipeline);
            spider.startRequest(requests);
            spider.thread(1);
            spider.start();
            return HttpResult.ok("赛季爬虫启动成功！");
        }else{
            return HttpResult.ok("赛季爬虫启动失败！");
        }
    }

    @GetMapping("/startSeasonCrawler2")
    public HttpResult startSeasonCrawler2() throws Exception{
        String middleUrl = "/stats/competition/541/".replace(LeidataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                ,LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_MIDDLE_WORD);
        String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl;
        Request request = new Request(url);
        request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_LEAGUE_MATCH_ID,"be2c9171f85945a0a3ec701ba4dffdf4");
        Spider spider = Spider.create(footballSeasonPageProcessor);
        spider.addPipeline(footballSeasonPipeline);
        spider.addRequest(request);
        spider.thread(1);
        spider.start();
        return HttpResult.ok("赛季爬虫启动成功！");
    }
}
