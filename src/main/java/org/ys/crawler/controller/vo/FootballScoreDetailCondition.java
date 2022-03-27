package org.ys.crawler.controller.vo;

import org.ys.common.vo.BasecCondition;

import java.util.Date;

/**
 * 用于前后端传递比分详情数据查询参数的类
 */
public class FootballScoreDetailCondition extends BasecCondition {
    private String footballScoreId;
    private String footballPeriodName;
    private Boolean isHome;
    private String teamName;
    private String footballPlayerName;

    public String getFootballScoreId() {
        return footballScoreId;
    }

    public void setFootballScoreId(String footballScoreId) {
        this.footballScoreId = footballScoreId;
    }

    public String getFootballPeriodName() {
        return footballPeriodName;
    }

    public void setFootballPeriodName(String footballPeriodName) {
        this.footballPeriodName = footballPeriodName;
    }

    public Boolean getHome() {
        return isHome;
    }

    public void setHome(Boolean home) {
        isHome = home;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getFootballPlayerName() {
        return footballPlayerName;
    }

    public void setFootballPlayerName(String footballPlayerName) {
        this.footballPlayerName = footballPlayerName;
    }
}

