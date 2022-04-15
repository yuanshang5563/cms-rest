package org.ys.crawler.service;

import org.ys.common.page.PageBean;
import org.ys.common.vo.CascaderTreeItem;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.model.FootballSeasonCategoryExample;

import java.util.List;

public interface FootballSeasonCategoryService {
    /**
     * 根据id查询
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    public FootballSeasonCategory queryFootballSeasonCategoryById(String footballSeasonCategoryId) throws Exception;

    /**
     * 根据Id获取全部属性(包括非数据库属性)
     * @param footballSeasonCategoryId
     * @return
     * @throws Exception
     */
    public FootballSeasonCategory queryFootballSeasonCategoryOfFullFieldById(String footballSeasonCategoryId) throws Exception;

    /**
     * 保存
     * @param footballSeasonCategory
     * @throws Exception
     */
    public int save(FootballSeasonCategory footballSeasonCategory) throws Exception;

    /**
     * 根据id更新
     * @param footballSeasonCategory
     * @throws Exception
     */
    public int updateById(FootballSeasonCategory footballSeasonCategory) throws Exception;

    /**
     * 根据条件更新
     * @param footballSeasonCategory
     * @param example
     * @throws Exception
     */
    public int updateByExample(FootballSeasonCategory footballSeasonCategory, FootballSeasonCategoryExample example) throws Exception;

    /**
     * 根据Id删除
     * @param footballSeasonCategoryId
     * @throws Exception
     */
    public int delFootballSeasonCategoryById(String footballSeasonCategoryId) throws Exception;

    /**
     * 根据指定条件查询
     * @param example
     * @return
     * @throws Exception
     */
    public List<FootballSeasonCategory> queryFootballSeasonCategoriesByExample(FootballSeasonCategoryExample example) throws Exception;

    /**
     * 查询全部
     * @return
     * @throws Exception
     */
    public List<FootballSeasonCategory> queryAll() throws Exception;

    /**
     * 根据指定条件分页
     * @param example
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageBean<FootballSeasonCategory> pageFootballSeasonCategoriesByExample(FootballSeasonCategoryExample example, int pageNum, int pageSize) throws Exception;

    /**
     * 根据联赛赛季id和赛季名称查询
     * @param footballSeasonId
     * @param footballSeasonCategoryName
     * @return
     * @throws Exception
     */
    public FootballSeasonCategory queryFootballSeasonCategoryBySeasonAndName(String footballSeasonId, String footballSeasonCategoryName) throws Exception;

    /**
     * 根据联赛id查询
     * @param footballLeagueMatchId
     * @return
     * @throws Exception
     */
    public List<FootballSeasonCategory> queryFootballSeasonCategoryByLeagueMatchId(String footballLeagueMatchId) throws Exception;

    /**
     * 根据赛季Id查询该赛季下属的赛季类别
     * @param footballSeasonId
     * @return
     * @throws Exception
     */
    public List<FootballSeasonCategory> queryFootballSeasonCategoryBySeasonId(String footballSeasonId) throws Exception;

    /**
     * 根据赛季Id集合查询该赛季下属的赛季类别
     * @param footballSeasonIds
     * @return
     * @throws Exception
     */
    public List<FootballSeasonCategory> queryFootballSeasonCategoryBySeasonIds(List<String> footballSeasonIds) throws Exception;

    /**
     * 根据赛季id获取赛季的级联数据
     * @param footballSeasonId
     * @return
     * @throws Exception
     */
    public List<CascaderTreeItem> findSeasonCascaderItemBySeasonId(String footballSeasonId) throws Exception;
}
