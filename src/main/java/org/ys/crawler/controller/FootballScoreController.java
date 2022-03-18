package org.ys.crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.constant.LeidataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.pipeLine.FootballScoreAndTeamPipeline;
import org.ys.crawler.processor.FootballScorePageProcessor;
import org.ys.crawler.service.FootballSeasonCategoryService;
import org.ys.crawler.service.FootballTeamService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/crawler/footballScoreController")
@RestController
public class FootballScoreController {
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;
    @Autowired
    private FootballTeamService footballTeamService;
    @Autowired
    private FootballScorePageProcessor footballScorePageProcessor;
    @Autowired
    private FootballScoreAndTeamPipeline footballScoreAndTeamPipeline;

    @GetMapping("/startScoreCrawler2")
    public HttpResult startScoreCrawler2() throws Exception{
        List<Request> requests = new ArrayList<Request>();
        String categoryId = "0915e923416549c0848fbd728c0f0880";
        FootballSeasonCategory seasonCategory = footballSeasonCategoryService.queryFootballSeasonCategoryById(categoryId);
        if(null != seasonCategory){
            String middleUrl = "/stats/competition/541/".replace(LeidataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                    ,LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_MIDDLE_WORD);
            String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl+ seasonCategory.getFootballSeasonCategoryUrl();
            int roundCount = seasonCategory.getRoundCount();
            if(roundCount != 0 && roundCount != -1){
                for (int i = 1; i <= roundCount; i++) {
                    String fullUrl = url + "?skip=0&round=" + i;
                    Request request = new Request(fullUrl);
                    request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY,seasonCategory);
                    requests.add(request);
                }
            }else{
                Request request = new Request(url);
                request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY,seasonCategory);
                requests.add(request);
            }
        }
        if(requests.size() > 0){
            Spider spider = Spider.create(footballScorePageProcessor);
            spider.addPipeline(footballScoreAndTeamPipeline);
            spider.startRequest(requests);
            spider.thread(1);
            spider.start();
            return HttpResult.ok("赛季比分爬虫启动成功！");
        }else{
            return HttpResult.ok("赛季比分爬虫启动失败！");
        }
    }
}
