package org.ys.crawler.controller.vo;

import org.ys.common.vo.BasecCondition;

/**
 * 用于前后端传递球队查询参数的类
 */
public class FootballTeamCondition extends BasecCondition {
    private String cascaderId;
    private String footballLeagueMatchId;
    private String footballSeasonId;
    private String footballSeasonCategoryId;
    private String teamName;
    private String country;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCascaderId() {
        return cascaderId;
    }

    public void setCascaderId(String cascaderId) {
        this.cascaderId = cascaderId;
    }

    public String getFootballLeagueMatchId() {
        return footballLeagueMatchId;
    }

    public void setFootballLeagueMatchId(String footballLeagueMatchId) {
        this.footballLeagueMatchId = footballLeagueMatchId;
    }

    public String getFootballSeasonId() {
        return footballSeasonId;
    }

    public void setFootballSeasonId(String footballSeasonId) {
        this.footballSeasonId = footballSeasonId;
    }

    public String getFootballSeasonCategoryId() {
        return footballSeasonCategoryId;
    }

    public void setFootballSeasonCategoryId(String footballSeasonCategoryId) {
        this.footballSeasonCategoryId = footballSeasonCategoryId;
    }
}
