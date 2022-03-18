package org.ys.crawler.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.crawler.dao.FootballSeasonCategoryMapper;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.model.FootballSeasonCategoryExample;
import org.ys.crawler.service.FootballSeasonCategoryService;
import org.ys.crawler.service.FootballSeasonService;

import java.util.ArrayList;
import java.util.List;

@Service("footballSeasonCategoryService")
public class FootballSeasonCategoryServiceImpl implements FootballSeasonCategoryService {
    @Autowired
    private FootballSeasonCategoryMapper footballSeasonCategoryMapper;
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Override
    public FootballSeasonCategory queryFootballSeasonCategoryById(String footballSeasonCategoryId) throws Exception {
        if (StringUtils.isEmpty(footballSeasonCategoryId)){
            return null;
        }
        return footballSeasonCategoryMapper.selectByPrimaryKey(footballSeasonCategoryId);
    }

    @Override
    public int save(FootballSeasonCategory footballSeasonCategory) throws Exception {
        if(null != footballSeasonCategory){
            return footballSeasonCategoryMapper.insert(footballSeasonCategory);
        }
        return 0;
    }

    @Override
    public int updateById(FootballSeasonCategory footballSeasonCategory) throws Exception {
        if(null != footballSeasonCategory){
            return footballSeasonCategoryMapper.updateByPrimaryKey(footballSeasonCategory);
        }
        return 0;
    }

    @Override
    public int updateByExample(FootballSeasonCategory footballSeasonCategory, FootballSeasonCategoryExample example) throws Exception {
        if (null != footballSeasonCategory && null != example){
            return footballSeasonCategoryMapper.updateByExample(footballSeasonCategory,example);
        }
        return 0;
    }

    @Override
    public int delFootballSeasonCategoryById(String footballSeasonCategoryId) throws Exception {
        if(StringUtils.isNotEmpty(footballSeasonCategoryId)){
            return footballSeasonCategoryMapper.deleteByPrimaryKey(footballSeasonCategoryId);
        }
        return 0;
    }

    @Override
    public List<FootballSeasonCategory> queryFootballSeasonCategoriesByExample(FootballSeasonCategoryExample example) throws Exception {
        if(null == example){
            return null;
        }
        return footballSeasonCategoryMapper.selectByExample(example);
    }

    @Override
    public List<FootballSeasonCategory> queryAll() throws Exception {
        FootballSeasonCategoryExample example = new FootballSeasonCategoryExample();
        return footballSeasonCategoryMapper.selectByExample(example);
    }

    @Override
    public PageBean<FootballSeasonCategory> pageFootballSeasonCategoriesByExample(FootballSeasonCategoryExample example, int pageNum, int pageSize) throws Exception {
        if(null == example) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<FootballSeasonCategory> footballSeasonCategories = footballSeasonCategoryMapper.selectByExample(example);
        return new PageBean<FootballSeasonCategory>(footballSeasonCategories);
    }

    @Override
    public FootballSeasonCategory queryFootballSeasonCategoryBySeasonAndName(String footballSeasonId, String footballSeasonCategoryName) throws Exception {
        if(StringUtils.isEmpty(footballSeasonId) || StringUtils.isEmpty(footballSeasonCategoryName)){
            return null;
        }
        FootballSeasonCategoryExample example = new FootballSeasonCategoryExample();
        example.createCriteria().andFootballSeasonCategoryNameEqualTo(StringUtils.trim(footballSeasonCategoryName))
               .andFootballSeasonIdEqualTo(StringUtils.trim(footballSeasonId));
        List<FootballSeasonCategory> footballSeasonCategories = footballSeasonCategoryMapper.selectByExample(example);
        if(null != footballSeasonCategories && footballSeasonCategories.size() > 0){
            return footballSeasonCategories.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<FootballSeasonCategory> queryFootballSeasonCategoryByLeagueMatchId(String footballLeagueMatchId) throws Exception {
        if(StringUtils.isEmpty(footballLeagueMatchId)){
            return null;
        }
        List<FootballSeason> footballSeasons = footballSeasonService.queryFootballSeasonsByLeagueMatch(StringUtils.trim(footballLeagueMatchId));
        if(null == footballSeasons || footballSeasons.size() == 0){
            return null;
        }
        List<String> seasonIdList = new ArrayList<String>();
        for (FootballSeason footballSeason : footballSeasons) {
            seasonIdList.add(StringUtils.trim(footballSeason.getFootballSeasonId()));
        }
        FootballSeasonCategoryExample example = new FootballSeasonCategoryExample();
        example.createCriteria().andFootballSeasonIdIn(seasonIdList);
        List<FootballSeasonCategory> footballSeasonCategories = footballSeasonCategoryMapper.selectByExample(example);
        return footballSeasonCategories;
    }
}