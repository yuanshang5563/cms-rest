package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.crawler.controller.vo.FootballSeasonCondition;
import org.ys.crawler.model.*;
import org.ys.crawler.pipeLine.FootballSeasonPipeline;
import org.ys.crawler.processor.FootballSeasonPageProcessor;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballSeasonService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/crawler/footballSeasonController")
@RestController
public class FootballSeasonController {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Autowired
    private FootballSeasonPageProcessor footballSeasonPageProcessor;
    @Autowired
    private FootballSeasonPipeline footballSeasonPipeline;

    /**
     * 爬取所有联赛的赛季数据
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_CRAW')")
    @GetMapping("/startSeasonCrawler")
    public HttpResult startSeasonCrawler() throws Exception{
        List<FootballLeagueMatch> matches = footballLeagueMatchService.queryAll();
        List<Request> requests = new ArrayList<Request>();
        if(null != matches && matches.size() > 0){
            for (FootballLeagueMatch match : matches) {
                if(StringUtils.isEmpty(match.getLeagueMatchUrl())){
                    continue;
                }
                String middleUrl = match.getLeagueMatchUrl().replace(LeiDataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                , LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_MIDDLE_WORD);
                String url = LeiDataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl;
                Request request = new Request(url);
                request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_LEAGUE_MATCH_ID,match.getFootballLeagueMatchId());
                requests.add(request);
            }
        }
        if(requests.size() > 0){
            QueueScheduler scheduler = new QueueScheduler();
            scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
            Spider spider = Spider.create(footballSeasonPageProcessor).setScheduler(scheduler);
            spider.addPipeline(footballSeasonPipeline);
            spider.startRequest(requests);
            spider.thread(1);
            spider.start();
            return HttpResult.ok("赛季爬虫启动成功！");
        }else{
            return HttpResult.error("赛季爬虫启动失败！");
        }
    }

    /**
     * 爬取指定联赛的赛季数据
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_CRAW_BY_LEAGUE_MATCH')")
    @GetMapping("/startSeasonCrawlerByLeagueMatch")
    public HttpResult startSeasonCrawlerByLeagueMatch(@RequestParam String footballLeagueMatchId) throws Exception{
        FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(footballLeagueMatchId);
        if(null != leagueMatch){
            String middleUrl = leagueMatch.getLeagueMatchUrl().replace(LeiDataCrawlerConstant.LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD
                    , LeiDataCrawlerConstant.LEIDATA_CRAWLER_SEASON_MIDDLE_WORD);
            String url = LeiDataCrawlerConstant.LEIDATA_CRAWLER_API_URL + middleUrl;
            Request request = new Request(url);
            request.putExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_LEAGUE_MATCH_ID,leagueMatch.getFootballLeagueMatchId());
            Spider spider = Spider.create(footballSeasonPageProcessor);
            spider.addPipeline(footballSeasonPipeline);
            spider.addRequest(request);
            spider.thread(1);
            spider.start();
            return HttpResult.ok(leagueMatch.getFootballLeagueMatchName()+"赛季爬虫启动成功！");
        }else{
            return HttpResult.error("赛季爬虫启动失败！");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody FootballSeasonCondition seasonCondition){
        if(null == seasonCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballSeason> pageBean = null;
        try {
            String seasonName = seasonCondition.getFootballSeasonName();
            String leagueMatchId = seasonCondition.getLeagueMatchId();
            Date queryDate = seasonCondition.getQueryDate();
            FootballSeasonExample example = new FootballSeasonExample();
            FootballSeasonExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(seasonName)){
                criteria.andFootballSeasonNameLike("%"+seasonName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(leagueMatchId)){
                criteria.andFootballLeagueMatchIdEqualTo(leagueMatchId.trim());
            }
            if(null != queryDate){
                criteria.andSeasonBeginDateGreaterThanOrEqualTo(queryDate);
                criteria.andSeasonEndDateLessThanOrEqualTo(queryDate);
            }
            pageBean = footballSeasonService.pageFootballSeasonsByExample(example, seasonCondition.getPageNum(), seasonCondition.getPageSize());
            if(null == pageBean){
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_SEASON_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam String footballSeasonId){
        if(StringUtils.isEmpty(footballSeasonId)){
            return HttpResult.ok();
        }
        try {
            FootballSeason footballSeason = footballSeasonService.queryFootballSeasonById(footballSeasonId.trim());
            if(null != footballSeason){
                FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(footballSeason.getFootballLeagueMatchId());
                if(null != leagueMatch){
                    footballSeason.setFootballLeagueMatchName(leagueMatch.getFootballLeagueMatchName());
                }
            }
            return HttpResult.ok(footballSeason);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
