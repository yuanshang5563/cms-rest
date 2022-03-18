package org.ys.crawler.controller.vo;

import org.ys.common.vo.BasecCondition;

/**
 * 用于前后端传递联赛查询参数的类
 */
public class FootballLeagueMatchCondition extends BasecCondition {
    private String footballLeagueMatchName;
    private String regionName;

    public String getFootballLeagueMatchName() {
        return footballLeagueMatchName;
    }

    public void setFootballLeagueMatchName(String footballLeagueMatchName) {
        this.footballLeagueMatchName = footballLeagueMatchName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
