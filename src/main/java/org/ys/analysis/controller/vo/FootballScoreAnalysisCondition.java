package org.ys.analysis.controller.vo;

import org.ys.common.vo.BasecCondition;

import java.util.List;

/**
 * 用于传递前后分数查询分析参数
 */
public class FootballScoreAnalysisCondition extends BasecCondition {
    private String footballSeasonCategoryId;
    private String round;
    private String homeFootballTeamId;
    private String awayFootballTeamId;
    private String footballLeagueMatchId;
    private Integer lastNum;
    private String[] footballLeagueMatchIds;

    public String getFootballSeasonCategoryId() {
        return footballSeasonCategoryId;
    }

    public void setFootballSeasonCategoryId(String footballSeasonCategoryId) {
        this.footballSeasonCategoryId = footballSeasonCategoryId;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getHomeFootballTeamId() {
        return homeFootballTeamId;
    }

    public void setHomeFootballTeamId(String homeFootballTeamId) {
        this.homeFootballTeamId = homeFootballTeamId;
    }

    public String getAwayFootballTeamId() {
        return awayFootballTeamId;
    }

    public void setAwayFootballTeamId(String awayFootballTeamId) {
        this.awayFootballTeamId = awayFootballTeamId;
    }

    public String getFootballLeagueMatchId() {
        return footballLeagueMatchId;
    }

    public void setFootballLeagueMatchId(String footballLeagueMatchId) {
        this.footballLeagueMatchId = footballLeagueMatchId;
    }

    public Integer getLastNum() {
        return lastNum;
    }

    public void setLastNum(Integer lastNum) {
        this.lastNum = lastNum;
    }

    public String[] getFootballLeagueMatchIds() {
        return footballLeagueMatchIds;
    }

    public void setFootballLeagueMatchIds(String[] footballLeagueMatchIds) {
        this.footballLeagueMatchIds = footballLeagueMatchIds;
    }
}
