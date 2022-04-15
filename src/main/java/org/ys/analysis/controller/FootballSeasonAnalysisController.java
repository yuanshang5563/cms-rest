package org.ys.analysis.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.http.HttpResult;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballSeasonService;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/analysis/footballSeasonAnalysisController")
@RestController
public class FootballSeasonAnalysisController {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballSeasonService footballSeasonService;

    @GetMapping("/findSeasonList")
    public HttpResult findSeasonList(@RequestParam String footballLeagueMatchId){
        List<FootballSeason> footballSeasons = null;
        if(StringUtils.isEmpty(footballLeagueMatchId)){
            return HttpResult.error("查询参数为空");
        }
        try {
            footballSeasons = footballSeasonService.queryFootballSeasonsByLeagueMatch(footballLeagueMatchId);
            if(null == footballSeasons){
                footballSeasons = new ArrayList<FootballSeason>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(footballSeasons);
    }
}
