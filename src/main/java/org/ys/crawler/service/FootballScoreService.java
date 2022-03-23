package org.ys.crawler.service;

import org.ys.common.page.PageBean;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballLeagueMatchExample;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballScoreExample;

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
     * 判断赛季类别是否已经存在比分数据
     * @param categoryIds 减少重复查询的set
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    public boolean isExistsScoreByCategoryId(Set<String> categoryIds, String footballSeasonCategoryId) throws Exception;
}
