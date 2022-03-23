package org.ys.crawler.controller.vo;

import org.ys.common.vo.BasecCondition;

import java.util.Date;

/**
 * 用于前后端传递联赛赛季查询参数的类
 */
public class FootballSeasonCondition extends BasecCondition {
    private String leagueMatchId;
    private String footballSeasonName;
    private Date queryDate;

    public String getFootballSeasonName() {
        return footballSeasonName;
    }

    public void setFootballSeasonName(String footballSeasonName) {
        this.footballSeasonName = footballSeasonName;
    }

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public String getLeagueMatchId() {
        return leagueMatchId;
    }

    public void setLeagueMatchId(String leagueMatchId) {
        this.leagueMatchId = leagueMatchId;
    }
}
