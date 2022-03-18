package org.ys.crawler.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.ys.crawler.model.FootballPlayer;
import org.ys.crawler.model.FootballPlayerExample;

@Repository
public interface FootballPlayerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    long countByExample(FootballPlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    int deleteByExample(FootballPlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String footballPlayerId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    int insert(FootballPlayer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    int insertSelective(FootballPlayer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    List<FootballPlayer> selectByExample(FootballPlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    FootballPlayer selectByPrimaryKey(String footballPlayerId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") FootballPlayer record, @Param("example") FootballPlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") FootballPlayer record, @Param("example") FootballPlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(FootballPlayer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cr_football_player
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(FootballPlayer record);
}