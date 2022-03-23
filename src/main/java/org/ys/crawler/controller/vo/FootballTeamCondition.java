package org.ys.crawler.controller.vo;

import org.ys.common.vo.BasecCondition;

/**
 * 用于前后端传递联赛查询参数的类
 */
public class FootballTeamCondition extends BasecCondition {
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
}
