package org.ys.analysis.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.analysis.controller.vo.FootballScoreAnalysisCondition;
import org.ys.common.http.HttpResult;
import org.ys.crawler.controller.vo.FootballIntegralCondition;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballScoreExample;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballScoreService;
import org.ys.crawler.service.FootballSeasonCategoryService;

import java.util.*;

@RequestMapping("/analysis/footballScoreAnalysisController")
@RestController
public class FootballScoreAnalysisController {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;
    @Autowired
    private FootballScoreService footballScoreService;

    @PostMapping("/findScoreList")
    public HttpResult findScoreList(@RequestBody FootballScoreAnalysisCondition analysisCondition){
        if(null == analysisCondition){
            return HttpResult.error("查询参数为空");
        }
        List<FootballScore> footballScores = null;
        try {
            footballScores = null;
            String footballLeagueMatchId = analysisCondition.getFootballLeagueMatchId();
            String footballSeasonCategoryId = analysisCondition.getFootballSeasonCategoryId();
            String homeFootballTeamId = analysisCondition.getHomeFootballTeamId();
            String awayFootballTeamId = analysisCondition.getAwayFootballTeamId();
            Integer lastNum = analysisCondition.getLastNum();
            String round = analysisCondition.getRound();
            String[] footballLeagueMatchIds = analysisCondition.getFootballLeagueMatchIds();
            FootballScoreExample example = new FootballScoreExample();
            FootballScoreExample.Criteria criteria = example.createCriteria();
            FootballScoreExample.Criteria criteria2 = null;
            boolean criteria2Flag = false;
            if(StringUtils.isNotEmpty(homeFootballTeamId) && StringUtils.isNotEmpty(awayFootballTeamId)){
                List<String> homeAndAwayTeamIds = new ArrayList<String>();
                homeAndAwayTeamIds.add(StringUtils.trim(homeFootballTeamId));
                homeAndAwayTeamIds.add(StringUtils.trim(awayFootballTeamId));
                criteria.andHomeFootballTeamIdIn(homeAndAwayTeamIds);
                criteria.andAwayFootballTeamIdIn(homeAndAwayTeamIds);
            }else{
                if(StringUtils.isNotEmpty(homeFootballTeamId)){
                    criteria2Flag = true;
                    if(null == criteria2){
                        criteria2 = example.createCriteria();
                    }
                    criteria.andHomeFootballTeamIdEqualTo(StringUtils.trim(homeFootballTeamId));
                    criteria2.andAwayFootballTeamIdEqualTo(StringUtils.trim(homeFootballTeamId));
                }
                if(StringUtils.isNotEmpty(awayFootballTeamId)){
                    criteria2Flag = true;
                    if(null == criteria2){
                        criteria2 = example.createCriteria();
                    }
                    criteria.andHomeFootballTeamIdEqualTo(StringUtils.trim(awayFootballTeamId));
                    criteria2.andAwayFootballTeamIdEqualTo(StringUtils.trim(awayFootballTeamId));
                }
            }
            if(StringUtils.isNotEmpty(footballLeagueMatchId)){
                criteria.andFootballLeagueMatchIdEqualTo(StringUtils.trim(footballLeagueMatchId));
                if(criteria2Flag){
                    criteria2.andFootballLeagueMatchIdEqualTo(StringUtils.trim(footballLeagueMatchId));
                }
            }
            if(null != footballLeagueMatchIds && footballLeagueMatchIds.length > 0){
                ArrayList<String> list = new ArrayList<>();
                for (String leagueMatchId : footballLeagueMatchIds) {
                    list.add(leagueMatchId);
                }
                criteria.andFootballLeagueMatchIdIn(list);
                if(criteria2Flag){
                    criteria2.andFootballLeagueMatchIdIn(list);
                }
            }
            if(StringUtils.isNotEmpty(footballSeasonCategoryId)){
                criteria.andFootballSeasonCategoryIdEqualTo(StringUtils.trim(footballSeasonCategoryId));
                if(criteria2Flag){
                    criteria2.andFootballSeasonCategoryIdEqualTo(StringUtils.trim(footballSeasonCategoryId));
                }
            }
            if(StringUtils.isNotEmpty(round)){
                criteria.andRoundEqualTo(StringUtils.trim(round));
                if(criteria2Flag){
                    criteria2.andRoundEqualTo(StringUtils.trim(round));
                }
            }
            example.setOrderByClause(" match_date desc ");
            if(null != criteria2){
                example.or(criteria2);
            }
            footballScores = footballScoreService.queryFootballScoresByExample(example);
            if(null == footballScores){
                footballScores = new ArrayList<FootballScore>();
            }else{
                if(null != lastNum && lastNum > 0 && footballScores.size() > lastNum){
                    footballScores = footballScores.subList(0,lastNum);
                }
                //填充非数据库字段
                footballScoreService.fillFootballScores(footballScores);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(footballScores);
    }

    @GetMapping("/findFootballLeagueMatches")
    public HttpResult findFootballLeagueMatches(@RequestParam String footballScoreId){
        List<FootballLeagueMatch> leagueMatches = new ArrayList<FootballLeagueMatch>();
        try {
            if(StringUtils.isNotEmpty(footballScoreId)){
                FootballScore footballScore = footballScoreService.queryFootballScoreById(StringUtils.trim(footballScoreId));
                if(null != footballScore){
                    String homeFootballTeamId = footballScore.getHomeFootballTeamId();
                    String awayFootballTeamId = footballScore.getAwayFootballTeamId();
                    FootballScoreExample example = new FootballScoreExample();
                    FootballScoreExample.Criteria criteria1 = example.createCriteria();
                    FootballScoreExample.Criteria criteria2 = example.createCriteria();
                    FootballScoreExample.Criteria criteria3 = example.createCriteria();
                    FootballScoreExample.Criteria criteria4 = example.createCriteria();
                    if(StringUtils.isNotEmpty(homeFootballTeamId)){
                        criteria1.andHomeFootballTeamIdEqualTo(homeFootballTeamId);
                        criteria2.andAwayFootballTeamIdEqualTo(homeFootballTeamId);
                    }
                    if(StringUtils.isNotEmpty(awayFootballTeamId)){
                        criteria3.andHomeFootballTeamIdEqualTo(awayFootballTeamId);
                        criteria4.andAwayFootballTeamIdEqualTo(awayFootballTeamId);
                    }
                    example.or(criteria2);
                    example.or(criteria3);
                    example.or(criteria4);
                    List<String> leagueMatchIds = footballScoreService.selectLeagueMatchesByExample(example);
                    if(null != leagueMatchIds && leagueMatchIds.size() > 0){
                        for (String leagueMatchId : leagueMatchIds) {
                            if(StringUtils.isNotEmpty(leagueMatchId)){
                                FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(leagueMatchId);
                                if(null != leagueMatch){
                                    leagueMatches.add(leagueMatch);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(leagueMatches);
    }

    @GetMapping("/findFootballScore")
    public HttpResult findFootballScore(@RequestParam String footballScoreId){
        if(StringUtils.isEmpty(footballScoreId)){
            return HttpResult.error("参数为空!");
        }
        try {
            FootballScore footballScore = footballScoreService.queryFootballScoreOfFullFieldById(StringUtils.trim(footballScoreId));
            return HttpResult.ok(footballScore);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
