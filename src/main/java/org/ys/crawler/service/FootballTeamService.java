package org.ys.crawler.service;

import org.ys.common.page.PageBean;
import org.ys.crawler.model.FootballTeam;
import org.ys.crawler.model.FootballTeamExample;

import java.util.List;

public interface FootballTeamService {
    /**
     * 根据id查询
     * @param footballTeamId
     * @return
     * @throws Exception
     */
    public FootballTeam queryFootballTeamById(String footballTeamId) throws Exception;

    /**
     * 保存
     * @param footballTeam
     * @throws Exception
     */
    public int save(FootballTeam footballTeam) throws Exception;

    /**
     * 根据id更新
     * @param footballTeam
     * @throws Exception
     */
    public int updateById(FootballTeam footballTeam) throws Exception;

    /**
     * 根据条件更新
     * @param footballTeam
     * @param example
     * @throws Exception
     */
    public int updateByExample(FootballTeam footballTeam, FootballTeamExample example) throws Exception;

    /**
     * 根据Id删除
     * @param footballTeamId
     * @throws Exception
     */
    public int delFootballTeamById(String footballTeamId) throws Exception;

    /**
     * 根据指定条件查询
     * @param example
     * @return
     * @throws Exception
     */
    public List<FootballTeam> queryFootballTeamsByExample(FootballTeamExample example) throws Exception;

    /**
     * 查询全部
     * @return
     * @throws Exception
     */
    public List<FootballTeam> queryAll() throws Exception;

    /**
     * 根据指定条件分页
     * @param example
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageBean<FootballTeam> pageFootballTeamsByExample(FootballTeamExample example, int pageNum, int pageSize) throws Exception;

    /**
     * 根据指定entityId查询
     * @param entityId
     * @return
     * @throws Exception
     */
    public FootballTeam queryFootballTeamByEntityId(String entityId) throws Exception;

    /**
     * 根据国家查询
     * @param country
     * @return
     * @throws Exception
     */
    public List<FootballTeam> queryFootballTeamsByCountry(String country) throws Exception;

    /**
     * 根据名字查询
     * @param teamName
     * @return
     * @throws Exception
     */
    public List<FootballTeam> queryFootballTeamsByName(String teamName) throws Exception;
}
