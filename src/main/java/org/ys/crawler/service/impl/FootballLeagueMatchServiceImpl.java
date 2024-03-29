package org.ys.crawler.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.common.vo.CascaderTreeItem;
import org.ys.crawler.constant.FootballLeagueMatchConstant;
import org.ys.crawler.dao.FootballLeagueMatchMapper;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballLeagueMatchExample;
import org.ys.crawler.service.FootballLeagueMatchService;

import java.util.ArrayList;
import java.util.List;

@Service("footballLeagueMatchService")
public class FootballLeagueMatchServiceImpl implements FootballLeagueMatchService {
    @Autowired
    private FootballLeagueMatchMapper footballLeagueMatchMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public FootballLeagueMatch queryFootballLeagueMatchById(String footballLeagueMatchId) throws Exception {
        if (StringUtils.isEmpty(footballLeagueMatchId)){
            return null;
        }
        return footballLeagueMatchMapper.selectByPrimaryKey(footballLeagueMatchId);
    }

    @Override
    public int save(FootballLeagueMatch footballLeagueMatch) throws Exception {
        if(null != footballLeagueMatch){
            return footballLeagueMatchMapper.insert(footballLeagueMatch);
        }
        return 0;
    }

    @Override
    public int updateById(FootballLeagueMatch footballLeagueMatch) throws Exception {
        if(null != footballLeagueMatch){
            return footballLeagueMatchMapper.updateByPrimaryKey(footballLeagueMatch);
        }
        return 0;
    }

    @Override
    public int updateByExample(FootballLeagueMatch footballLeagueMatch, FootballLeagueMatchExample example) throws Exception {
        if (null != footballLeagueMatch && null != example){
            return footballLeagueMatchMapper.updateByExample(footballLeagueMatch,example);
        }
        return 0;
    }

    @Override
    public int delFootballLeagueMatchById(String footballLeagueMatchId) throws Exception {
        if(StringUtils.isNotEmpty(footballLeagueMatchId)){
            return footballLeagueMatchMapper.deleteByPrimaryKey(footballLeagueMatchId);
        }
        return 0;
    }

    @Override
    public List<FootballLeagueMatch> queryFootballLeagueMatchesByExample(FootballLeagueMatchExample example) throws Exception {
        if(null == example){
            return null;
        }
        return footballLeagueMatchMapper.selectByExample(example);
    }

    @Override
    public List<FootballLeagueMatch> queryAll() throws Exception {
        FootballLeagueMatchExample example = new FootballLeagueMatchExample();
        return footballLeagueMatchMapper.selectByExample(example);
    }

    @Override
    public PageBean<FootballLeagueMatch> pageFootballLeagueMatchesByExample(FootballLeagueMatchExample example, int pageNum, int pageSize) throws Exception {
        if(null == example) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<FootballLeagueMatch> footballLeagueMatches = footballLeagueMatchMapper.selectByExample(example);
        return new PageBean<FootballLeagueMatch>(footballLeagueMatches);
    }

    @Override
    public FootballLeagueMatch queryFootballLeagueMatchesByLevel(String footballLeagueMatchLevel) throws Exception {
        if (StringUtils.isEmpty(footballLeagueMatchLevel)){
            return null;
        }
        FootballLeagueMatchExample example = new FootballLeagueMatchExample();
        example.createCriteria().andFootballLeagueMatchLevelEqualTo(StringUtils.trim(footballLeagueMatchLevel));
        List<FootballLeagueMatch> footballLeagueMatches = footballLeagueMatchMapper.selectByExample(example);
        if(null ==footballLeagueMatches && footballLeagueMatches.size() > 0){
            return footballLeagueMatches.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<FootballLeagueMatch> queryFootballLeagueMatchesByRegionName(String regionName) throws Exception {
        if (StringUtils.isEmpty(regionName)){
            return null;
        }
        FootballLeagueMatchExample example = new FootballLeagueMatchExample();
        example.createCriteria().andRegionNameEqualTo(StringUtils.trim(regionName));
        return footballLeagueMatchMapper.selectByExample(example);
    }

    @Override
    public FootballLeagueMatch queryFootballLeagueMatchesByName(String footballLeagueMatchName) throws Exception {
        if(StringUtils.isEmpty(footballLeagueMatchName)){
            return null;
        }
        FootballLeagueMatchExample example = new FootballLeagueMatchExample();
        example.createCriteria().andFootballLeagueMatchNameEqualTo(StringUtils.trim(footballLeagueMatchName));
        List<FootballLeagueMatch> footballLeagueMatches = footballLeagueMatchMapper.selectByExample(example);
        if(null !=footballLeagueMatches && footballLeagueMatches.size() > 0){
            return footballLeagueMatches.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<CascaderTreeItem> findLeagueMatchCascaderItem() throws Exception {
        //先从缓存中找
        List<CascaderTreeItem> treeItems = (List<CascaderTreeItem>) redisTemplate.opsForList().leftPop(FootballLeagueMatchConstant.LEAGUE_MATCH_CASCADER_ITEM_REDIS);
        if(null == treeItems || treeItems.size() <= 0){
            List<FootballLeagueMatch> leagueMatches = queryAll();
            treeItems = new ArrayList<CascaderTreeItem>();
            if(null != leagueMatches && leagueMatches.size() > 0){
                for (FootballLeagueMatch leagueMatch : leagueMatches) {
                    CascaderTreeItem treeItem = new CascaderTreeItem();
                    treeItem.setId(leagueMatch.getFootballLeagueMatchId());
                    treeItem.setName(StringUtils.trim(leagueMatch.getFootballLeagueMatchName()));
                    treeItem.setChildren(new ArrayList<CascaderTreeItem>());
                    treeItems.add(treeItem);
                }
            }
            redisTemplate.opsForList().leftPush(FootballLeagueMatchConstant.LEAGUE_MATCH_CASCADER_ITEM_REDIS,treeItems);
        }
        return treeItems;
    }
}
