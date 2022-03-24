package org.ys.crawler.controller.vo;

import org.ys.common.vo.BasecCondition;

import java.util.Date;

/**
 * 用于前后端传递联赛赛季查询参数的类
 */
public class FootballSeasonCategoryCondition extends BasecCondition {
    private String footballLeagueMatchId;
    private String footballSeasonId;
    private String footballSeasonCategoryName;
    private String footballSeasonName;

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

    public String getFootballSeasonCategoryName() {
        return footballSeasonCategoryName;
    }

    public void setFootballSeasonCategoryName(String footballSeasonCategoryName) {
        this.footballSeasonCategoryName = footballSeasonCategoryName;
    }

    public String getFootballSeasonName() {
        return footballSeasonName;
    }

    public void setFootballSeasonName(String footballSeasonName) {
        this.footballSeasonName = footballSeasonName;
    }
}
