package org.ys.crawler.service;

import org.ys.common.page.PageBean;
import org.ys.crawler.model.FootballPlayer;
import org.ys.crawler.model.FootballPlayerExample;

import java.util.List;

public interface FootballPlayerService {
    /**
     * 根据id查询
     * @param footballPlayerId
     * @return
     * @throws Exception
     */
    public FootballPlayer queryFootballPlayerById(String footballPlayerId) throws Exception;

    /**
     * 保存
     * @param footballPlayer
     * @throws Exception
     */
    public int save(FootballPlayer footballPlayer) throws Exception;

    /**
     * 根据id更新
     * @param footballPlayer
     * @throws Exception
     */
    public int updateById(FootballPlayer footballPlayer) throws Exception;

    /**
     * 根据条件更新
     * @param footballPlayer
     * @param example
     * @throws Exception
     */
    public int updateByExample(FootballPlayer footballPlayer, FootballPlayerExample example) throws Exception;

    /**
     * 根据Id删除
     * @param footballPlayerId
     * @throws Exception
     */
    public int delFootballPlayerById(String footballPlayerId) throws Exception;

    /**
     * 根据指定条件查询
     * @param example
     * @return
     * @throws Exception
     */
    public List<FootballPlayer> queryFootballPlayersByExample(FootballPlayerExample example) throws Exception;

    /**
     * 查询全部
     * @return
     * @throws Exception
     */
    public List<FootballPlayer> queryAll() throws Exception;

    /**
     * 根据指定条件分页
     * @param example
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageBean<FootballPlayer> pageFootballPlayersByExample(FootballPlayerExample example, int pageNum, int pageSize) throws Exception;

    /**
     * 根据球队查询
     * @param footballTeamId
     * @return
     * @throws Exception
     */
    public List<FootballPlayer> queryFootballPlayersByTeamId(String footballTeamId) throws Exception;

    /**
     * 根据球员名称查询
     * @param footballPlayName
     * @return
     * @throws Exception
     */
    public List<FootballPlayer> queryFootballPlayersByName(String footballPlayName) throws Exception;

    /**
     * 根据球队和球员名称查询
     * @param footballTeamId
     * @param footballPlayName
     * @return
     * @throws Exception
     */
    public FootballPlayer queryFootballPlayerByTeamAndName(String footballTeamId,String footballPlayName) throws Exception;

    /**
     * 根据EntityId查询
     * @param entityId
     * @return
     * @throws Exception
     */
    public FootballPlayer queryFootballPlayerByEntityId(String entityId) throws Exception;
}
