package org.ys.crawler.service;

import org.ys.common.page.PageBean;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballLeagueMatchExample;

import java.util.List;

public interface FootballLeagueMatchService {
    /**
     * 根据id查询
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    public FootballLeagueMatch queryFootballLeagueMatchById(String footballLeagueMatchId) throws Exception;

    /**
     * 保存
     * @param footballLeagueMatch
     * @throws Exception
     */
    public int save(FootballLeagueMatch footballLeagueMatch) throws Exception;

    /**
     * 根据id更新
     * @param footballLeagueMatch
     * @throws Exception
     */
    public int updateById(FootballLeagueMatch footballLeagueMatch) throws Exception;

    /**
     * 根据条件更新
     * @param footballLeagueMatch
     * @param example
     * @throws Exception
     */
    public int updateByExample(FootballLeagueMatch footballLeagueMatch, FootballLeagueMatchExample example) throws Exception;

    /**
     * 根据Id删除
     * @param footballLeagueMatchId
     * @throws Exception
     */
    public int delFootballLeagueMatchById(String footballLeagueMatchId) throws Exception;

    /**
     * 根据指定条件查询
     * @param example
     * @return
     * @throws Exception
     */
    public List<FootballLeagueMatch> queryFootballLeagueMatchesByExample(FootballLeagueMatchExample example) throws Exception;

    /**
     * 查询全部
     * @return
     * @throws Exception
     */
    public List<FootballLeagueMatch> queryAll() throws Exception;

    /**
     * 根据指定条件分页
     * @param example
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageBean<FootballLeagueMatch> pageFootballLeagueMatchesByExample(FootballLeagueMatchExample example, int pageNum, int pageSize) throws Exception;

    /**
     * 根据联赛级别查询
     * @param footballLeagueMatchLevel
     * @return
     * @throws Exception
     */
    public FootballLeagueMatch queryFootballLeagueMatchesByLevel(String footballLeagueMatchLevel) throws Exception;

    /**
     * 根据地区查询
     * @param regionName
     * @return
     * @throws Exception
     */
    public List<FootballLeagueMatch> queryFootballLeagueMatchesByRegionName(String regionName) throws Exception;

    /**
     * 根据联赛名称查询
     * @param footballLeagueMatchName
     * @return
     * @throws Exception
     */
    public FootballLeagueMatch queryFootballLeagueMatchesByName(String footballLeagueMatchName) throws Exception;

}
