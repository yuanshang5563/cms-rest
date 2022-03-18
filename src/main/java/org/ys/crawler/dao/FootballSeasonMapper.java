package org.ys.crawler.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonExample;

@Repository
public interface FootballSeasonMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    long countByExample(FootballSeasonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    int deleteByExample(FootballSeasonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String footballSeasonId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    int insert(FootballSeason record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    int insertSelective(FootballSeason record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    List<FootballSeason> selectByExample(FootballSeasonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    FootballSeason selectByPrimaryKey(String footballSeasonId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") FootballSeason record, @Param("example") FootballSeasonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") FootballSeason record, @Param("example") FootballSeasonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(FootballSeason record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_season
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(FootballSeason record);
}