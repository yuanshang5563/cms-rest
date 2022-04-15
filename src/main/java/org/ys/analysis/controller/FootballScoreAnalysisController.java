package org.ys.analysis.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.analysis.controller.vo.FootballScoreAnalysisCondition;
import org.ys.common.http.HttpResult;
import org.ys.crawler.controller.vo.FootballIntegralCondition;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballScoreExample;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballScoreService;
import org.ys.crawler.service.FootballSeasonCategoryService;

import java.util.ArrayList;
import java.util.List;

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
        List<FootballScore> footballScores =  null;
        try {
            String footballLeagueMatchId = analysisCondition.getFootballLeagueMatchId();
            String footballSeasonCategoryId = analysisCondition.getFootballSeasonCategoryId();
            String homeFootballTeamId = analysisCondition.getHomeFootballTeamId();
            String awayFootballTeamId = analysisCondition.getAwayFootballTeamId();
            Integer lastNum = analysisCondition.getLastNum();
            String round = analysisCondition.getRound();
            FootballScoreExample example = new FootballScoreExample();
            FootballScoreExample.Criteria criteria = example.createCriteria();
            FootballScoreExample.Criteria criteria2 = null;
            if(StringUtils.isNotEmpty(footballLeagueMatchId)){
                criteria.andFootballLeagueMatchIdEqualTo(StringUtils.trim(footballLeagueMatchId));
            }
            if(StringUtils.isNotEmpty(footballSeasonCategoryId)){
                criteria.andFootballSeasonCategoryIdEqualTo(StringUtils.trim(footballSeasonCategoryId));
            }
            if(StringUtils.isNotEmpty(homeFootballTeamId)){
                criteria.andHomeFootballTeamIdEqualTo(StringUtils.trim(homeFootballTeamId));
                if(null == criteria2){
                    criteria2 = example.createCriteria();
                }
                criteria2.andAwayFootballTeamIdEqualTo(StringUtils.trim(homeFootballTeamId));
            }
            if(StringUtils.isNotEmpty(awayFootballTeamId)){
                criteria.andAwayFootballTeamIdEqualTo(StringUtils.trim(awayFootballTeamId));
                if(null == criteria2){
                    criteria2 = example.createCriteria();
                }
                criteria2.andHomeFootballTeamIdEqualTo(StringUtils.trim(awayFootballTeamId));
            }
            if(StringUtils.isNotEmpty(round)){
                criteria.andRoundEqualTo(StringUtils.trim(round));
            }
            if(null != criteria2){
                example.or(criteria2);
            }
            example.setOrderByClause(" match_date desc ");
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
}
