package org.ys.analysis.controller.vo;

import org.ys.common.vo.BasecCondition;

/**
 * 用于传递前后联赛查询分析参数
 */
public class FootballLeagueMatchAnalysisCondition extends BasecCondition {
    private String footballLeagueMatchName;
    private String regionName;
    private String cascaderId;

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

    public String getCascaderId() {
        return cascaderId;
    }

    public void setCascaderId(String cascaderId) {
        this.cascaderId = cascaderId;
    }
}
