package org.ys.crawler.service;

import org.ys.common.page.PageBean;
import org.ys.crawler.model.*;

import java.util.List;
import java.util.Set;

public interface FootballScoreService {
    /**
     * 根据id查询
     * @param footballScoreId
     * @return
     * @throws Exception
     */
    public FootballScore queryFootballScoreById(String footballScoreId) throws Exception;

    /**
     * 根据Id获取全部属性(包括非数据库属性)
     * @param footballScoreId
     * @return
     * @throws Exception
     */
    public FootballScore queryFootballScoreOfFullFieldById(String footballScoreId) throws Exception;

    /**
     * 保存
     * @param footballScore
     * @throws Exception
     */
    public int save(FootballScore footballScore) throws Exception;

    /**
     * 根据id更新
     * @param footballScore
     * @throws Exception
     */
    public int updateById(FootballScore footballScore) throws Exception;

    /**
     * 根据条件更新
     * @param footballScore
     * @param example
     * @throws Exception
     */
    public int updateByExample(FootballScore footballScore, FootballScoreExample example) throws Exception;

    /**
     * 根据Id删除
     * @param footballScoreId
     * @throws Exception
     */
    public int delFootballScoreById(String footballScoreId) throws Exception;

    /**
     * 根据指定条件查询
     * @param example
     * @return
     * @throws Exception
     */
    public List<FootballScore> queryFootballScoresByExample(FootballScoreExample example) throws Exception;

    /**
     * 查询全部
     * @return
     * @throws Exception
     */
    public List<FootballScore> queryAll() throws Exception;

    /**
     * 根据指定条件分页
     * @param example
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageBean<FootballScore> pageFootballScoresByExample(FootballScoreExample example, int pageNum, int pageSize) throws Exception;

    /**
     *
     * @param leidataScoreId
     * @return
     * @throws Exception
     */
    public FootballScore queryFootballScoreByLeiDataId(String leidataScoreId) throws Exception;

    /**
     * 根据赛季Id查询比分记录
     * @param footballSeasonId
     * @return
     * @throws Exception
     */
    public List<FootballScore> queryFootballScoresBySeasonId(String footballSeasonId) throws Exception;

    /**
     * 查询多个赛季的比分数据
     * @param footballSeasonIds
     * @return
     * @throws Exception
     */
    public List<FootballScore> queryFootballScoresBySeasonId(List<String> footballSeasonIds) throws Exception;

    /**
     * 根据联赛Id查询比分记录
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    public List<FootballScore> queryFootballScoresByLeagueMatchId(String footballLeagueMatchId) throws Exception;

    /**
     * 根据联赛赛季类别Id查询比分记录
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    public List<FootballScore> queryFootballScoresBySeasonCategoryId(String footballSeasonCategoryId) throws Exception;

    /**
     * 查询指定赛季类别的指定轮数比分
     * @param footballSeasonCategoryId
     * @param round
     * @return
     * @throws Exception
     */
    public List<FootballScore> queryFootballScoresBySeasonCategoryIdAndRound(String footballSeasonCategoryId, int round) throws Exception;

    /**
     * 判断该赛季类别是否已经存在正确数量的比分数据
     * @param footballSeasonCategory
     * @return
     * @throws Exception
     */
    public boolean judgeScoresByCategory(FootballSeasonCategory footballSeasonCategory) throws Exception;

    /**
     * 判断该赛季类别的轮数是否已经存在正确数量的比分数据
     * @param footballSeasonCategory
     * @param round
     * @return
     * @throws Exception
     */
    public boolean judgeScoresByCategoryAndRound(FootballSeasonCategory footballSeasonCategory,int round) throws Exception;

    /**
     * 根据score获取比分总和
     * @param footballScore
     * @return
     */
    public int getScoreSumByScore(FootballScore footballScore);

    /**
     * 获取指定赛季类别的总分
     * @param footballSeasonCategory
     * @return
     * @throws Exception
     */
    public int getScoreSumByByCategory(FootballSeasonCategory footballSeasonCategory) throws Exception;

    /**
     * 填充指定比赛比分数据的非数据库字段信息
     * @param footballScores
     * @return
     * @throws Exception
     */
    public List<FootballScore> fillFootballScores(List<FootballScore> footballScores) throws Exception;

    /**
     * 根据指定条件查询联赛Id
     * @param example
     * @return
     * @throws Exception
     */
    public List<String> selectLeagueMatchesByExample(FootballScoreExample example) throws Exception;
}
