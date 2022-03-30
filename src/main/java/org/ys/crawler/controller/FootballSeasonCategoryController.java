package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.common.vo.CascaderTreeItem;
import org.ys.crawler.controller.vo.FootballSeasonCategoryCondition;
import org.ys.crawler.model.*;
import org.ys.crawler.pipeLine.FootballRoundPipeline;
import org.ys.crawler.pipeLine.FootballSeasonCategoryPipeline;
import org.ys.crawler.processor.FootballRoundPageProcessor;
import org.ys.crawler.processor.FootballSeasonCategoryPageProcessor;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballSeasonCategoryService;
import org.ys.crawler.service.FootballSeasonService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/crawler/footballSeasonCategoryController")
@RestController
public class FootballSeasonCategoryController extends BaseCrawlerController{
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

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_CATEGORY_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody FootballSeasonCategoryCondition seasonCategoryCondition){
        if(null == seasonCategoryCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballSeasonCategory> pageBean = null;
        try {
            String leagueMatchId = seasonCategoryCondition.getFootballLeagueMatchId();
            String seasonId = seasonCategoryCondition.getFootballSeasonId();
            String categoryName = seasonCategoryCondition.getFootballSeasonCategoryName();
            String seasonName = seasonCategoryCondition.getFootballSeasonName();
            String cascaderId = seasonCategoryCondition.getCascaderId();
            FootballSeasonCategoryExample example = new FootballSeasonCategoryExample();
            FootballSeasonCategoryExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(categoryName)){
                criteria.andFootballSeasonCategoryNameLike("%"+categoryName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(seasonName)){
                FootballSeasonExample seasonExample = new FootballSeasonExample();
                seasonExample.createCriteria().andFootballSeasonNameLike("%"+seasonName.trim()+"%");
                List<FootballSeason> footballSeasons = footballSeasonService.queryFootballSeasonsByExample(seasonExample);
                List<String> footballSeasonIds = getSeasonList(footballSeasons);
                if(null == footballSeasonIds){
                    footballSeasonIds = new ArrayList<String>();
                }
                criteria.andFootballSeasonIdIn(footballSeasonIds);
            }
            if(StringUtils.isNotEmpty(cascaderId)){
                if(cascaderId.contains("footballLeagueMatchId:")){
                    leagueMatchId = cascaderId.replaceAll("footballLeagueMatchId:","");
                }else if(cascaderId.contains("footballSeasonId:")){
                    seasonId = cascaderId.replaceAll("footballSeasonId:","");
                }
            }
            //有小的优先查小的
            if(StringUtils.isNotEmpty(seasonId)){
                criteria.andFootballSeasonIdEqualTo(seasonId.trim());
            }else{
                if(StringUtils.isNotEmpty(leagueMatchId)){
                    criteria.andFootballLeagueMatchIdEqualTo(leagueMatchId);
                }
            }
            pageBean = footballSeasonCategoryService.pageFootballSeasonCategoriesByExample(example, seasonCategoryCondition.getPageNum(), seasonCategoryCondition.getPageSize());
            if(null == pageBean){
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_CATEGORY_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam String footballSeasonCategoryId){
        if(StringUtils.isEmpty(footballSeasonCategoryId)){
            return HttpResult.ok();
        }
        try {
            FootballSeasonCategory seasonCategory = footballSeasonCategoryService.queryFootballSeasonCategoryById(footballSeasonCategoryId.trim());
            if(null != seasonCategory){
                FootballSeason season = footballSeasonService.queryFootballSeasonById(seasonCategory.getFootballSeasonId());
                if(null != season){
                    seasonCategory.setFootballSeasonName(season.getFootballSeasonName());
                    String leagueMatchId = season.getFootballLeagueMatchId();
                    FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(leagueMatchId);
                    if(null != leagueMatch){
                        seasonCategory.setFootballLeagueMatchName(leagueMatch.getFootballLeagueMatchName());
                    }
                }
            }
            return HttpResult.ok(seasonCategory);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    /**
     * 根据赛季id获取赛季类别的级联数据
     * @return
     */
    @GetMapping("/findSeasonCascaderItemBySeasonId")
    public HttpResult findSeasonCascaderItemBySeasonId(@RequestParam String footballSeasonId){
        try {
            List<CascaderTreeItem> cascaderItemList = footballSeasonCategoryService.findSeasonCascaderItemBySeasonId(footballSeasonId);
            return HttpResult.ok(cascaderItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    /**
     * 爬取所有联赛的赛季类别记录
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_CATE_CRAW')")
    @GetMapping("/startSeasonCategoryCrawler")
    public HttpResult startSeasonCategoryCrawler() throws Exception{
        Spider spider =null;
        List<FootballLeagueMatch> leagueMatches =  getStringFootballLeagueMatchList();
        List<Request> requests = new ArrayList<Request>();
        if(null != leagueMatches && leagueMatches.size() > 0){
            for (FootballLeagueMatch leagueMatch : leagueMatches) {
                List<Request> requestList = getCategoryRequests(leagueMatch, LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_CATEGORY_CRAW_TYPE);
                if(null != requestList && requestList.size() > 0){
                    requests.addAll(requestList);
                }
            }
        }
        spider = getCategorySpider(requests);
        if(null != spider){
            return HttpResult.ok("赛季类别爬虫启动成功！");
        }else{
            return HttpResult.error("赛季类别爬虫启动失败！");
        }
    }

    /**
     * 爬取指定联赛的赛季类别记录
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_CATE_CRAW_BY_LEAGUE_MATCH')")
    @GetMapping("/startSeasonCategoryCrawlerByLeagueMatch")
    public HttpResult startSeasonCategoryCrawlerByLeagueMatch(@RequestParam String footballLeagueMatchId) throws Exception{
        FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(footballLeagueMatchId);
        List<Request> requests = getCategoryRequests(leagueMatch,null);
        Spider categorySpider = getCategorySpider(requests);
        if(null != categorySpider){
            return HttpResult.ok("本赛季类别爬虫启动成功！");
        }else{
            return HttpResult.error("本赛季类别爬虫启动失败！");
        }
    }

    /**
     * 爬取指定联赛的赛季类别轮数记录
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_ROUND_CRAW_BY_LEAGUE_MATCH')")
    @GetMapping("/startSeasonRoundCrawlerByLeagueMatch")
    public HttpResult startSeasonRoundCrawlerByLeagueMatch(@RequestParam String footballLeagueMatchId) throws Exception {
        FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(footballLeagueMatchId);
        List<Request> requests = getRoundRequests(leagueMatch,null);
        Spider spider = getRoundSpider(requests);
        if(null != spider){
            return HttpResult.ok("本赛季类别轮数爬虫启动成功！");
        }else{
            return HttpResult.error("本赛季类别轮数爬虫启动失败！");
        }
    }

    /**
     * 爬取所有联赛的赛季类别轮数记录
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_ROUND_CRAW')")
    @GetMapping("/startSeasonRoundCrawler")
    public HttpResult startSeasonRoundCrawler() throws Exception {
        Spider spider =null;
        List<FootballLeagueMatch> leagueMatches =  getStringFootballLeagueMatchList();
        if(null != leagueMatches && leagueMatches.size() > 0){
            List<Request> requests = new ArrayList<Request>();
            for (FootballLeagueMatch leagueMatch : leagueMatches) {
                List<Request> requestList = getRoundRequests(leagueMatch, LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_ROUND_TYPE);
                if(null != requestList && requestList.size() > 0){
                    requests.addAll(requestList);
                }
            }
            spider = getRoundSpider(requests);
        }
        if(null != spider){
            return HttpResult.ok("赛季类别轮数爬虫启动成功！");
        }else{
            return HttpResult.error("赛季类别轮数爬虫启动失败！");
        }
    }

    /**
     * 根据联赛获取要爬取的Request
     * @param leagueMatch
     * @param crawType
     * @return
     * @throws Exception
     */
    private List<Request> getRoundRequests(FootballLeagueMatch leagueMatch,String crawType) throws Exception {
        List<FootballSeasonCategory> seasonCategories = null;
        if(null != leagueMatch){
            seasonCategories = footballSeasonCategoryService.queryFootballSeasonCategoryByLeagueMatchId(leagueMatch.getFootballLeagueMatchId());
        }
        List<Request> requests = new ArrayList<Request>();
        if (null != seasonCategories && seasonCategories.size() > 0) {
            String middleUrl = leagueMatch.getLeagueMatchUrl().replace(LeiDataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                    , LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_ROUND_MIDDLE_WORD);
            for (FootballSeasonCategory seasonCategory : seasonCategories) {
                if(StringUtils.isNotEmpty(crawType) && StringUtils.equals(crawType, LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_ROUND_TYPE)){
                    //如果总爬虫爬过，不要重复爬取
                    if(null != seasonCategory.getRoundCount() && seasonCategory.getRoundCount() != 0){
                        continue;
                    }
                }
                String url = LeiDataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + seasonCategory.getFootballSeasonCategoryUrl();
                Request request = new Request(url);
                request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY, seasonCategory);
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
    private Spider getRoundSpider(List<Request> requests) {
        Spider spider = null;
        if (null !=requests && requests.size() > 0) {
            QueueScheduler scheduler = getQueueScheduler();
            spider = Spider.create(footballRoundPageProcessor).setScheduler(scheduler);
            spider.addPipeline(footballRoundPipeline);
            spider.startRequest(requests);
            spider.thread(getThreadCount());
            spider.start();
        }
        return spider;
    }

    /**
     * 获取所有联赛
     * @return
     * @throws Exception
     */
    private List<FootballLeagueMatch> getStringFootballLeagueMatchList() throws Exception {
        List<FootballLeagueMatch> leagueMatches = footballLeagueMatchService.queryAll();
        return leagueMatches;
    }

    /**
     * 根据request启动赛季类别爬虫
     * @param requests
     * @return
     */
    private Spider getCategorySpider(List<Request> requests) {
        Spider spider = null;
        if(null != requests && requests.size() > 0){
            QueueScheduler scheduler = getQueueScheduler();
            spider = Spider.create(footballSeasonCategoryPageProcessor).setScheduler(scheduler);
            spider.addPipeline(footballSeasonCategoryPipeline);
            spider.startRequest(requests);
            spider.thread(getThreadCount());
            spider.start();
        }
        return spider;
    }

    /**
     * 根据联赛获取赛季类别的request
     * @param leagueMatch
     * @param crawType
     * @return
     * @throws Exception
     */
    private List<Request> getCategoryRequests(FootballLeagueMatch leagueMatch,String crawType) throws Exception {
        List<Request> requests = new ArrayList<Request>();
        List<FootballSeason> footballSeasons = null;
        if(null != leagueMatch){
            footballSeasons = footballSeasonService.queryFootballSeasonsByLeagueMatch(leagueMatch.getFootballLeagueMatchId());
            if(null != footballSeasons && footballSeasons.size() > 0) {
                String middleUrl = leagueMatch.getLeagueMatchUrl().replace(LeiDataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                        , LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_MIDDLE_WORD);
                Set<String> categoryIds = new HashSet<String>();
                for (FootballSeason footballSeason : footballSeasons) {
                    //总爬虫不再重复爬取类别
                    if(StringUtils.isNotEmpty(crawType) && StringUtils.equals(crawType, LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_CATEGORY_CRAW_TYPE)){
                        String seasonId = footballSeason.getFootballSeasonId();
                        if(StringUtils.isNotEmpty(seasonId)){
                            boolean containsFlag = categoryIds.contains(seasonId);
                            if(containsFlag){
                                continue;
                            }else{
                                List<FootballSeasonCategory> seasonCategories = footballSeasonCategoryService.queryFootballSeasonCategoryBySeasonId(seasonId);
                                if(null != seasonCategories && seasonCategories.size() > 0){
                                    categoryIds.add(seasonId);
                                    continue;
                                }
                            }
                        }
                    }
                    String url = LeiDataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl + footballSeason.getFootballSeasonUrl().replace("/", "");
                    Request request = new Request(url);
                    request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON, footballSeason);
                    requests.add(request);
                }
            }
        }
        return requests;
    }

    /**
     * 根据赛季List提取其Id的LIst
     * @param footballSeasons
     * @return
     */
    private List<String> getSeasonList(List<FootballSeason> footballSeasons) {
        if(null != footballSeasons && footballSeasons.size() > 0){
            List<String> footballSeasonIds = new ArrayList<String>();
            for (FootballSeason footballSeason : footballSeasons) {
                footballSeasonIds.add(footballSeason.getFootballSeasonId());
            }
            return footballSeasonIds;
        }else{
            return null;
        }
    }
}
