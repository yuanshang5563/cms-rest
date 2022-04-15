package org.ys.crawler.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.common.vo.CascaderTreeItem;
import org.ys.crawler.dao.FootballSeasonCategoryMapper;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.model.FootballSeasonCategoryExample;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballSeasonCategoryService;
import org.ys.crawler.service.FootballSeasonService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("footballSeasonCategoryService")
public class FootballSeasonCategoryServiceImpl implements FootballSeasonCategoryService {
    @Autowired
    private FootballSeasonCategoryMapper footballSeasonCategoryMapper;
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Override
    public FootballSeasonCategory queryFootballSeasonCategoryById(String footballSeasonCategoryId) throws Exception {
        if (StringUtils.isEmpty(footballSeasonCategoryId)){
            return null;
        }
        return footballSeasonCategoryMapper.selectByPrimaryKey(footballSeasonCategoryId);
    }

    @Override
    public FootballSeasonCategory queryFootballSeasonCategoryOfFullFieldById(String footballSeasonCategoryId) throws Exception {
        if (StringUtils.isEmpty(footballSeasonCategoryId)){
            return null;
        }
        FootballSeasonCategory seasonCategory = queryFootballSeasonCategoryById(footballSeasonCategoryId);
        if(null != seasonCategory){
            FootballSeason season = footballSeasonService.queryFootballSeasonById(seasonCategory.getFootballSeasonId());
            if(null != season){
                seasonCategory.setFootballSeasonName(season.getFootballSeasonName());
                String leagueMatchId = season.getFootballLeagueMatchId();
                FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(leagueMatchId);
                if(null != leagueMatch){
                    seasonCategory.setFootballLeagueMatchName(leagueMatch.getFootballLeagueMatchName());
                }
            }
        }
        return seasonCategory;
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
        if(null != footballSeasonCategories && footballSeasonCategories.size() > 0){
            Map<String, FootballSeason> seasonMap = new HashMap<String,FootballSeason>();
            Map<String, FootballLeagueMatch> leagueMatchMap = new HashMap<String,FootballLeagueMatch>();
            for (FootballSeasonCategory footballSeasonCategory : footballSeasonCategories) {
                FootballSeason season = seasonMap.get(footballSeasonCategory.getFootballSeasonId());
                if(null == season){
                    season = footballSeasonService.queryFootballSeasonById(footballSeasonCategory.getFootballSeasonId());
                    if(null != season){
                        seasonMap.put(footballSeasonCategory.getFootballSeasonId(),season);
                    }
                }
                String leagueMatchId = season.getFootballLeagueMatchId();
                FootballLeagueMatch leagueMatch = leagueMatchMap.get(leagueMatchId);
                if(null == leagueMatch){
                    leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(leagueMatchId);
                    if(null != leagueMatch){
                        leagueMatchMap.put(leagueMatchId,leagueMatch);
                    }
                }
                footballSeasonCategory.setFootballSeasonName(season.getFootballSeasonName());
                footballSeasonCategory.setFootballLeagueMatchName(leagueMatch.getFootballLeagueMatchName());
            }
        }
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
        FootballSeasonCategoryExample example = new FootballSeasonCategoryExample();
        example.createCriteria().andFootballLeagueMatchIdEqualTo(footballLeagueMatchId);
        return footballSeasonCategoryMapper.selectByExample(example);
    }

    @Override
    public List<FootballSeasonCategory> queryFootballSeasonCategoryBySeasonId(String footballSeasonId) throws Exception {
        if(StringUtils.isEmpty(footballSeasonId)){
            return null;
        }
        FootballSeasonCategoryExample example = new FootballSeasonCategoryExample();
        example.createCriteria().andFootballSeasonIdEqualTo(StringUtils.trim(footballSeasonId));
        return footballSeasonCategoryMapper.selectByExample(example);
    }

    @Override
    public List<FootballSeasonCategory> queryFootballSeasonCategoryBySeasonIds(List<String> footballSeasonIds) throws Exception {
        if(null == footballSeasonIds || footballSeasonIds.size() == 0){
            return null;
        }
        FootballSeasonCategoryExample example = new FootballSeasonCategoryExample();
        example.createCriteria().andFootballSeasonIdIn(footballSeasonIds);
        return footballSeasonCategoryMapper.selectByExample(example);
    }

    @Override
    public List<CascaderTreeItem> findSeasonCascaderItemBySeasonId(String footballSeasonId) throws Exception {
        if(StringUtils.isEmpty(footballSeasonId)){
            return null;
        }
        List<FootballSeasonCategory> categories = queryFootballSeasonCategoryBySeasonId(footballSeasonId);
        List<CascaderTreeItem> treeItems = new ArrayList<CascaderTreeItem>();
        if(null != categories && categories.size() > 0){
            for (FootballSeasonCategory category : categories) {
                CascaderTreeItem item = new CascaderTreeItem();
                item.setId(category.getFootballSeasonCategoryId());
                item.setName(StringUtils.trim(category.getFootballSeasonCategoryName()));
                item.setParentId(category.getFootballSeasonId());
                item.setChildren(new ArrayList<CascaderTreeItem>());
                treeItems.add(item);
            }
        }
        return treeItems;
    }
}
