package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.crawler.controller.vo.FootballPlayerCondition;
import org.ys.crawler.model.FootballPlayer;
import org.ys.crawler.model.FootballPlayerExample;
import org.ys.crawler.model.FootballTeam;
import org.ys.crawler.model.FootballTeamExample;
import org.ys.crawler.service.FootballPlayerService;
import org.ys.crawler.service.FootballTeamService;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/crawler/footballPlayerController")
@RestController
public class FootballPlayerController {
    @Autowired
    private FootballPlayerService footballPlayerService;
    @Autowired
    private FootballTeamService footballTeamService;


    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_PLAYER_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody FootballPlayerCondition playerCondition){
        if(null == playerCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballPlayer> pageBean = null;
        try {
            String playerName = playerCondition.getFootballPlayerName();
            String playerType = playerCondition.getFootballPlayerType();
            String footballTeamId = playerCondition.getFootballTeamId();
            String teamName = playerCondition.getTeamName();
            FootballPlayerExample example = new FootballPlayerExample();
            FootballPlayerExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(playerName)){
                criteria.andFootballPlayerNameLike("%"+playerName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(playerType)){
                criteria.andFootballPlayerTypeLike("%"+playerType.trim()+"%");
            }
            if(StringUtils.isNotEmpty(teamName)){
                FootballTeamExample teamExample = new FootballTeamExample();
                teamExample.createCriteria().andTeamNameLike("%"+teamName.trim()+"%");
                List<FootballTeam> footballTeams = footballTeamService.queryFootballTeamsByExample(teamExample);
                List<String> footballTeamIds = new ArrayList<String>();
                if(null != footballTeams && footballTeams.size() > 0){
                    for (FootballTeam footballTeam : footballTeams) {
                        footballTeamIds.add(footballTeam.getFootballTeamId());
                    }
                }
                criteria.andFootballTeamIdIn(footballTeamIds);
            }
            if(StringUtils.isNotEmpty(footballTeamId)){
                criteria.andFootballTeamIdEqualTo(footballTeamId.trim());
            }
            pageBean = footballPlayerService.pageFootballPlayersByExample(example, playerCondition.getPageNum(), playerCondition.getPageSize());
            if(null == pageBean){
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_PLAYER_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam String footballPlayerId){
        if(StringUtils.isEmpty(footballPlayerId)){
            return HttpResult.ok();
        }
        try {
            FootballPlayer footballPlayer = footballPlayerService.queryFootballPlayerById(footballPlayerId.trim());
            if(null != footballPlayer){
                FootballTeam footballTeam = footballTeamService.queryFootballTeamById(footballPlayer.getFootballTeamId());
                if(null != footballTeam){
                    footballPlayer.setTeamName(footballTeam.getTeamName());
                }
            }
            return HttpResult.ok(footballPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
