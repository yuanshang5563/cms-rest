package org.ys.crawler.service;

import org.ys.common.page.PageBean;
import org.ys.crawler.model.*;

import java.util.List;
import java.util.Set;

public interface FootballIntegralService {
    /**
     * 根据id查询
     * @param footballIntegralId
     * @return
     * @throws Exception
     */
    public FootballIntegral queryFootballIntegralById(String footballIntegralId) throws Exception;

    /**
     * 根据Id获取全部属性(包括非数据库属性)积分数据
     * @param footballIntegralId
     * @return
     * @throws Exception
     */
    public FootballIntegral queryFootballIntegralOfFullFieldById(String footballIntegralId) throws Exception;

    /**
     * 保存
     * @param footballIntegral
     * @throws Exception
     */
    public int save(FootballIntegral footballIntegral) throws Exception;

    /**
     * 根据id更新
     * @param footballIntegral
     * @throws Exception
     */
    public int updateById(FootballIntegral footballIntegral) throws Exception;

    /**
     * 根据条件更新
     * @param footballIntegral
     * @param example
     * @throws Exception
     */
    public int updateByExample(FootballIntegral footballIntegral, FootballIntegralExample example) throws Exception;

    /**
     * 根据Id删除
     * @param footballIntegralId
     * @throws Exception
     */
    public int delFootballIntegralById(String footballIntegralId) throws Exception;

    /**
     * 根据指定条件查询
     * @param example
     * @return
     * @throws Exception
     */
    public List<FootballIntegral> queryFootballIntegralsByExample(FootballIntegralExample example) throws Exception;

    /**
     * 查询全部
     * @return
     * @throws Exception
     */
    public List<FootballIntegral> queryAll() throws Exception;

    /**
     * 根据指定条件分页
     * @param example
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageBean<FootballIntegral> pageFootballIntegralsByExample(FootballIntegralExample example, int pageNum, int pageSize) throws Exception;

    /**
     * 根据指定leiDataIntegralId查询
     * @param leiDataIntegralId
     * @return
     * @throws Exception
     */
    public FootballIntegral queryFootballIntegralByLeiDataId(String leiDataIntegralId) throws Exception;

    /**
     * 判断赛季类别是否已经存在积分数据
     * @param categoryIds 减少重复查询的set
     * @param category
     * @return
     * @throws Exception
     */
    public boolean isExistsIntegralByCategoryId(Set<String> categoryIds, FootballSeasonCategory category) throws Exception;

    /**
     * 根据联赛Id查询积分记录
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    public List<FootballIntegral> queryFootballIntegralsByLeagueMatchId(String footballLeagueMatchId) throws Exception;

    /**
     * 根据联赛赛季Id查询积分记录
     * @param footballSeasonId
     * @return
     * @throws Exception
     */
    public List<FootballIntegral> queryFootballIntegralsBySeasonId(String footballSeasonId) throws Exception;

    /**
     * 根据联赛赛季类别Id查询积分记录
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    public List<FootballIntegral> queryFootballIntegralsBySeasonCategoryId(String footballSeasonCategoryId) throws Exception;

    /**
     * 根据赛季类别Id查找球队
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    public List<FootballTeam> queryFootballTeamsBySeasonCategoryId(String footballSeasonCategoryId) throws Exception;

    /**
     * 根据赛季Id查找球队
     * @param footballSeasonId
     * @return
     * @throws Exception
     */
    public List<FootballTeam> queryFootballTeamsBySeasonId(String footballSeasonId) throws Exception;

    /**
     * 根据联赛Id查找球队
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    public List<FootballTeam> queryFootballTeamsByLeagueMatchId(String footballLeagueMatchId) throws Exception;

    /**
     * 根据赛季类别id获取该类别比赛每轮应赛场数
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    public int queryRoundScoreCountBySeasonCategoryId(String footballSeasonCategoryId) throws Exception;

    /**
     * 填充指定积分数据的非数据库字段信息
     * @param footballIntegrals
     * @return
     * @throws Exception
     */
    public List<FootballIntegral> fillFootballIntegrals(List<FootballIntegral> footballIntegrals) throws Exception;
}
