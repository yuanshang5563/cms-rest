package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.constant.LeidataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.pipeLine.FootballRoundPipeline;
import org.ys.crawler.pipeLine.FootballSeasonCategoryPipeline;
import org.ys.crawler.processor.FootballRoundPageProcessor;
import org.ys.crawler.processor.FootballSeasonCategoryPageProcessor;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballSeasonCategoryService;
import org.ys.crawler.service.FootballSeasonService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/crawler/footballSeasonCategoryController")
@RestController
public class FootballSeasonCategoryController {
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;
    @Autowired
    private FootballSeasonCategoryPageProcessor footballSeasonCategoryPageProcessor;
    @Autowired
    private FootballRoundPageProcessor footballRoundPageProcessor;
    @Autowired
    private FootballRoundPipeline footballRoundPipeline;
    @Autowired
    private FootballSeasonCategoryPipeline footballSeasonCategoryPipeline;

    @GetMapping("/startSeasonCategoryCrawler")
    public HttpResult startSeasonCategoryCrawler() throws Exception{
        List<FootballSeason> footballSeasons = footballSeasonService.queryAll();
        if(null != footballSeasons && footballSeasons.size() > 0){
            QueueScheduler scheduler = new QueueScheduler();
            scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
            Spider spider = Spider.create(footballSeasonCategoryPageProcessor).setScheduler(scheduler);
            spider.addPipeline(footballSeasonCategoryPipeline);
            for (FootballSeason footballSeason : footballSeasons) {
                if(StringUtils.isEmpty(footballSeason.getFootballSeasonUrl())){
                    continue;
                }
               // String middleUrl = match.getLeagueMatchUrl().replace(LeidataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                //,LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_MIDDLE_WORD);
                //String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl;
                //redisTemplate.opsForValue().set(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_LEAGUE_MATCH_ID_REDIS,match.getFootballLeagueMatchId(),1, TimeUnit.HOURS);
                //spider.addUrl(url);
            }
            spider.thread(4);
            spider.start();
        }
        return HttpResult.ok("赛季类别爬虫启动成功！");
    }

    @GetMapping("/startSeasonCategoryCrawler2")
    public HttpResult startSeasonCategoryCrawler2() throws Exception{
        FootballSeason footballSeason = footballSeasonService.queryFootballSeasonById("6fe3d2ee98784a358bf4f57c93542423");
        if(null != footballSeason){
            String middleUrl = "/stats/competition/541/".replace(LeidataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                    ,LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_MIDDLE_WORD);
            middleUrl = middleUrl + footballSeason.getFootballSeasonUrl().replace("/","");
            String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl;
            Request request = new Request(url);
            //redisTemplate.opsForValue().set(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_REDIS,footballSeason,1, TimeUnit.HOURS);
            request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON,footballSeason);
            //spider.addUrl(url);
            Spider spider = Spider.create(footballSeasonCategoryPageProcessor);
            spider.addPipeline(footballSeasonCategoryPipeline);
            spider.addRequest(request);
            spider.thread(1);
            spider.start();
            return HttpResult.ok("赛季类别爬虫启动成功！");
        }else{
            return HttpResult.error("未找到要爬取的赛季对象！");
        }
    }

    @GetMapping("/startSeasonRoundCrawler")
    public HttpResult startSeasonRoundCrawler() throws Exception{
        String categoryId = "0915e923416549c0848fbd728c0f0880";
        FootballSeasonCategory footballSeasonCategory = footballSeasonCategoryService.queryFootballSeasonCategoryById(categoryId);
        if(null != footballSeasonCategory){

            String middleUrl = "/stats/competition/541/".replace(LeidataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                    ,LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_ROUND_MIDDLE_WORD);
            String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + footballSeasonCategory.getFootballSeasonCategoryUrl();
            Request request = new Request(url);
            request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY,footballSeasonCategory);
            Spider spider = Spider.create(footballRoundPageProcessor);
            spider.addPipeline(footballRoundPipeline);
            spider.addRequest(request);
            spider.thread(1);
            spider.start();
        }
        return HttpResult.ok("赛季类别轮数爬虫启动成功！");
    }

    @GetMapping("/startSeasonCateCrawlerByLeagueMatch")
    public HttpResult startSeasonCateCrawlerByLeagueMatch() throws Exception{
        String leagueMatchId = "be2c9171f85945a0a3ec701ba4dffdf4";
        List<Request> requests = new ArrayList<Request>();
        FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(leagueMatchId);
        List<FootballSeason> footballSeasons = footballSeasonService.queryFootballSeasonsByLeagueMatch(leagueMatchId);
        if(null != footballSeasons && footballSeasons.size() > 0) {
            String middleUrl = leagueMatch.getLeagueMatchUrl().replace(LeidataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                    , LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_MIDDLE_WORD);
            for (FootballSeason footballSeason : footballSeasons) {
                String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + footballSeason.getFootballSeasonUrl().replace("/", "");
                Request request = new Request(url);
                request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON, footballSeason);
                requests.add(request);
            }
        }
        if(requests.size() > 0){
            Spider spider = Spider.create(footballSeasonCategoryPageProcessor);
            spider.addPipeline(footballSeasonCategoryPipeline);
            spider.startRequest(requests);
            spider.thread(1);
            spider.start();
            return HttpResult.ok("赛季类别批量爬虫启动成功！");
        }else{
            return HttpResult.ok("赛季类别批量爬虫启动失败！");
        }
    }

    @GetMapping("/startSeasonRoundCrawlerByLeagueMatch")
    public HttpResult startSeasonRoundCrawlerByLeagueMatch() throws Exception {
        String leagueMatchId = "be2c9171f85945a0a3ec701ba4dffdf4";
        FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(leagueMatchId);
        List<FootballSeasonCategory> seasonCategories = footballSeasonCategoryService.queryFootballSeasonCategoryByLeagueMatchId(leagueMatchId);
        List<Request> requests = new ArrayList<Request>();
        if (null != seasonCategories && seasonCategories.size() > 0) {
            for (FootballSeasonCategory seasonCategory : seasonCategories) {

                String middleUrl = leagueMatch.getLeagueMatchUrl().replace(LeidataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                        , LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_ROUND_MIDDLE_WORD);
                String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + seasonCategory.getFootballSeasonCategoryUrl();
                Request request = new Request(url);
                request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY, seasonCategory);
                requests.add(request);
            }
        }
        if(requests.size() > 0){
            Spider spider = Spider.create(footballRoundPageProcessor);
            spider.addPipeline(footballRoundPipeline);
            spider.startRequest(requests);
            spider.thread(1);
            spider.start();
            return HttpResult.ok("赛季类别轮数批量爬虫启动成功！");
        }else{
            return HttpResult.ok("赛季类别轮数批量爬虫启动失败！");
        }
    }
}
