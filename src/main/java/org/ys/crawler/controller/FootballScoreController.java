package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.constant.LeidataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.pipeLine.FootballScoreAndTeamPipeline;
import org.ys.crawler.processor.FootballScorePageProcessor;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballScoreService;
import org.ys.crawler.service.FootballSeasonCategoryService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/crawler/footballScoreController")
@RestController
public class FootballScoreController {
    @Autowired
    private FootballScoreService footballScoreService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballScorePageProcessor footballScorePageProcessor;
    @Autowired
    private FootballScoreAndTeamPipeline footballScoreAndTeamPipeline;

    /**
     * 爬取所有联赛的比分数据
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_CRAW')")
    @GetMapping("/startScoreCrawler")
    public HttpResult startScoreCrawler() throws Exception{
        Spider spider = null;
        List<FootballLeagueMatch> leagueMatches = footballLeagueMatchService.queryAll();
        if(null != leagueMatches && leagueMatches.size() > 0){
            List<Request> requests = new ArrayList<Request>();
            for (FootballLeagueMatch leagueMatch : leagueMatches) {
                List<Request> requestList = getRequestsByLeagueMatch(leagueMatch,LeidataCrawlerConstant.LEIDATA_CRAWLER_SCORE_TYPE);
                if(null != requestList && requestList.size() > 0){
                    requests.addAll(requestList);
                }
            }
            spider = getSpider(requests);
        }
        if(null != spider){
            return HttpResult.ok(spider.getStatus());
        }else{
            return HttpResult.error("赛季比分爬虫启动失败！");
        }
    }

    /**
     * 爬取指定联赛的比分数据
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_CRAW_BY_LEAGUE_MATCH')")
    @GetMapping("/startScoreCrawlerByLeagueMatch")
    public HttpResult startScoreCrawlerByLeagueMatch(@RequestParam String footballLeagueMatchId) throws Exception{
        Spider spider = null;
        if(StringUtils.isNotEmpty(footballLeagueMatchId)){
            FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(StringUtils.trim(footballLeagueMatchId));
            List<Request> requests = getRequestsByLeagueMatch(leagueMatch,null);
            spider = getSpider(requests);
        }
        if(null != spider){
            return HttpResult.ok(spider.getStatus());
        }else{
            return HttpResult.error("本联赛赛季比分爬虫启动失败！");
        }
    }

    /**
     * 根据联赛爬取比分数据
     * @param leagueMatch
     * @param crawType
     * @return
     * @throws Exception
     */
    private List<Request> getRequestsByLeagueMatch(FootballLeagueMatch leagueMatch,String crawType) throws Exception {
        if(null == leagueMatch){
            return null;
        }
        List<FootballSeasonCategory> seasonCategories = footballSeasonCategoryService.queryFootballSeasonCategoryByLeagueMatchId(leagueMatch.getFootballLeagueMatchId());
        List<Request> requests = new ArrayList<Request>();
        if(null != seasonCategories && seasonCategories.size() > 0){
            String middleUrl = leagueMatch.getLeagueMatchUrl().replace(LeidataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                    ,LeidataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_MIDDLE_WORD);
            Set<String> categoryIds = new HashSet<String>();
            for (FootballSeasonCategory category : seasonCategories) {
                //总爬虫不重复爬取
                if(StringUtils.isNotEmpty(crawType) && StringUtils.equals(crawType,LeidataCrawlerConstant.LEIDATA_CRAWLER_SCORE_TYPE)){
                    boolean existsFlag = footballScoreService.isExistsScoreByCategoryId(categoryIds, category.getFootballSeasonCategoryId());
                    if(existsFlag){
                        continue;
                    }
                }
                String url = LeidataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl+ category.getFootballSeasonCategoryUrl();
                int roundCount = category.getRoundCount();
                if(roundCount > 1){
                    for (int i = 1; i <= roundCount; i++) {
                        String fullUrl = url + "?skip=0&round=" + i;
                        Request request = new Request(fullUrl);
                        request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY,category);
                        requests.add(request);
                    }
                }else{
                    Request request = new Request(url);
                    request.putExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY,category);
                    requests.add(request);
                }
            }
        }
        return requests;
    }

    /**
     * 根据request启动爬虫
     * @param requests
     * @return
     */
    private Spider getSpider(List<Request> requests) {
        Spider spider = null;
        if(null != requests && requests.size() > 0){
            QueueScheduler scheduler = new QueueScheduler();
            scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
            spider = Spider.create(footballScorePageProcessor).setScheduler(scheduler);
            spider.addPipeline(footballScoreAndTeamPipeline);
            spider.startRequest(requests);
            spider.thread(1);
            spider.start();
        }
        return spider;
    }
}
