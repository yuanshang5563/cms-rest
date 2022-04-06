package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.crawler.controller.vo.FootballTeamCondition;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballTeam;
import org.ys.crawler.model.FootballTeamExample;
import org.ys.crawler.service.FootballIntegralService;
import org.ys.crawler.service.FootballScoreService;
import org.ys.crawler.service.FootballTeamService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/crawler/FootballTeamController")
@RestController
public class FootballTeamController {
    @Autowired
    private FootballTeamService footballTeamService;
    @Autowired
    private FootballIntegralService footballIntegralService;
    @Autowired
    private FootballScoreService footballScoreService;

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_TEAM_LIST')")
    @PostMapping("/findPage")
    public Object findPage(@RequestBody FootballTeamCondition footballTeamCondition){
        if(null == footballTeamCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballTeam> pageBean = null;
        try {
            String teamName = footballTeamCondition.getTeamName();
            String country = footballTeamCondition.getCountry();
            String cascaderId = footballTeamCondition.getCascaderId();
            String footballLeagueMatchId = footballTeamCondition.getFootballLeagueMatchId();
            String footballSeasonId = footballTeamCondition.getFootballSeasonId();
            String footballSeasonCategoryId = footballTeamCondition.getFootballSeasonCategoryId();
            FootballTeamExample example = new FootballTeamExample();
            FootballTeamExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(teamName)){
                criteria.andTeamNameLike("%"+teamName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(country)){
                criteria.andCountryLike("%"+country.trim()+"%");
            }
            if(StringUtils.isNotEmpty(cascaderId)){
                if(cascaderId.contains("footballLeagueMatchId:")){
                    footballLeagueMatchId = cascaderId.replaceAll("footballLeagueMatchId:","");
                }else if(cascaderId.contains("footballSeasonId:")){
                    footballSeasonId = cascaderId.replaceAll("footballSeasonId:","");
                }else if(cascaderId.contains("footballSeasonCategoryId:")){
                    footballSeasonCategoryId = cascaderId.replaceAll("footballSeasonCategoryId:","");
                }
            }
            //类别，赛季和联赛都不为空，哪个最小用哪个
            List<FootballTeam> teamList = null;
            if(StringUtils.isNotEmpty(footballSeasonCategoryId)){
                teamList = footballIntegralService.queryFootballTeamsBySeasonCategoryId(footballSeasonCategoryId);
            }else{
                if(StringUtils.isNotEmpty(footballSeasonId)){
                    teamList = footballIntegralService.queryFootballTeamsBySeasonId(footballSeasonId);
                }else{
                    if(StringUtils.isNotEmpty(footballLeagueMatchId)){
                        teamList = footballIntegralService.queryFootballTeamsByLeagueMatchId(footballLeagueMatchId);
                    }
                }
            }
            if(null != teamList && teamList.size() > 0){
                List<String> teamIds = new ArrayList<String>();
                for (FootballTeam team : teamList) {
                    teamIds.add(team.getFootballTeamId());
                }
                criteria.andFootballTeamIdIn(teamIds);
            }else{
                List<FootballScore> footballScores = null;
                //没有积分数据，如果类别Id或赛季id或联赛Id不为空从比分数据中找队伍id
                if(StringUtils.isNotEmpty(footballSeasonCategoryId)){
                    footballScores = footballScoreService.queryFootballScoresBySeasonCategoryId(footballSeasonCategoryId);
                }else{
                    if(StringUtils.isNotEmpty(footballSeasonId)){
                        footballScores = footballScoreService.queryFootballScoresBySeasonId(footballSeasonId);
                    }else{
                        if(StringUtils.isNotEmpty(footballLeagueMatchId)){
                            footballScores = footballScoreService.queryFootballScoresByLeagueMatchId(footballLeagueMatchId);
                        }
                    }
                }
                if(null != footballScores && footballScores.size() > 0){
                    Set<String> teamIdSet = new HashSet<String>();
                    for (FootballScore score : footballScores) {
                        teamIdSet.add(score.getHomeFootballTeamId());
                        teamIdSet.add(score.getAwayFootballTeamId());
                    }
                    if(null != teamIdSet && teamIdSet.size() > 0){
                        criteria.andFootballTeamIdIn(new ArrayList<>(teamIdSet));
                    }
                }else{
                    //没有积分数据也没有比分数据
                    if(StringUtils.isNotEmpty(footballLeagueMatchId) || StringUtils.isNotEmpty(footballSeasonId) || StringUtils.isNotEmpty(footballSeasonCategoryId)){
                        criteria.andFootballTeamIdIn(new ArrayList<>());
                    }
                }
            }
            pageBean = footballTeamService.pageFootballTeamsByExample(example, footballTeamCondition.getPageNum(), footballTeamCondition.getPageSize());
            if(null == pageBean){
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_TEAM_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam String footballTeamId){
        if(StringUtils.isEmpty(footballTeamId)){
            return HttpResult.ok();
        }
        try {
            FootballTeam footballTeam = footballTeamService.queryFootballTeamById(footballTeamId.trim());
            return HttpResult.ok(footballTeam);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
