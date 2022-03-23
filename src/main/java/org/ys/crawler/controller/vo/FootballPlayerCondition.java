package org.ys.crawler.controller.vo;

import org.ys.common.vo.BasecCondition;

/**
 * 用于前后端传递球员查询参数的类
 */
public class FootballPlayerCondition extends BasecCondition {
    private String footballPlayerName;
    private String footballPlayerType;
    private String footballTeamId;
    private String teamName;

    public String getFootballPlayerName() {
        return footballPlayerName;
    }

    public void setFootballPlayerName(String footballPlayerName) {
        this.footballPlayerName = footballPlayerName;
    }

    public String getFootballPlayerType() {
        return footballPlayerType;
    }

    public void setFootballPlayerType(String footballPlayerType) {
        this.footballPlayerType = footballPlayerType;
    }

    public String getFootballTeamId() {
        return footballTeamId;
    }

    public void setFootballTeamId(String footballTeamId) {
        this.footballTeamId = footballTeamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
