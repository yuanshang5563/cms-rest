package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.constant.LeidataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.pipeLine.FootballRoundPipeline;
import org.ys.crawler.pipeLine.FootballScoreDetailAndPlayerPipeline;
import org.ys.crawler.pipeLine.FootballSeasonCategoryPipeline;
import org.ys.crawler.processor.FootballRoundPageProcessor;
import org.ys.crawler.processor.FootballScoreDetailPageProcessor;
import org.ys.crawler.processor.FootballSeasonCategoryPageProcessor;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballScoreService;
import org.ys.crawler.service.FootballSeasonCategoryService;
import org.ys.crawler.service.FootballSeasonService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/crawler/footballScoreDetailController")
@RestController
public class FootballScoreDetailController {
    @Autowired
    private FootballScoreService footballScoreService;
    @Autowired
    private FootballScoreDetailPageProcessor footballScoreDetailPageProcessor;
    @Autowired
    private FootballScoreDetailAndPlayerPipeline footballScoreDetailAndPlayerPipeline;

    @GetMapping("/startSeasonDetailCrawlerBySeason")
    public HttpResult startSeasonDetailCrawlerBySeason() throws Exception {
        String seasonId = "6fe3d2ee98784a358bf4f57c93542423";
        List<Request> requests = new ArrayList<Request>();
        List<FootballScore> footballScores= footballScoreService.queryFootballScoresBySeasonId(seasonId);
        if (null != footballScores && footballScores.size() > 0) {
            String middleUrl = "/" + LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_STATS_MIDDLE_WORD + "/" +
                    LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_MIDDLE_WORD + "/";
            for (FootballScore footballScore : footballScores) {
                String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + footballScore.getLeidataScoreId();
                Request request = new Request(url);
                request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SCORE, footballScore);
                requests.add(request);
            }
        }
        if(requests.size() > 0){
            Spider spider = Spider.create(footballScoreDetailPageProcessor);
            spider.addPipeline(footballScoreDetailAndPlayerPipeline);
            spider.startRequest(requests);
            spider.thread(1);
            spider.start();
            return HttpResult.ok("赛季详情批量爬虫启动成功！");
        }else{
            return HttpResult.ok("赛季详情批量爬虫启动失败！");
        }
    }
}
