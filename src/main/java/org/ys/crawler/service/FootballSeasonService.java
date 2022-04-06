package org.ys.crawler.service;

import org.ys.common.page.PageBean;
import org.ys.common.vo.CascaderTreeItem;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonExample;

import java.util.List;

public interface FootballSeasonService {
    /**
     * 根据id查询
     * @param footballSeasonId
     * @return
     * @throws Exception
     */
    public FootballSeason queryFootballSeasonById(String footballSeasonId) throws Exception;

    /**
     * 保存
     * @param footballSeason
     * @throws Exception
     */
    public int save(FootballSeason footballSeason) throws Exception;

    /**
     * 根据id更新
     * @param footballSeason
     * @throws Exception
     */
    public int updateById(FootballSeason footballSeason) throws Exception;

    /**
     * 根据条件更新
     * @param footballSeason
     * @param example
     * @throws Exception
     */
    public int updateByExample(FootballSeason footballSeason, FootballSeasonExample example) throws Exception;

    /**
     * 根据Id删除
     * @param footballSeasonId
     * @throws Exception
     */
    public int delFootballSeasonById(String footballSeasonId) throws Exception;

    /**
     * 根据指定条件查询
     * @param example
     * @return
     * @throws Exception
     */
    public List<FootballSeason> queryFootballSeasonsByExample(FootballSeasonExample example) throws Exception;

    /**
     * 查询全部
     * @return
     * @throws Exception
     */
    public List<FootballSeason> queryAll() throws Exception;

    /**
     * 根据指定条件分页
     * @param example
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageBean<FootballSeason> pageFootballSeasonsByExample(FootballSeasonExample example, int pageNum, int pageSize) throws Exception;

    /**
     * 根据联赛和赛季名称查询
     * @param footballLeagueMatchId
     * @param footballSeasonName
     * @return
     * @throws Exception
     */
    public FootballSeason queryFootballSeasonByLeagueMatchAndName(String footballLeagueMatchId,String footballSeasonName) throws Exception;

    /**
     * 根据联赛查询
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    public List<FootballSeason> queryFootballSeasonsByLeagueMatch(String footballLeagueMatchId) throws Exception;

    /**
     * 根据联赛id获取赛季的级联数据
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    public List<CascaderTreeItem> findSeasonCascaderItemByLeagueMatchId(String footballLeagueMatchId) throws Exception;

    /**
     * 获取最新赛季
     * @return
     * @throws Exception
     */
    public List<FootballSeason> queryLatestFootballSeasons() throws Exception;

    /**
     * 获取最新赛季的id集合
     * @return
     * @throws Exception
     */
    public List<String> queryLatestFootballSeasonIds() throws Exception;

    /**
     * 获取该联赛的最新赛季
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    public FootballSeason queryLatestFootballSeasonByLeagueMatch(String footballLeagueMatchId) throws Exception;
}
