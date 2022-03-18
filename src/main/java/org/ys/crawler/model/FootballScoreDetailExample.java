package org.ys.crawler.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FootballScoreDetailExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public FootballScoreDetailExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCTime(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Time> timeList = new ArrayList<java.sql.Time>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                timeList.add(new java.sql.Time(iter.next().getTime()));
            }
            addCriterion(condition, timeList, property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value1.getTime()), new java.sql.Time(value2.getTime()), property);
        }

        public Criteria andFootballScoreDetailIdIsNull() {
            addCriterion("football_score_detail_id is null");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdIsNotNull() {
            addCriterion("football_score_detail_id is not null");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdEqualTo(String value) {
            addCriterion("football_score_detail_id =", value, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdNotEqualTo(String value) {
            addCriterion("football_score_detail_id <>", value, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdGreaterThan(String value) {
            addCriterion("football_score_detail_id >", value, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdGreaterThanOrEqualTo(String value) {
            addCriterion("football_score_detail_id >=", value, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdLessThan(String value) {
            addCriterion("football_score_detail_id <", value, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdLessThanOrEqualTo(String value) {
            addCriterion("football_score_detail_id <=", value, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdLike(String value) {
            addCriterion("football_score_detail_id like", value, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdNotLike(String value) {
            addCriterion("football_score_detail_id not like", value, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdIn(List<String> values) {
            addCriterion("football_score_detail_id in", values, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdNotIn(List<String> values) {
            addCriterion("football_score_detail_id not in", values, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdBetween(String value1, String value2) {
            addCriterion("football_score_detail_id between", value1, value2, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreDetailIdNotBetween(String value1, String value2) {
            addCriterion("football_score_detail_id not between", value1, value2, "footballScoreDetailId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdIsNull() {
            addCriterion("football_score_id is null");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdIsNotNull() {
            addCriterion("football_score_id is not null");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdEqualTo(String value) {
            addCriterion("football_score_id =", value, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdNotEqualTo(String value) {
            addCriterion("football_score_id <>", value, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdGreaterThan(String value) {
            addCriterion("football_score_id >", value, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdGreaterThanOrEqualTo(String value) {
            addCriterion("football_score_id >=", value, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdLessThan(String value) {
            addCriterion("football_score_id <", value, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdLessThanOrEqualTo(String value) {
            addCriterion("football_score_id <=", value, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdLike(String value) {
            addCriterion("football_score_id like", value, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdNotLike(String value) {
            addCriterion("football_score_id not like", value, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdIn(List<String> values) {
            addCriterion("football_score_id in", values, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdNotIn(List<String> values) {
            addCriterion("football_score_id not in", values, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdBetween(String value1, String value2) {
            addCriterion("football_score_id between", value1, value2, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andFootballScoreIdNotBetween(String value1, String value2) {
            addCriterion("football_score_id not between", value1, value2, "footballScoreId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdIsNull() {
            addCriterion("goal_football_team_id is null");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdIsNotNull() {
            addCriterion("goal_football_team_id is not null");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdEqualTo(String value) {
            addCriterion("goal_football_team_id =", value, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdNotEqualTo(String value) {
            addCriterion("goal_football_team_id <>", value, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdGreaterThan(String value) {
            addCriterion("goal_football_team_id >", value, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdGreaterThanOrEqualTo(String value) {
            addCriterion("goal_football_team_id >=", value, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdLessThan(String value) {
            addCriterion("goal_football_team_id <", value, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdLessThanOrEqualTo(String value) {
            addCriterion("goal_football_team_id <=", value, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdLike(String value) {
            addCriterion("goal_football_team_id like", value, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdNotLike(String value) {
            addCriterion("goal_football_team_id not like", value, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdIn(List<String> values) {
            addCriterion("goal_football_team_id in", values, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdNotIn(List<String> values) {
            addCriterion("goal_football_team_id not in", values, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdBetween(String value1, String value2) {
            addCriterion("goal_football_team_id between", value1, value2, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalFootballTeamIdNotBetween(String value1, String value2) {
            addCriterion("goal_football_team_id not between", value1, value2, "goalFootballTeamId");
            return (Criteria) this;
        }

        public Criteria andGoalTimeIsNull() {
            addCriterion("goal_time is null");
            return (Criteria) this;
        }

        public Criteria andGoalTimeIsNotNull() {
            addCriterion("goal_time is not null");
            return (Criteria) this;
        }

        public Criteria andGoalTimeEqualTo(Date value) {
            addCriterionForJDBCTime("goal_time =", value, "goalTime");
            return (Criteria) this;
        }

        public Criteria andGoalTimeNotEqualTo(Date value) {
            addCriterionForJDBCTime("goal_time <>", value, "goalTime");
            return (Criteria) this;
        }

        public Criteria andGoalTimeGreaterThan(Date value) {
            addCriterionForJDBCTime("goal_time >", value, "goalTime");
            return (Criteria) this;
        }

        public Criteria andGoalTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("goal_time >=", value, "goalTime");
            return (Criteria) this;
        }

        public Criteria andGoalTimeLessThan(Date value) {
            addCriterionForJDBCTime("goal_time <", value, "goalTime");
            return (Criteria) this;
        }

        public Criteria andGoalTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCTime("goal_time <=", value, "goalTime");
            return (Criteria) this;
        }

        public Criteria andGoalTimeIn(List<Date> values) {
            addCriterionForJDBCTime("goal_time in", values, "goalTime");
            return (Criteria) this;
        }

        public Criteria andGoalTimeNotIn(List<Date> values) {
            addCriterionForJDBCTime("goal_time not in", values, "goalTime");
            return (Criteria) this;
        }

        public Criteria andGoalTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("goal_time between", value1, value2, "goalTime");
            return (Criteria) this;
        }

        public Criteria andGoalTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCTime("goal_time not between", value1, value2, "goalTime");
            return (Criteria) this;
        }

        public Criteria andIsHomeIsNull() {
            addCriterion("is_home is null");
            return (Criteria) this;
        }

        public Criteria andIsHomeIsNotNull() {
            addCriterion("is_home is not null");
            return (Criteria) this;
        }

        public Criteria andIsHomeEqualTo(Boolean value) {
            addCriterion("is_home =", value, "isHome");
            return (Criteria) this;
        }

        public Criteria andIsHomeNotEqualTo(Boolean value) {
            addCriterion("is_home <>", value, "isHome");
            return (Criteria) this;
        }

        public Criteria andIsHomeGreaterThan(Boolean value) {
            addCriterion("is_home >", value, "isHome");
            return (Criteria) this;
        }

        public Criteria andIsHomeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_home >=", value, "isHome");
            return (Criteria) this;
        }

        public Criteria andIsHomeLessThan(Boolean value) {
            addCriterion("is_home <", value, "isHome");
            return (Criteria) this;
        }

        public Criteria andIsHomeLessThanOrEqualTo(Boolean value) {
            addCriterion("is_home <=", value, "isHome");
            return (Criteria) this;
        }

        public Criteria andIsHomeIn(List<Boolean> values) {
            addCriterion("is_home in", values, "isHome");
            return (Criteria) this;
        }

        public Criteria andIsHomeNotIn(List<Boolean> values) {
            addCriterion("is_home not in", values, "isHome");
            return (Criteria) this;
        }

        public Criteria andIsHomeBetween(Boolean value1, Boolean value2) {
            addCriterion("is_home between", value1, value2, "isHome");
            return (Criteria) this;
        }

        public Criteria andIsHomeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_home not between", value1, value2, "isHome");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdIsNull() {
            addCriterion("football_player_id is null");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdIsNotNull() {
            addCriterion("football_player_id is not null");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdEqualTo(String value) {
            addCriterion("football_player_id =", value, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdNotEqualTo(String value) {
            addCriterion("football_player_id <>", value, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdGreaterThan(String value) {
            addCriterion("football_player_id >", value, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdGreaterThanOrEqualTo(String value) {
            addCriterion("football_player_id >=", value, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdLessThan(String value) {
            addCriterion("football_player_id <", value, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdLessThanOrEqualTo(String value) {
            addCriterion("football_player_id <=", value, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdLike(String value) {
            addCriterion("football_player_id like", value, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdNotLike(String value) {
            addCriterion("football_player_id not like", value, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdIn(List<String> values) {
            addCriterion("football_player_id in", values, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdNotIn(List<String> values) {
            addCriterion("football_player_id not in", values, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdBetween(String value1, String value2) {
            addCriterion("football_player_id between", value1, value2, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPlayerIdNotBetween(String value1, String value2) {
            addCriterion("football_player_id not between", value1, value2, "footballPlayerId");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameIsNull() {
            addCriterion("football_period_name is null");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameIsNotNull() {
            addCriterion("football_period_name is not null");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameEqualTo(String value) {
            addCriterion("football_period_name =", value, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameNotEqualTo(String value) {
            addCriterion("football_period_name <>", value, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameGreaterThan(String value) {
            addCriterion("football_period_name >", value, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameGreaterThanOrEqualTo(String value) {
            addCriterion("football_period_name >=", value, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameLessThan(String value) {
            addCriterion("football_period_name <", value, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameLessThanOrEqualTo(String value) {
            addCriterion("football_period_name <=", value, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameLike(String value) {
            addCriterion("football_period_name like", value, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameNotLike(String value) {
            addCriterion("football_period_name not like", value, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameIn(List<String> values) {
            addCriterion("football_period_name in", values, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameNotIn(List<String> values) {
            addCriterion("football_period_name not in", values, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameBetween(String value1, String value2) {
            addCriterion("football_period_name between", value1, value2, "footballPeriodName");
            return (Criteria) this;
        }

        public Criteria andFootballPeriodNameNotBetween(String value1, String value2) {
            addCriterion("football_period_name not between", value1, value2, "footballPeriodName");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table cr_football_score_detail
     *
     * @mbg.generated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}