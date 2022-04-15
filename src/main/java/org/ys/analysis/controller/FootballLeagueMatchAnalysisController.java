package org.ys.analysis.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.analysis.controller.vo.FootballLeagueMatchAnalysisCondition;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.common.vo.CascaderTreeItem;
import org.ys.crawler.controller.BaseCrawlerController;
import org.ys.crawler.controller.vo.FootballLeagueMatchCondition;
import org.ys.crawler.downloader.FootballLeagueMatchDownloader;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballLeagueMatchExample;
import org.ys.crawler.pipeLine.FootballLeagueMatchPipeline;
import org.ys.crawler.processor.LeagueMatchPageProcessor;
import org.ys.crawler.service.FootballLeagueMatchService;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/analysis/footballLeagueMatchAnalysisController")
@RestController
public class FootballLeagueMatchAnalysisController{
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_LEAGUE_MATCH_ANALYSIS_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody FootballLeagueMatchAnalysisCondition analysisCondition){
        if(null == analysisCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballLeagueMatch> pageBean = null;
        try {
            String footballLeagueMatchName = analysisCondition.getFootballLeagueMatchName();
            String regionName = analysisCondition.getRegionName();
            String cascaderId = analysisCondition.getCascaderId();
            FootballLeagueMatchExample example = new FootballLeagueMatchExample();
            FootballLeagueMatchExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(footballLeagueMatchName)){
                criteria.andFootballLeagueMatchNameLike("%"+StringUtils.trim(footballLeagueMatchName)+"%");
            }
            if(StringUtils.isNotEmpty(regionName)){
                criteria.andRegionNameLike("%"+StringUtils.trim(regionName)+"%");
            }
            if(StringUtils.isNotEmpty(cascaderId)){
                if(cascaderId.contains("footballLeagueMatchId:")){
                    String footballLeagueMatchId = cascaderId.replaceAll("footballLeagueMatchId:","");
                    criteria.andFootballLeagueMatchIdEqualTo(StringUtils.trim(footballLeagueMatchId));
                }
            }
            pageBean = footballLeagueMatchService.pageFootballLeagueMatchesByExample(example, analysisCondition.getPageNum(), analysisCondition.getPageSize());
            if(null == pageBean){
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @GetMapping("/find")
    public HttpResult find(@RequestParam String footballLeagueMatchId){
        if(StringUtils.isNotEmpty(footballLeagueMatchId)){
            try {
                FootballLeagueMatch footballLeagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(footballLeagueMatchId.trim());
                return HttpResult.ok(footballLeagueMatch);
            } catch (Exception e) {
                e.printStackTrace();
                return HttpResult.error("程序出现异常");
            }
        }else{
            return HttpResult.error("查询参数为空");
        }

    }
}
