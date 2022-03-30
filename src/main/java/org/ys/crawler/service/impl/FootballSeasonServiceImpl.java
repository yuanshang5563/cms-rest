package org.ys.crawler.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.common.vo.CascaderTreeItem;
import org.ys.crawler.dao.FootballSeasonMapper;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonExample;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballSeasonService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("footballSeasonService")
public class FootballSeasonServiceImpl implements FootballSeasonService {
    @Autowired
    private FootballSeasonMapper footballSeasonMapper;
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;

    @Override
    public FootballSeason queryFootballSeasonById(String footballSeasonId) throws Exception {
        if (StringUtils.isEmpty(footballSeasonId)){
            return null;
        }
        return footballSeasonMapper.selectByPrimaryKey(footballSeasonId);
    }

    @Override
    public int save(FootballSeason footballSeason) throws Exception {
        if(null != footballSeason){
            return footballSeasonMapper.insert(footballSeason);
        }
        return 0;
    }

    @Override
    public int updateById(FootballSeason footballSeason) throws Exception {
        if(null != footballSeason){
            return footballSeasonMapper.updateByPrimaryKey(footballSeason);
        }
        return 0;
    }

    @Override
    public int updateByExample(FootballSeason footballSeason, FootballSeasonExample example) throws Exception {
        if (null != footballSeason && null != example){
            return footballSeasonMapper.updateByExample(footballSeason,example);
        }
        return 0;
    }

    @Override
    public int delFootballSeasonById(String footballSeasonId) throws Exception {
        if(StringUtils.isNotEmpty(footballSeasonId)){
            return footballSeasonMapper.deleteByPrimaryKey(footballSeasonId);
        }
        return 0;
    }

    @Override
    public List<FootballSeason> queryFootballSeasonsByExample(FootballSeasonExample example) throws Exception {
        if(null == example){
            return null;
        }
        return footballSeasonMapper.selectByExample(example);
    }

    @Override
    public List<FootballSeason> queryAll() throws Exception {
        FootballSeasonExample example = new FootballSeasonExample();
        return footballSeasonMapper.selectByExample(example);
    }

    @Override
    public PageBean<FootballSeason> pageFootballSeasonsByExample(FootballSeasonExample example, int pageNum, int pageSize) throws Exception {
        if(null == example) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<FootballSeason> footballSeasons = footballSeasonMapper.selectByExample(example);
        if(null != footballSeasons && footballSeasons.size() > 0){
            Map<String,FootballLeagueMatch> leagueMatchMap = new HashMap<String,FootballLeagueMatch>();
            for (FootballSeason footballSeason : footballSeasons) {
                FootballLeagueMatch leagueMatch = leagueMatchMap.get(footballSeason.getFootballLeagueMatchId());
                if(null == leagueMatch){
                    leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(footballSeason.getFootballLeagueMatchId());
                    if(null != leagueMatch){
                        leagueMatchMap.put(footballSeason.getFootballLeagueMatchId(),leagueMatch);
                    }
                }
                footballSeason.setFootballLeagueMatchName(leagueMatch.getFootballLeagueMatchName());
            }
        }
        return new PageBean<FootballSeason>(footballSeasons);
    }

    @Override
    public FootballSeason queryFootballSeasonByLeagueMatchAndName(String footballLeagueMatchId,String footballSeasonName) throws Exception {
        if(StringUtils.isEmpty(footballSeasonName) || StringUtils.isEmpty(footballLeagueMatchId)){
            return null;
        }
        FootballSeasonExample example = new FootballSeasonExample();
        example.createCriteria().andFootballSeasonNameEqualTo(StringUtils.trim(footballSeasonName))
        .andFootballLeagueMatchIdEqualTo(StringUtils.trim(footballLeagueMatchId));
        List<FootballSeason> footballSeasons = footballSeasonMapper.selectByExample(example);
        if(null != footballSeasons && footballSeasons.size() > 0){
            return footballSeasons.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<FootballSeason> queryFootballSeasonsByLeagueMatch(String footballLeagueMatchId) throws Exception {
        if(StringUtils.isEmpty(footballLeagueMatchId)){
            return null;
        }
        FootballSeasonExample example = new FootballSeasonExample();
        example.createCriteria().andFootballLeagueMatchIdEqualTo(StringUtils.trim(footballLeagueMatchId));
        example.setOrderByClause(" season_begin_date desc");
        List<FootballSeason> footballSeasons = footballSeasonMapper.selectByExample(example);
        return footballSeasons;
    }

    @Override
    public List<CascaderTreeItem> findSeasonCascaderItemByLeagueMatchId(String footballLeagueMatchId) throws Exception {
        if(StringUtils.isEmpty(footballLeagueMatchId)){
            return null;
        }
        List<FootballSeason> footballSeasons = queryFootballSeasonsByLeagueMatch(footballLeagueMatchId);
        List<CascaderTreeItem> treeItems = new ArrayList<CascaderTreeItem>();
        if(null != footballSeasons && footballSeasons.size() > 0){
            for (FootballSeason footballSeason : footballSeasons) {
                CascaderTreeItem item = new CascaderTreeItem();
                item.setId(footballSeason.getFootballSeasonId());
                item.setName(StringUtils.trim(footballSeason.getFootballSeasonName()));
                item.setParentId(footballSeason.getFootballLeagueMatchId());
                item.setChildren(new ArrayList<CascaderTreeItem>());
                treeItems.add(item);
            }
        }
        return treeItems;
    }
}
