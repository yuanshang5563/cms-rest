package org.ys.crawler.service;

import org.ys.common.page.PageBean;
import org.ys.crawler.model.FootballScoreDetail;
import org.ys.crawler.model.FootballScoreDetailExample;

import java.util.List;

public interface FootballScoreDetailService {
    /**
     * 根据id查询
     * @param footballScoreDetailId
     * @return
     * @throws Exception
     */
    public FootballScoreDetail queryFootballScoreDetailById(String footballScoreDetailId) throws Exception;

    /**
     * 保存
     * @param footballScoreDetail
     * @throws Exception
     */
    public int save(FootballScoreDetail footballScoreDetail) throws Exception;

    /**
     * 根据id更新
     * @param footballScoreDetail
     * @throws Exception
     */
    public int updateById(FootballScoreDetail footballScoreDetail) throws Exception;

    /**
     * 根据条件更新
     * @param footballScoreDetail
     * @param example
     * @throws Exception
     */
    public int updateByExample(FootballScoreDetail footballScoreDetail, FootballScoreDetailExample example) throws Exception;

    /**
     * 根据Id删除
     * @param footballScoreDetailId
     * @throws Exception
     */
    public int delFootballScoreDetailById(String footballScoreDetailId) throws Exception;

    /**
     * 根据指定条件查询
     * @param example
     * @return
     * @throws Exception
     */
    public List<FootballScoreDetail> queryFootballScoreDetailsByExample(FootballScoreDetailExample example) throws Exception;

    /**
     * 查询全部
     * @return
     * @throws Exception
     */
    public List<FootballScoreDetail> queryAll() throws Exception;

    /**
     * 根据指定条件分页
     * @param example
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageBean<FootballScoreDetail> pageFootballScoreDetailsByExample(FootballScoreDetailExample example, int pageNum, int pageSize) throws Exception;

    /**
     * 根据比赛Id查询
     * @param footballScoreId
     * @return
     * @throws Exception
     */
    public List<FootballScoreDetail> queryFootballScoreDetailsByScoreId(String footballScoreId) throws Exception;

    /**
     * 根据赛季ID批量删除比分详情数据
     * @param footballScoreId
     * @return
     * @throws Exception
     */
    public int delFootballScoreDetailsByScoreId(String footballScoreId) throws Exception;

    /**
     * 根据雷达id查询数据
     * @param leidataScoreId
     * @return
     * @throws Exception
     */
    public List<FootballScoreDetail> queryFootballScoreDetailsByLeiDataId(String leidataScoreId) throws Exception;
}
