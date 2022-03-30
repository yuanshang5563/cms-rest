package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.crawler.controller.vo.FootballScoreCondition;
import org.ys.crawler.model.*;
import org.ys.crawler.pipeLine.FootballScoreAndTeamPipeline;
import org.ys.crawler.processor.FootballScorePageProcessor;
import org.ys.crawler.service.*;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/crawler/footballScoreController")
@RestController
public class FootballScoreController extends BaseCrawlerController{
    @Autowired
    private FootballTeamService footballTeamService;
    @Autowired
    private FootballScoreService footballScoreService;
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballScorePageProcessor footballScorePageProcessor;
    @Autowired
    private FootballScoreAndTeamPipeline footballScoreAndTeamPipeline;

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody FootballScoreCondition scoreCondition){
        if(null == scoreCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballScore> pageBean = null;
        try {
            String round = scoreCondition.getRound();
            String footballLeagueMatchId = scoreCondition.getFootballLeagueMatchId();
            String footballSeasonCategoryId = scoreCondition.getFootballSeasonCategoryId();
            String footballSeasonId = scoreCondition.getFootballSeasonId();
            String cascaderId = scoreCondition.getCascaderId();
            FootballScoreExample example = new FootballScoreExample();
            FootballScoreExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(round)){
                criteria.andRoundEqualTo(round.trim());
            }
            if(StringUtils.isNotEmpty(cascaderId)){
                if(cascaderId.contains("footballLeagueMatchId:")){
                    footballLeagueMatchId = cascaderId.replaceAll("footballLeagueMatchId:","");
                }else if(cascaderId.contains("footballSeasonId:")){
                    footballSeasonId = cascaderId.replaceAll("footballSeasonId:","");
                }else if(cascaderId.contains("footballSeasonCategoryId:")){
                    footballSeasonCategoryId = cascaderId.replaceAll("footballSeasonCategoryId:","");
                }
            }
            //类别，赛季和联赛都不为空，哪个最小查哪个
            if(StringUtils.isNotEmpty(footballSeasonCategoryId)){
                criteria.andFootballSeasonCategoryIdEqualTo(footballSeasonCategoryId.trim());
            }else{
                if(StringUtils.isNotEmpty(footballSeasonId)){
                    criteria.andFootballSeasonIdEqualTo(footballSeasonId.trim());
                }else{
                    if(StringUtils.isNotEmpty(footballLeagueMatchId)){
                        criteria.andFootballLeagueMatchIdEqualTo(footballLeagueMatchId.trim());
                    }
                }
            }
            pageBean = footballScoreService.pageFootballScoresByExample(example, scoreCondition.getPageNum(), scoreCondition.getPageSize());
            if(null == pageBean){
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam String footballScoreId){
        if(StringUtils.isEmpty(footballScoreId)){
            return HttpResult.ok();
        }
        try {
            FootballScore footballScore = footballScoreService.queryFootballScoreById(footballScoreId.trim());
            if(null != footballScore){
                FootballTeam homeTeam = footballTeamService.queryFootballTeamById(footballScore.getHomeFootballTeamId());
                if(null != homeTeam){
                    footballScore.setHomeFootballTeamName(homeTeam.getTeamName());
                }
                FootballTeam awayTeam = footballTeamService.queryFootballTeamById(footballScore.getAwayFootballTeamId());
                if(null != awayTeam){
                    footballScore.setAwayFootballTeamName(awayTeam.getTeamName());
                }
                //填充联赛，赛季，类别的名称
                FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(footballScore.getFootballLeagueMatchId());
                if(null != leagueMatch){
                    footballScore.setFootballLeagueMatchName(leagueMatch.getFootballLeagueMatchName());
                }
                FootballSeason season = footballSeasonService.queryFootballSeasonById(footballScore.getFootballSeasonId());
                if(null != season){
                    footballScore.setFootballSeasonName(season.getFootballSeasonName());
                }
                FootballSeasonCategory seasonCategory = footballSeasonCategoryService.queryFootballSeasonCategoryById(footballScore.getFootballSeasonCategoryId());
                if(null != seasonCategory){
                    footballScore.setFootballSeasonCategoryName(seasonCategory.getFootballSeasonCategoryName());
                }
            }
            return HttpResult.ok(footballScore);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

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
                List<Request> requestList = getRequestsByLeagueMatch(leagueMatch, LeiDataCrawlerConstant.LEIDATA_CRAWLER_SCORE_TYPE);
                if(null != requestList && requestList.size() > 0){
                    requests.addAll(requestList);
                }
            }
            spider = getSpider(requests);
        }
        if(null != spider){
            return HttpResult.ok("赛季比分爬虫启动成功！");
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
            return HttpResult.ok("本赛季比分爬虫启动成功！");
        }else{
            return HttpResult.error("本联赛赛季比分爬虫启动失败！");
        }
    }

    /**
     * 根据赛季Id爬取比赛数据
     * @param footballSeasonId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_CRAW_BY_SEASON')")
    @GetMapping("/startScoreCrawlerBySeason")
    public HttpResult startScoreCrawlerBySeason(@RequestParam String footballSeasonId) throws Exception{
        Spider spider = null;
        if(StringUtils.isNotEmpty(footballSeasonId)){
            List<FootballSeasonCategory> categories = footballSeasonCategoryService.queryFootballSeasonCategoryBySeasonId(footballSeasonId);
            if(null != categories && categories.size() > 0){
                List<Request> requests = new ArrayList<Request>();
                for (FootballSeasonCategory category : categories) {
                    List<Request> requestList = getRequestsBySeasonCategory(category);
                    if(null != requestList && requestList.size() > 0){
                        requests.addAll(requestList);
                    }
                }
                spider = getSpider(requests);
            }
        }
        if(null != spider){
            return HttpResult.ok("本赛季的比分爬虫启动成功！");
        }else{
            return HttpResult.error("本赛季的比分爬虫启动失败！");
        }
    }

    /**
     * 根据赛季类别Id爬取比赛数据
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_CRAW_BY_SEASON_CATEGORY')")
    @GetMapping("/startScoreCrawlerBySeasonCategory")
    public HttpResult startScoreCrawlerBySeasonCategory(@RequestParam String footballSeasonCategoryId) throws Exception{
        Spider spider = null;
        if(StringUtils.isNotEmpty(footballSeasonCategoryId)){
            FootballSeasonCategory seasonCategory = footballSeasonCategoryService.queryFootballSeasonCategoryById(footballSeasonCategoryId);
            List<Request> requests = getRequestsBySeasonCategory(seasonCategory);
            spider = getSpider(requests);
        }
        if(null != spider){
            return HttpResult.ok("本赛季类别的比分爬虫启动成功！");
        }else{
            return HttpResult.error("本赛季类别的比分爬虫启动失败！");
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
            String middleUrl = leagueMatch.getLeagueMatchUrl().replace(LeiDataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                    , LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_MIDDLE_WORD);
            Set<String> categoryIds = new HashSet<String>();
            for (FootballSeasonCategory category : seasonCategories) {
                //总爬虫不重复爬取
                if(StringUtils.isNotEmpty(crawType) && StringUtils.equals(crawType, LeiDataCrawlerConstant.LEIDATA_CRAWLER_SCORE_TYPE)){
                    boolean existsFlag = footballScoreService.isExistsScoreByCategoryId(categoryIds, category.getFootballSeasonCategoryId());
                    if(existsFlag){
                        continue;
                    }
                }
                getRequestBySeasonCategory(category, requests, middleUrl);
            }
        }
        return requests;
    }

    /**
     * 根据赛季类别爬取比分数据
     * @param seasonCategory
     * @return
     * @throws Exception
     */
    private List<Request> getRequestsBySeasonCategory(FootballSeasonCategory seasonCategory) throws Exception {
        if(null == seasonCategory){
            return null;
        }
        String footballSeasonId = seasonCategory.getFootballSeasonId();
        FootballSeason footballSeason = footballSeasonService.queryFootballSeasonById(footballSeasonId);
        if(null == footballSeason){
            return null;
        }
        String leagueMatchId = footballSeason.getFootballLeagueMatchId();
        FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(leagueMatchId);
        if(null == leagueMatch){
            return null;
        }
        List<Request> requests = new ArrayList<Request>();
        String middleUrl = leagueMatch.getLeagueMatchUrl().replace(LeiDataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                , LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_MIDDLE_WORD);
        getRequestBySeasonCategory(seasonCategory, requests, middleUrl);
        return requests;
    }

    /**
     * 根据类别获取request,通用方法
     * @param seasonCategory
     * @param requests
     * @param middleUrl
     */
    private void getRequestBySeasonCategory(FootballSeasonCategory seasonCategory, List<Request> requests, String middleUrl) {
        if(null != seasonCategory && null != requests && StringUtils.isNotEmpty(middleUrl)){
            String url = LeiDataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + seasonCategory.getFootballSeasonCategoryUrl();
            int roundCount = seasonCategory.getRoundCount();
            if (roundCount > 1) {
                for (int i = 1; i <= roundCount; i++) {
                    String fullUrl = url + "?skip=0&round=" + i;
                    Request request = new Request(fullUrl);
                    request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY, seasonCategory);
                    requests.add(request);
                }
            } else {
                Request request = new Request(url);
                request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY, seasonCategory);
                requests.add(request);
            }
        }
    }

    /**
     * 根据request启动爬虫
     * @param requests
     * @return
     */
    private Spider getSpider(List<Request> requests) {
        Spider spider = null;
        if(null != requests && requests.size() > 0){
            QueueScheduler scheduler = getQueueScheduler();
            spider = Spider.create(footballScorePageProcessor).setScheduler(scheduler);
            spider.addPipeline(footballScoreAndTeamPipeline);
            spider.startRequest(requests);
            spider.thread(getThreadCount());
            spider.start();
        }
        return spider;
    }
}
