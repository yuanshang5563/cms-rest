package org.ys.crawler.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.crawler.dao.FootballScoreDetailMapper;
import org.ys.crawler.model.FootballScoreDetail;
import org.ys.crawler.model.FootballScoreDetailExample;
import org.ys.crawler.service.FootballScoreDetailService;

import java.util.List;

@Service("footballScoreDetailService")
public class FootballScoreDetailServiceImpl implements FootballScoreDetailService {
    @Autowired
    private FootballScoreDetailMapper footballScoreDetailMapper;

    @Override
    public FootballScoreDetail queryFootballScoreDetailById(String footballScoreDetailId) throws Exception {
        if (StringUtils.isEmpty(footballScoreDetailId)){
            return null;
        }
        return footballScoreDetailMapper.selectByPrimaryKey(footballScoreDetailId);
    }

    @Override
    public int save(FootballScoreDetail footballScoreDetail) throws Exception {
        if(null != footballScoreDetail){
            return footballScoreDetailMapper.insert(footballScoreDetail);
        }
        return 0;
    }

    @Override
    public int updateById(FootballScoreDetail footballScoreDetail) throws Exception {
        if(null != footballScoreDetail){
            return footballScoreDetailMapper.updateByPrimaryKey(footballScoreDetail);
        }
        return 0;
    }

    @Override
    public int updateByExample(FootballScoreDetail footballScoreDetail, FootballScoreDetailExample example) throws Exception {
        if (null != footballScoreDetail && null != example){
            return footballScoreDetailMapper.updateByExample(footballScoreDetail,example);
        }
        return 0;
    }

    @Override
    public int delFootballScoreDetailById(String footballScoreDetailId) throws Exception {
        if(StringUtils.isNotEmpty(footballScoreDetailId)){
            return footballScoreDetailMapper.deleteByPrimaryKey(footballScoreDetailId);
        }
        return 0;
    }

    @Override
    public List<FootballScoreDetail> queryFootballScoreDetailsByExample(FootballScoreDetailExample example) throws Exception {
        if(null == example){
            return null;
        }
        return footballScoreDetailMapper.selectByExample(example);
    }

    @Override
    public List<FootballScoreDetail> queryAll() throws Exception {
        FootballScoreDetailExample example = new FootballScoreDetailExample();
        return footballScoreDetailMapper.selectByExample(example);
    }

    @Override
    public PageBean<FootballScoreDetail> pageFootballScoreDetailsByExample(FootballScoreDetailExample example, int pageNum, int pageSize) throws Exception {
        if(null == example) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<FootballScoreDetail> footballScoreDetails = footballScoreDetailMapper.selectByExample(example);
        return new PageBean<FootballScoreDetail>(footballScoreDetails);
    }

    @Override
    public List<FootballScoreDetail> queryFootballScoreDetailsByScoreId(String footballScoreId) throws Exception {
        if(StringUtils.isEmpty(footballScoreId)){
            return null;
        }
        FootballScoreDetailExample example = new FootballScoreDetailExample();
        example.createCriteria().andFootballScoreIdEqualTo(StringUtils.trim(footballScoreId));
        return footballScoreDetailMapper.selectByExample(example);
    }

    @Override
    public int delFootballScoreDetailsByScoreId(String footballScoreId) throws Exception {
        if(StringUtils.isNotEmpty(footballScoreId)){
            FootballScoreDetailExample example = new FootballScoreDetailExample();
            example.createCriteria().andFootballScoreIdEqualTo(StringUtils.trim(footballScoreId));
            return footballScoreDetailMapper.deleteByExample(example);
        }
        return 0;
    }

    @Override
    public List<FootballScoreDetail> queryFootballScoreDetailsByLeiDataId(String leidataScoreId) throws Exception {
        if(StringUtils.isEmpty(leidataScoreId)){
            return null;
        }
        FootballScoreDetailExample example = new FootballScoreDetailExample();
        example.createCriteria().andLeidataScoreIdEqualTo(StringUtils.trim(leidataScoreId));
        return footballScoreDetailMapper.selectByExample(example);
    }
}
