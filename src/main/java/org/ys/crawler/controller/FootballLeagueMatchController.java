package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.crawler.controller.vo.FootballLeagueMatchCondition;
import org.ys.crawler.downloader.FootballLeagueMatchDownloader;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballLeagueMatchExample;
import org.ys.crawler.pipeLine.FootballLeagueMatchPipeline;
import org.ys.crawler.processor.LeagueMatchPageProcessor;
import org.ys.crawler.service.FootballLeagueMatchService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/crawler/footballLeagueMatchController")
@RestController
public class FootballLeagueMatchController extends BaseCrawlerController{
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private LeagueMatchPageProcessor leagueMatchPageProcessor;
    @Autowired
    private FootballLeagueMatchPipeline footballLeagueMatchPipeline;
    @Autowired
    private FootballLeagueMatchDownloader footballLeagueMatchDownloader;

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_LEAGUE_MATCH_CRAW')")
    @GetMapping("/startLeagueMatchCrawler")
    public HttpResult startLeagueMatchCrawler(){
        Spider spider = null;
        try {
            QueueScheduler scheduler = new QueueScheduler();
            scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
            Request request = new Request(LeiDataCrawlerConstant.LEIDATA_CRAWLER_STATS_URL);
            spider = Spider.create(leagueMatchPageProcessor);
            spider.setDownloader(footballLeagueMatchDownloader);
            spider.addPipeline(footballLeagueMatchPipeline);
            spider.addRequest(request);
            spider.setScheduler(scheduler);
            spider.thread(getThreadCount());
            spider.start();
            return HttpResult.ok("联赛爬虫启动成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("联赛爬虫启动失败！");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_LEAGUE_MATCH_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody FootballLeagueMatchCondition leagueMatchCondition){
        if(null == leagueMatchCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballLeagueMatch> pageBean = null;
        try {
            String footballLeagueMatchName = leagueMatchCondition.getFootballLeagueMatchName();
            String regionName = leagueMatchCondition.getRegionName();
            FootballLeagueMatchExample example = new FootballLeagueMatchExample();
            FootballLeagueMatchExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(footballLeagueMatchName)){
                criteria.andFootballLeagueMatchNameLike("%"+footballLeagueMatchName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(regionName)){
                criteria.andRegionNameLike("%"+regionName.trim()+"%");
            }
            pageBean = footballLeagueMatchService.pageFootballLeagueMatchesByExample(example, leagueMatchCondition.getPageNum(), leagueMatchCondition.getPageSize());
            if(null == pageBean){
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_LEAGUE_MATCH_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam String footballLeagueMatchId){
        if(StringUtils.isEmpty(footballLeagueMatchId)){
            return HttpResult.ok();
        }
        try {
            FootballLeagueMatch footballLeagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(footballLeagueMatchId.trim());
            return HttpResult.ok(footballLeagueMatch);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_LEAGUE_MATCH_LIST_ALL')")
    @GetMapping("/findAllLeagueMatch")
    public HttpResult findAllLeagueMatch(){
        try {
            Map<String,List<FootballLeagueMatch>> leagueMatchMap = new HashMap<String,List<FootballLeagueMatch>>();
            List<FootballLeagueMatch> leagueMatches = footballLeagueMatchService.queryAll();
            if(null != leagueMatches && leagueMatches.size() > 0){
                for (FootballLeagueMatch leagueMatch : leagueMatches) {
                    List<FootballLeagueMatch> matchList = leagueMatchMap.get(leagueMatch.getRegionName());
                    if(null == matchList){
                        matchList = new ArrayList<FootballLeagueMatch>();
                        leagueMatchMap.put(leagueMatch.getRegionName(),matchList);
                    }
                    matchList.add(leagueMatch);
                }
            }
            return HttpResult.ok(leagueMatchMap);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
