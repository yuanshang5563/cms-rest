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

import java.util.ArrayList;
import java.util.List;

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
            return HttpResult.error("参数为空!");
        }
        try {
            FootballScoreDetail detail = footballScoreDetailService.queryFootballScoreDetailOfFullFieldById(footballScoreDetailId.trim());
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
                List<Request> requestList = getRequestsByLeagueMatchId(leagueMatch.getFootballLeagueMatchId());
                if(null != requestList && requestList.size() > 0){
                    requests.addAll(requestList);
                }
            }
        }
        Spider spider = getSpider(requests,footballScoreDetailPageProcessor,footballScoreDetailAndPlayerPipeline);
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
        List<Request> requests = getRequestsByLeagueMatchId(footballLeagueMatchId);
        Spider spider = getSpider(requests,footballScoreDetailPageProcessor,footballScoreDetailAndPlayerPipeline);
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
                    List<Request> requestList = getRequestsByCategory(category);
                    if(null != requestList && requestList.size() > 0){
                        requests.addAll(requestList);
                    }
                }
                spider =getSpider(requests,footballScoreDetailPageProcessor,footballScoreDetailAndPlayerPipeline);
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
        FootballSeasonCategory category = footballSeasonCategoryService.queryFootballSeasonCategoryById(footballSeasonCategoryId);
        List<Request> requests = getRequestsByCategory(category);
        spider = getSpider(requests,footballScoreDetailPageProcessor,footballScoreDetailAndPlayerPipeline);
        if(null != spider){
            return HttpResult.ok("本赛季的比分详情爬虫启动成功！");
        }else{
            return HttpResult.error("本赛季的比分详情批量爬虫启动失败！");
        }
    }

    /**
     * 根据联赛Id获取要爬取的request
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    private List<Request> getRequestsByLeagueMatchId(String footballLeagueMatchId) throws Exception {
        List<Request> requests = null;
        if(StringUtils.isNotEmpty(footballLeagueMatchId)){
            //如果指定了爬取联赛前几个赛季的详情数据，按要求来
            int count = getSeasonCount();
            if(count == 0){
                List<FootballScore>  footballScores = footballScoreService.queryFootballScoresByLeagueMatchId(footballLeagueMatchId);
                requests = getRequestsByScoreList(footballScores);
            }else{
                requests = new ArrayList<Request>();
                //先找赛季类别，通过赛季类别来爬取
                List<FootballSeason> footballSeasons = footballSeasonService.queryFootballSeasonsByLeagueMatch(footballLeagueMatchId);
                if(null != footballSeasons && footballSeasons.size() > 0){
                    List<String> seasonIds = new ArrayList<String>();
                    if(footballSeasons.size() > count){
                        List<FootballSeason> seasons = footballSeasons.subList(0, count);
                        for (FootballSeason season : seasons) {
                            seasonIds.add(season.getFootballSeasonId());
                        }

                    }else{
                        //联赛少于指定爬取的赛季数量，那么就全部爬取
                        for (FootballSeason footballSeason : footballSeasons) {
                            seasonIds.add(footballSeason.getFootballSeasonId());
                        }
                    }
                    //获取赛季类别
                    List<FootballSeasonCategory> categories = footballSeasonCategoryService.queryFootballSeasonCategoryBySeasonIds(seasonIds);
                    if(null != categories && categories.size() > 0){
                        for (FootballSeasonCategory category : categories) {
                            List<Request> requestList = getRequestsByCategory(category);
                            if(null != requestList && requestList.size() > 0){
                                requests.addAll(requestList);
                            }
                        }
                    }
                }
            }

        }
        return requests;
    }

    /**
     * 根据类别Id获取request
     * @param footballSeasonCategory
     * @return
     * @throws Exception
     */
    private List<Request> getRequestsByCategory(FootballSeasonCategory footballSeasonCategory) throws Exception {
        if (null == footballSeasonCategory){
            return null;
        }
        //判断是否可以重复爬取
        boolean againFlag = this.getCrawlerAgainFlag();
        if(!againFlag){
            boolean detailsFlag = footballScoreDetailService.judgeDetailsByCategory(footballSeasonCategory);
            if(detailsFlag){
                return null;
            }
        }
        List<FootballScore> footballScores = footballScoreService.queryFootballScoresBySeasonCategoryId(footballSeasonCategory.getFootballSeasonCategoryId());
        List<Request> requests = getRequestsByScoreList(footballScores);
        return requests;
    }

    /**
     * 根据比分数据获取请求list
     * @param footballScores
     * @return
     * @throws Exception
     */
    private List<Request> getRequestsByScoreList(List<FootballScore> footballScores) throws Exception {
        if (null != footballScores && footballScores.size() > 0) {
            List<Request> requests = new ArrayList<Request>();
            String middleUrl = "/" + LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_STATS_MIDDLE_WORD + "/" +
                    LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_MIDDLE_WORD + "/";
            for (FootballScore footballScore : footballScores) {
                //判断是不是0:0如果是就没必要请求详情数据了
                if (judgeScoreCount(footballScore)) {
                    continue;
                }
                boolean existDetailFlag = footballScoreDetailService.judgeDetailsByScore(footballScore);
                if (existDetailFlag) {
                    continue;
                }
                String url = LeiDataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + footballScore.getLeidataScoreId();
                Request request = new Request(url);
                request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SCORE, footballScore);
                requests.add(request);
            }
            return requests;
        } else {
            return null;
        }
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
        int scoreCount = footballScoreService.getScoreSumByScore(footballScore);
        if(scoreCount == 0){
            return true;
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
