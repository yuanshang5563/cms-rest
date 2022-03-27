package org.ys.crawler.controller.vo;

import org.ys.common.vo.BasecCondition;

import java.util.Date;

/**
 * 用于前后端传递比分数据查询参数的类
 */
public class FootballScoreCondition extends BasecCondition {
    private String footballLeagueMatchId;
    private String footballSeasonId;
    private String footballSeasonCategoryId;
    private Date matchDate;
    private String round;

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

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }
}
