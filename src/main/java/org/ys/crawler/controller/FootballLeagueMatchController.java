package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.constant.LeidataCrawlerConstant;
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

@RequestMapping("/crawler/footballLeagueMatchController")
@RestController
public class FootballLeagueMatchController {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private LeagueMatchPageProcessor leagueMatchPageProcessor;
    @Autowired
    private FootballLeagueMatchPipeline footballLeagueMatchPipeline;
    @Autowired
    private FootballLeagueMatchDownloader footballLeagueMatchDownloader;

    @GetMapping("/startLeagueMatchCrawler")
    public HttpResult startLeagueMatchCrawler(){
        QueueScheduler scheduler = new QueueScheduler();
        scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
        Request request = new Request(LeidataCrawlerConstant.LEIDATA_CRAWLER_STATS_URL);
        System.out.println("------------1---------");
        Spider.create(leagueMatchPageProcessor)
                .setDownloader(footballLeagueMatchDownloader)
                .addPipeline(footballLeagueMatchPipeline)
                .addRequest(request)
                .setScheduler(scheduler)
                .thread(1)
                .start();
        System.out.println("------------2---------");
        return HttpResult.ok("执行成功！");
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
}
