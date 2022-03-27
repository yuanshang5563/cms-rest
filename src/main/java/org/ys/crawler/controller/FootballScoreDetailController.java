package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.constant.CrawlerConstant;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.core.model.CoreParameter;
import org.ys.crawler.controller.vo.FootballScoreDetailCondition;
import org.ys.crawler.model.*;
import org.ys.crawler.pipeLine.FootballScoreDetailAndPlayerPipeline;
import org.ys.crawler.processor.FootballScoreDetailPageProcessor;
import org.ys.crawler.service.*;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/crawler/footballScoreDetailController")
@RestController
public class FootballScoreDetailController extends BaseCrawlerController{
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;
    @Autowired
    private FootballScoreService footballScoreService;
    @Autowired
    private FootballTeamService footballTeamService;
    @Autowired
    private FootballPlayerService footballPlayerService;
    @Autowired
    private FootballScoreDetailService footballScoreDetailService;
    @Autowired
    private FootballScoreDetailPageProcessor footballScoreDetailPageProcessor;
    @Autowired
    private FootballScoreDetailAndPlayerPipeline footballScoreDetailAndPlayerPipeline;

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_DETAIL_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody FootballScoreDetailCondition scoreDetailCondition){
        if(null == scoreDetailCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballScoreDetail> pageBean = null;
        try {
            String footballScoreId = scoreDetailCondition.getFootballScoreId();
            String footballPeriodName = scoreDetailCondition.getFootballPeriodName();
            String teamName = scoreDetailCondition.getTeamName();
            String footballPlayerName = scoreDetailCondition.getFootballPlayerName();
            FootballScoreDetailExample example = new FootballScoreDetailExample();
            FootballScoreDetailExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(footballScoreId)){
                criteria.andFootballScoreIdEqualTo(footballScoreId.trim());
            }
            if(StringUtils.isNotEmpty(footballPeriodName)){
                criteria.andFootballPeriodNameLike("%"+footballPeriodName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(teamName)){
                List<String> teamIds = new ArrayList<String>();
                List<FootballTeam> footballTeams = footballTeamService.queryFootballTeamsByName(teamName);
                if(null != footballTeams && footballTeams.size() > 0){
                    for (FootballTeam team : footballTeams) {
                        teamIds.add(team.getFootballTeamId());
                    }
                }
                criteria.andGoalFootballTeamIdIn(teamIds);
            }
            if(StringUtils.isNotEmpty(footballPlayerName)){
                List<String> playerIds = new ArrayList<String>();
                List<FootballPlayer> footballPlayers = footballPlayerService.queryFootballPlayersByName(footballPlayerName);
                if(null != footballPlayers && footballPlayers.size() > 0){
                    for (FootballPlayer player : footballPlayers) {
                        playerIds.add(player.getFootballTeamId());
                    }
                }
                criteria.andFootballPlayerIdIn(playerIds);
            }
            pageBean = footballScoreDetailService.pageFootballScoreDetailsByExample(example, scoreDetailCondition.getPageNum(), scoreDetailCondition.getPageSize());
            if(null == pageBean){
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_DETAIL_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam String footballScoreDetailId){
        if(StringUtils.isEmpty(footballScoreDetailId)){
            return HttpResult.ok();
        }
        try {
            FootballScoreDetail detail = footballScoreDetailService.queryFootballScoreDetailById(footballScoreDetailId.trim());
            if(null != detail){
                FootballTeam team = footballTeamService.queryFootballTeamById(detail.getGoalFootballTeamId());
                if(null != team){
                    detail.setTeamName(team.getTeamName());
                }
                FootballPlayer player = footballPlayerService.queryFootballPlayerById(detail.getFootballPlayerId());
                if(null != player){
                    detail.setFootballPlayerName(player.getFootballPlayerName());
                }
            }
            return HttpResult.ok(detail);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

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
     * 根据赛季Id爬取下面的比分详情数据
     * @param footballSeasonId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_DETAIL_CRAW_BY_SEASON')")
    @GetMapping("/startSeasonDetailCrawlerBySeason")
    public HttpResult startSeasonDetailCrawlerBySeason(@RequestParam String footballSeasonId) throws Exception {
        Spider spider = null;
        if(StringUtils.isNotEmpty(footballSeasonId)){
            List<FootballSeasonCategory> categories = footballSeasonCategoryService.queryFootballSeasonCategoryBySeasonId(footballSeasonId);
            if(null != categories && categories.size() > 0){
                List<Request> requests = new ArrayList<Request>();
                for (FootballSeasonCategory category : categories) {
                    List<Request> requestList = getRequestsByCategoryId(category.getFootballSeasonCategoryId());
                    if(null != requestList && requestList.size() > 0){
                        requests.addAll(requestList);
                    }
                }
                spider = getSpider(requests);
            }
        }
        if(null != spider){
            return HttpResult.ok("本赛季的比分详情爬虫启动成功！");
        }else{
            return HttpResult.error("本赛季的比分详情批量爬虫启动失败！");
        }
    }

    /**
     * 根据赛季类别Id爬取下面的比分详情数据
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SCORE_DETAIL_CRAW_BY_SEASON_CATEGORY')")
    @GetMapping("/startSeasonDetailCrawlerBySeasonCategory")
    public HttpResult startSeasonDetailCrawlerBySeasonCategory(@RequestParam String footballSeasonCategoryId) throws Exception {
        Spider spider = null;
        List<Request> requests = getRequestsByCategoryId(footballSeasonCategoryId);
        spider = getSpider(requests);
        if(null != spider){
            return HttpResult.ok("本赛季的比分详情爬虫启动成功！");
        }else{
            return HttpResult.error("本赛季的比分详情批量爬虫启动失败！");
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
            List<FootballScore> footballScores = null;
            int count = getSeasonCount();
            if(count == 0){
                footballScores = footballScoreService.queryFootballScoresByLeagueMatchId(footballLeagueMatchId);
            }else{
                List<FootballSeason> footballSeasons = footballSeasonService.queryFootballSeasonsByLeagueMatch(footballLeagueMatchId);
                if(null != footballSeasons && footballSeasons.size() > 0){
                    if(footballSeasons.size() > count){
                        List<FootballSeason> seasons = footballSeasons.subList(0, count);
                        List<String> seasonIds = new ArrayList<String>();
                        for (FootballSeason season : seasons) {
                            seasonIds.add(season.getFootballSeasonId());
                        }
                        footballScores = footballScoreService.queryFootballScoresBySeasonId(seasonIds);
                    }else{
                        footballScores = footballScoreService.queryFootballScoresByLeagueMatchId(footballLeagueMatchId);
                    }
                }
            }
            if (null != footballScores && footballScores.size() > 0) {
                String middleUrl = "/" + LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_STATS_MIDDLE_WORD + "/" +
                        LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_MIDDLE_WORD + "/";
                Set<String> categoryIds = new HashSet<String>();
                for (FootballScore footballScore : footballScores) {
                    //判断是不是0:0如果是就没必要请求详情数据了
                    if(judgeScoreCount(footballScore)){
                        continue;
                    }
                    if (judgeExistDetails(footballScore)) continue;
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
     * 根据类别Id获取request
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    private List<Request> getRequestsByCategoryId(String footballSeasonCategoryId) throws Exception {
        if (StringUtils.isEmpty(footballSeasonCategoryId)){
            return null;
        }
        List<Request> requests = new ArrayList<Request>();
        List<FootballScore> footballScores = footballScoreService.queryFootballScoresBySeasonCategoryId(footballSeasonCategoryId);
        if (null != footballScores && footballScores.size() > 0) {
            String middleUrl = "/" + LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_STATS_MIDDLE_WORD + "/" +
                    LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_MIDDLE_WORD + "/";
            for (FootballScore footballScore : footballScores) {
                //判断是不是0:0如果是就没必要请求详情数据了
                if(judgeScoreCount(footballScore)){
                    continue;
                }
                if(judgeExistDetails(footballScore)){
                    continue;
                }
                String url = LeiDataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + footballScore.getLeidataScoreId();
                Request request = new Request(url);
                request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SCORE, footballScore);
                requests.add(request);
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
            QueueScheduler scheduler = getQueueScheduler();
            spider = Spider.create(footballScoreDetailPageProcessor).setScheduler(scheduler);
            spider.addPipeline(footballScoreDetailAndPlayerPipeline);
            spider.startRequest(requests);
            spider.thread(getThreadCount());
            spider.start();
        }
        return spider;
    }

    /**
     * 判断是否为0：0
     * @param footballScore
     * @return
     */
    private boolean judgeScoreCount(FootballScore footballScore) {
        if(null == footballScore){
            return true;
        }
        //如果是0:0就没必要请求进球数据了
        if(null == footballScore.getHomeScore() || null == footballScore.getAwayScore()){
            return true;
        }
        int scoreCount = getScoreCount(footballScore);
        if(scoreCount == 0){
            return true;
        }
        return false;
    }

    /**
     * 获取总分
     * @param footballScore
     * @return
     */
    private int getScoreCount(FootballScore footballScore) {
        int homeScore = footballScore.getHomeScore().intValue();
        int awayScore = footballScore.getAwayScore().intValue();
        int scoreCount = homeScore + awayScore;
        return  scoreCount;
    }

    private boolean judgeExistDetails(FootballScore footballScore) throws Exception {
        int scoreCount = getScoreCount(footballScore);
        List<FootballScoreDetail> existsScoreDetails = footballScoreDetailService.queryFootballScoreDetailsByScoreId(footballScore.getFootballScoreId());
        if(null != existsScoreDetails && existsScoreDetails.size() > 0){
            //如果已存在正确的详情数据,数据正确就不再继续请求了
            if(existsScoreDetails.size() == scoreCount){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取要爬取的多少个前面赛季的数字
     * @return
     */
    private int getSeasonCount(){
        CoreParameter countParam = null;
        try {
            countParam = coreParameterService.queryCoreParameterByParamCode(CrawlerConstant.CRAW_DETAIL_SEASON_COUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null == countParam || StringUtils.isEmpty(countParam.getParamValue())){
            return 0;
        }else{
            return Integer.parseInt(countParam.getParamValue());
        }
    }
}
