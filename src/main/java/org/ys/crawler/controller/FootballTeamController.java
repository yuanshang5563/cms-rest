package org.ys.crawler.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.crawler.controller.vo.FootballTeamCondition;
import org.ys.crawler.model.FootballTeam;
import org.ys.crawler.model.FootballTeamExample;
import org.ys.crawler.service.FootballTeamService;

import java.util.ArrayList;

@RequestMapping("/crawler/FootballTeamController")
@RestController
public class FootballTeamController {
    @Autowired
    private FootballTeamService footballTeamService;

    @PreAuthorize("hasAuthority('ROLE_FOOTBALL_TEAM_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody FootballTeamCondition footballTeamCondition){
        if(null == footballTeamCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<FootballTeam> pageBean = null;
        try {
            String teamName = footballTeamCondition.getTeamName();
            String country = footballTeamCondition.getCountry();
            FootballTeamExample example = new FootballTeamExample();
            FootballTeamExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(teamName)){
                criteria.andTeamNameLike("%"+teamName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(country)){
                criteria.andCountryLike("%"+country.trim()+"%");
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
