package org.ys.crawler.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballLeagueMatchExample;

@Repository
public interface FootballLeagueMatchMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    long countByExample(FootballLeagueMatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    int deleteByExample(FootballLeagueMatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String footballLeagueMatchId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    int insert(FootballLeagueMatch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    int insertSelective(FootballLeagueMatch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    List<FootballLeagueMatch> selectByExample(FootballLeagueMatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    FootballLeagueMatch selectByPrimaryKey(String footballLeagueMatchId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") FootballLeagueMatch record, @Param("example") FootballLeagueMatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") FootballLeagueMatch record, @Param("example") FootballLeagueMatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(FootballLeagueMatch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_league_match
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(FootballLeagueMatch record);
}