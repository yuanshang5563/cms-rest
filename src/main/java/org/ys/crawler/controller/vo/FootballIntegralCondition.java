package org.ys.crawler.controller.vo;

import org.ys.common.vo.BasecCondition;

import java.util.Date;

/**
 * 用于前后端传递积分数据查询参数的类
 */
public class FootballIntegralCondition extends BasecCondition {
    private String cascaderId;
    private String footballLeagueMatchId;
    private String footballSeasonId;
    private String footballSeasonCategoryId;

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

    public String getCascaderId() {
        return cascaderId;
    }

    public void setCascaderId(String cascaderId) {
        this.cascaderId = cascaderId;
    }
}
