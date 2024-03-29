package org.ys.crawler.model;

import java.io.Serializable;
import java.util.Date;

public class FootballScoreDetail implements Serializable {
    private static final long serialVersionUID = 3944707039145299480L;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cr_football_score_detail.football_score_detail_id
     *
     * @mbggenerated
     */
    private String footballScoreDetailId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cr_football_score_detail.football_score_id
     *
     * @mbggenerated
     */
    private String footballScoreId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cr_football_score_detail.goal_football_team_id
     *
     * @mbggenerated
     */
    private String goalFootballTeamId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cr_football_score_detail.goal_time
     *
     * @mbggenerated
     */
    private Date goalTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cr_football_score_detail.is_home
     *
     * @mbggenerated
     */
    private Boolean isHome;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cr_football_score_detail.football_player_id
     *
     * @mbggenerated
     */
    private String footballPlayerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cr_football_score_detail.football_period_name
     *
     * @mbggenerated
     */
    private String footballPeriodName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cr_football_score_detail.leidata_score_id
     *
     * @mbggenerated
     */
    private String leidataScoreId;

    // 非数据库字段,球队名称
    private String teamName;
    // 非数据库字段,球员名称
    private String footballPlayerName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cr_football_score_detail.football_score_detail_id
     *
     * @return the value of cr_football_score_detail.football_score_detail_id
     *
     * @mbggenerated
     */
    public String getFootballScoreDetailId() {
        return footballScoreDetailId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cr_football_score_detail.football_score_detail_id
     *
     * @param footballScoreDetailId the value for cr_football_score_detail.football_score_detail_id
     *
     * @mbggenerated
     */
    public void setFootballScoreDetailId(String footballScoreDetailId) {
        this.footballScoreDetailId = footballScoreDetailId == null ? null : footballScoreDetailId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cr_football_score_detail.football_score_id
     *
     * @return the value of cr_football_score_detail.football_score_id
     *
     * @mbggenerated
     */
    public String getFootballScoreId() {
        return footballScoreId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cr_football_score_detail.football_score_id
     *
     * @param footballScoreId the value for cr_football_score_detail.football_score_id
     *
     * @mbggenerated
     */
    public void setFootballScoreId(String footballScoreId) {
        this.footballScoreId = footballScoreId == null ? null : footballScoreId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cr_football_score_detail.goal_football_team_id
     *
     * @return the value of cr_football_score_detail.goal_football_team_id
     *
     * @mbggenerated
     */
    public String getGoalFootballTeamId() {
        return goalFootballTeamId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cr_football_score_detail.goal_football_team_id
     *
     * @param goalFootballTeamId the value for cr_football_score_detail.goal_football_team_id
     *
     * @mbggenerated
     */
    public void setGoalFootballTeamId(String goalFootballTeamId) {
        this.goalFootballTeamId = goalFootballTeamId == null ? null : goalFootballTeamId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cr_football_score_detail.goal_time
     *
     * @return the value of cr_football_score_detail.goal_time
     *
     * @mbggenerated
     */
    public Date getGoalTime() {
        return goalTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cr_football_score_detail.goal_time
     *
     * @param goalTime the value for cr_football_score_detail.goal_time
     *
     * @mbggenerated
     */
    public void setGoalTime(Date goalTime) {
        this.goalTime = goalTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cr_football_score_detail.is_home
     *
     * @return the value of cr_football_score_detail.is_home
     *
     * @mbggenerated
     */
    public Boolean getIsHome() {
        return isHome;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cr_football_score_detail.is_home
     *
     * @param isHome the value for cr_football_score_detail.is_home
     *
     * @mbggenerated
     */
    public void setIsHome(Boolean isHome) {
        this.isHome = isHome;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cr_football_score_detail.football_player_id
     *
     * @return the value of cr_football_score_detail.football_player_id
     *
     * @mbggenerated
     */
    public String getFootballPlayerId() {
        return footballPlayerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cr_football_score_detail.football_player_id
     *
     * @param footballPlayerId the value for cr_football_score_detail.football_player_id
     *
     * @mbggenerated
     */
    public void setFootballPlayerId(String footballPlayerId) {
        this.footballPlayerId = footballPlayerId == null ? null : footballPlayerId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cr_football_score_detail.football_period_name
     *
     * @return the value of cr_football_score_detail.football_period_name
     *
     * @mbggenerated
     */
    public String getFootballPeriodName() {
        return footballPeriodName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cr_football_score_detail.football_period_name
     *
     * @param footballPeriodName the value for cr_football_score_detail.football_period_name
     *
     * @mbggenerated
     */
    public void setFootballPeriodName(String footballPeriodName) {
        this.footballPeriodName = footballPeriodName == null ? null : footballPeriodName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cr_football_score_detail.leidata_score_id
     *
     * @return the value of cr_football_score_detail.leidata_score_id
     *
     * @mbggenerated
     */
    public String getLeidataScoreId() {
        return leidataScoreId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cr_football_score_detail.leidata_score_id
     *
     * @param leidataScoreId the value for cr_football_score_detail.leidata_score_id
     *
     * @mbggenerated
     */
    public void setLeidataScoreId(String leidataScoreId) {
        this.leidataScoreId = leidataScoreId == null ? null : leidataScoreId.trim();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getFootballPlayerName() {
        return footballPlayerName;
    }

    public void setFootballPlayerName(String footballPlayerName) {
        this.footballPlayerName = footballPlayerName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FootballScoreDetail other = (FootballScoreDetail) that;
        return (this.getFootballScoreDetailId() == null ? other.getFootballScoreDetailId() == null : this.getFootballScoreDetailId().equals(other.getFootballScoreDetailId()))
            && (this.getFootballScoreId() == null ? other.getFootballScoreId() == null : this.getFootballScoreId().equals(other.getFootballScoreId()))
            && (this.getGoalFootballTeamId() == null ? other.getGoalFootballTeamId() == null : this.getGoalFootballTeamId().equals(other.getGoalFootballTeamId()))
            && (this.getGoalTime() == null ? other.getGoalTime() == null : this.getGoalTime().equals(other.getGoalTime()))
            && (this.getIsHome() == null ? other.getIsHome() == null : this.getIsHome().equals(other.getIsHome()))
            && (this.getFootballPlayerId() == null ? other.getFootballPlayerId() == null : this.getFootballPlayerId().equals(other.getFootballPlayerId()))
            && (this.getFootballPeriodName() == null ? other.getFootballPeriodName() == null : this.getFootballPeriodName().equals(other.getFootballPeriodName()))
            && (this.getLeidataScoreId() == null ? other.getLeidataScoreId() == null : this.getLeidataScoreId().equals(other.getLeidataScoreId()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFootballScoreDetailId() == null) ? 0 : getFootballScoreDetailId().hashCode());
        result = prime * result + ((getFootballScoreId() == null) ? 0 : getFootballScoreId().hashCode());
        result = prime * result + ((getGoalFootballTeamId() == null) ? 0 : getGoalFootballTeamId().hashCode());
        result = prime * result + ((getGoalTime() == null) ? 0 : getGoalTime().hashCode());
        result = prime * result + ((getIsHome() == null) ? 0 : getIsHome().hashCode());
        result = prime * result + ((getFootballPlayerId() == null) ? 0 : getFootballPlayerId().hashCode());
        result = prime * result + ((getFootballPeriodName() == null) ? 0 : getFootballPeriodName().hashCode());
        result = prime * result + ((getLeidataScoreId() == null) ? 0 : getLeidataScoreId().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", footballScoreDetailId=").append(footballScoreDetailId);
        sb.append(", footballScoreId=").append(footballScoreId);
        sb.append(", goalFootballTeamId=").append(goalFootballTeamId);
        sb.append(", goalTime=").append(goalTime);
        sb.append(", isHome=").append(isHome);
        sb.append(", footballPlayerId=").append(footballPlayerId);
        sb.append(", footballPeriodName=").append(footballPeriodName);
        sb.append(", leidataScoreId=").append(leidataScoreId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}