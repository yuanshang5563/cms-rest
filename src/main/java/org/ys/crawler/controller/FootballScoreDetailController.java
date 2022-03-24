package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.pipeLine.FootballScoreDetailAndPlayerPipeline;
import org.ys.crawler.processor.FootballScoreDetailPageProcessor;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballScoreService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/crawler/footballScoreDetailController")
@RestController
public class FootballScoreDetailController {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballScoreService footballScoreService;
    @Autowired
    private FootballScoreDetailPageProcessor footballScoreDetailPageProcessor;
    @Autowired
    private FootballScoreDetailAndPlayerPipeline footballScoreDetailAndPlayerPipeline;

    /**
     * 爬取所有联赛的比分详情记录
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_DETAIL_CRAW')")
    @GetMapping("/startSeasonDetailCrawler")
    public HttpResult startSeasonDetailCrawler() throws Exception {
        List<FootballLeagueMatch> leagueMatches = footballLeagueMatchService.queryAll();
        List<Request> requests = new ArrayList<Request>();
        if(null != leagueMatches && leagueMatches.size() > 0){
            for (FootballLeagueMatch leagueMatch : leagueMatches) {
                List<Request> requestList = getRequestsByLeagueMatchId(leagueMatch.getFootballLeagueMatchId(), LeiDataCrawlerConstant.LEIDATA_CRAWLER_SCORE_DETAIL_TYPE);
                if(null != requestList && requestList.size() > 0){
                    requests.addAll(requestList);
                }
            }
        }
        Spider spider =getSpider(requests);
        if(null != spider){
            return HttpResult.ok("赛季比分详情爬虫启动成功！");
        }else{
            return HttpResult.error("赛季详情批量爬虫启动失败！");
        }
    }

    /**
     * 爬取指定联赛的比分详情记录
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_DETAIL_CRAW_BY_LEAGUE_MATCH')")
    @GetMapping("/startSeasonDetailCrawlerByLeagueMatch")
    public HttpResult startSeasonDetailCrawlerByLeagueMatch(@RequestParam String footballLeagueMatchId) throws Exception {
        List<Request> requests = getRequestsByLeagueMatchId(footballLeagueMatchId,null);
        Spider spider =getSpider(requests);
        if(null != spider){
            return HttpResult.ok("本赛季比分详情爬虫启动成功！");
        }else{
            return HttpResult.error("本赛季比分详情批量爬虫启动失败！");
        }
    }

    /**
     * 根据联赛Id获取要爬取的request
     * @param footballLeagueMatchId
     * @param crawType
     * @return
     * @throws Exception
     */
    private List<Request> getRequestsByLeagueMatchId(String footballLeagueMatchId,String crawType) throws Exception {
        List<Request> requests = new ArrayList<Request>();
        if(StringUtils.isNotEmpty(footballLeagueMatchId)){
            List<FootballScore> footballScores = footballScoreService.queryFootballScoresByLeagueMatchId(footballLeagueMatchId);
            if (null != footballScores && footballScores.size() > 0) {
                String middleUrl = "/" + LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_STATS_MIDDLE_WORD + "/" +
                        LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_MIDDLE_WORD + "/";
                Set<String> categoryIds = new HashSet<String>();
                for (FootballScore footballScore : footballScores) {
                    if(StringUtils.isNotEmpty(crawType) && StringUtils.equals(crawType, LeiDataCrawlerConstant.LEIDATA_CRAWLER_SCORE_DETAIL_TYPE)){
                        boolean existsFlag = footballScoreService.isExistsScoreByCategoryId(categoryIds, footballScore.getFootballSeasonCategoryId());
                        if(existsFlag){
                            continue;
                        }
                    }
                    String url = LeiDataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + footballScore.getLeidataScoreId();
                    Request request = new Request(url);
                    request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SCORE, footballScore);
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
            spider = Spider.create(footballScoreDetailPageProcessor).setScheduler(scheduler);
            spider.addPipeline(footballScoreDetailAndPlayerPipeline);
            spider.startRequest(requests);
            spider.thread(1);
            spider.start();
        }
        return spider;
    }
}
