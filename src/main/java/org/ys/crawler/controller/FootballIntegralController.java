package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.crawler.controller.vo.FootballIntegralCondition;
import org.ys.crawler.model.*;
import org.ys.crawler.pipeLine.FootballIntegralAndTeamPipeline;
import org.ys.crawler.processor.FootballIntegralPageProcessor;
import org.ys.crawler.service.*;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/crawler/footballIntegralController")
@RestController
public class FootballIntegralController extends BaseCrawlerController{
    @Autowired
    private FootballTeamService footballTeamService;
    @Autowired
    private FootballIntegralService footballIntegralService;
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballIntegralPageProcessor footballIntegralPageProcessor;
    @Autowired
    private FootballIntegralAndTeamPipeline footballIntegralPipeline;

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_INTEGRAL_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody FootballIntegralCondition integralCondition){
        if(null == integralCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballIntegral> pageBean = null;
        try {
            String footballLeagueMatchId = integralCondition.getFootballLeagueMatchId();
            String footballSeasonCategoryId = integralCondition.getFootballSeasonCategoryId();
            String footballSeasonId = integralCondition.getFootballSeasonId();
            String cascaderId = integralCondition.getCascaderId();
            FootballIntegralExample example = new FootballIntegralExample();
            FootballIntegralExample.Criteria criteria = example.createCriteria();
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
            pageBean = footballIntegralService.pageFootballIntegralsByExample(example, integralCondition.getPageNum(), integralCondition.getPageSize());
            if(null == pageBean){
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_INTEGRAL_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam String footballIntegralId){
        if(StringUtils.isEmpty(footballIntegralId)){
            return HttpResult.error("参数为空!");
        }
        try {
            FootballIntegral footballIntegral = footballIntegralService.queryFootballIntegralOfFullFieldById(footballIntegralId.trim());
            return HttpResult.ok(footballIntegral);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    /**
     * 爬取所有联赛的积分数据
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_INTEGRAL_CRAW')")
    @GetMapping("/startIntegralCrawler")
    public HttpResult startIntegralCrawler() throws Exception{
        Spider spider = null;
        List<FootballLeagueMatch> leagueMatches = footballLeagueMatchService.queryAll();
        if(null != leagueMatches && leagueMatches.size() > 0){
            List<Request> requests = new ArrayList<Request>();
            for (FootballLeagueMatch leagueMatch : leagueMatches) {
                //杯赛没有积分数据，不爬取
                if(StringUtils.contains(leagueMatch.getFootballLeagueMatchName(),"杯")){
                    continue;
                }
                List<Request> requestList = getRequestsByLeagueMatch(leagueMatch, LeiDataCrawlerConstant.LEIDATA_CRAWLER_INTEGRAL_TYPE);
                if(null != requestList && requestList.size() > 0){
                    requests.addAll(requestList);
                }
            }
            spider = getSpider(requests,footballIntegralPageProcessor,footballIntegralPipeline);
        }
        if(null != spider){
            return HttpResult.ok("积分爬虫启动成功！");
        }else{
            return HttpResult.error("积分爬虫启动失败！");
        }
    }

    /**
     * 爬取指定联赛的积分数据
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_INTEGRAL_CRAW_BY_LEAGUE_MATCH')")
    @GetMapping("/startIntegralCrawlerByLeagueMatch")
    public HttpResult startIntegralCrawlerByLeagueMatch(@RequestParam String footballLeagueMatchId) throws Exception{
        Spider spider = null;
        if(StringUtils.isNotEmpty(footballLeagueMatchId)){
            FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(StringUtils.trim(footballLeagueMatchId));
            if(null != leagueMatch){
                //杯赛没有积分数据，不爬取
                if(StringUtils.contains(leagueMatch.getFootballLeagueMatchName(),"杯")){
                    return HttpResult.ok("杯赛没有积分不爬取！");
                }
            }
            List<Request> requests = getRequestsByLeagueMatch(leagueMatch,null);
            spider = getSpider(requests,footballIntegralPageProcessor,footballIntegralPipeline);
        }
        if(null != spider){
            return HttpResult.ok("本赛季积分爬虫启动成功！");
        }else{
            return HttpResult.error("本联赛积分爬虫启动失败！");
        }
    }

    /**
     * 根据赛季Id爬取积分数据
     * @param footballSeasonId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_INTEGRAL_CRAW_BY_SEASON')")
    @GetMapping("/startIntegralCrawlerBySeason")
    public HttpResult startIntegralCrawlerBySeason(@RequestParam String footballSeasonId) throws Exception{
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
                spider = getSpider(requests,footballIntegralPageProcessor,footballIntegralPipeline);
            }
        }
        if(null != spider){
            return HttpResult.ok("本赛季的积分爬虫启动成功！");
        }else{
            return HttpResult.error("本赛季的积分爬虫启动失败！");
        }
    }

    /**
     * 根据联赛爬取积分数据
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
                    , LeiDataCrawlerConstant.LEIDATA_CRAWLER_INTEGRAL_MIDDLE_WORD);
            Set<String> categoryIds = new HashSet<String>();
            for (FootballSeasonCategory category : seasonCategories) {
                //如果轮数不大于1轮，没有积分数据
                if(category.getRoundCount() <= getIntegralRoundCount()){
                    continue;
                }
                //总爬虫不重复爬取旧赛季类别的数据
                if(StringUtils.isNotEmpty(crawType) && StringUtils.equals(crawType, LeiDataCrawlerConstant.LEIDATA_CRAWLER_INTEGRAL_TYPE)){
                    boolean existsFlag = footballIntegralService.isExistsIntegralByCategoryId(categoryIds, category);
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
     * 根据赛季类别爬取积分数据
     * @param seasonCategory
     * @return
     * @throws Exception
     */
    private List<Request> getRequestsBySeasonCategory(FootballSeasonCategory seasonCategory) throws Exception {
        if(null == seasonCategory){
            return null;
        }
        if(seasonCategory.getRoundCount() <= getIntegralRoundCount()){
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
        //杯赛没有积分数据，不爬取
        if(StringUtils.contains(leagueMatch.getFootballLeagueMatchName(),"杯")){
            return null;
        }
        List<Request> requests = new ArrayList<Request>();
        String middleUrl = leagueMatch.getLeagueMatchUrl().replace(LeiDataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                , LeiDataCrawlerConstant.LEIDATA_CRAWLER_INTEGRAL_MIDDLE_WORD);
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
            if (roundCount > getIntegralRoundCount()) {
                String fullUrl = url + "?r=" + roundCount;
                Request request = new Request(fullUrl);
                request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY, seasonCategory);
                requests.add(request);
            }
        }
    }
}
