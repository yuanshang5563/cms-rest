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
    public FootballSeason queryFootballSeasonOfFullFieldById(String footballSeasonId) throws Exception {
        if (StringUtils.isEmpty(footballSeasonId)){
            return null;
        }
        FootballSeason season = queryFootballSeasonById(footballSeasonId);
        if(null != season){
            FootballLeagueMatch leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(season.getFootballLeagueMatchId());
            if(null != leagueMatch){
                season.setFootballLeagueMatchName(leagueMatch.getFootballLeagueMatchName());
            }
        }
        return season;
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
        fillFootballSeasons(footballSeasons);
        return new PageBean<FootballSeason>(footballSeasons);
    }

    @Override
    public List<FootballSeason> fillFootballSeasons(List<FootballSeason> footballSeasons) throws Exception {
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
        return footballSeasons;
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

    @Override
    public List<FootballSeason> queryLatestFootballSeasons() throws Exception {
        //先找到所有联赛，再一个个找最新赛季
        List<FootballLeagueMatch> leagueMatches = footballLeagueMatchService.queryAll();
        if(null != leagueMatches && leagueMatches.size() > 0){
            List<FootballSeason> latestSeasons = new ArrayList<FootballSeason>();
            for (FootballLeagueMatch leagueMatch : leagueMatches) {
                FootballSeasonExample example = new FootballSeasonExample();
                example.createCriteria().andFootballLeagueMatchIdEqualTo(leagueMatch.getFootballLeagueMatchId());
                example.setOrderByClause(" season_end_date desc");
                List<FootballSeason> seasons = queryFootballSeasonsByExample(example);
                if(null != seasons && seasons.size() > 0){
                    latestSeasons.add(seasons.get(0));
                }
            }
            return latestSeasons;
        }else{
            return null;
        }
    }

    @Override
    public List<String> queryLatestFootballSeasonIds() throws Exception {
        //先找到所有联赛，再一个个找最新赛季
        List<FootballLeagueMatch> leagueMatches = footballLeagueMatchService.queryAll();
        if(null != leagueMatches && leagueMatches.size() > 0){
            List<String> latestSeasonIds = new ArrayList<String>();
            for (FootballLeagueMatch leagueMatch : leagueMatches) {
                FootballSeasonExample example = new FootballSeasonExample();
                example.createCriteria().andFootballLeagueMatchIdEqualTo(leagueMatch.getFootballLeagueMatchId());
                example.setOrderByClause(" season_end_date desc");
                List<FootballSeason> seasons = queryFootballSeasonsByExample(example);
                if(null != seasons && seasons.size() > 0){
                    latestSeasonIds.add(seasons.get(0).getFootballSeasonId());
                }
            }
            return latestSeasonIds;
        }else{
            return null;
        }
    }

    @Override
    public FootballSeason queryLatestFootballSeasonByLeagueMatch(String footballLeagueMatchId) throws Exception {
        if(StringUtils.isEmpty(footballLeagueMatchId)){
            return null;
        }
        FootballSeasonExample example = new FootballSeasonExample();
        example.createCriteria().andFootballLeagueMatchIdEqualTo(StringUtils.trim(footballLeagueMatchId));
        example.setOrderByClause(" season_end_date desc");
        List<FootballSeason> seasons = queryFootballSeasonsByExample(example);
        if(null != seasons && seasons.size() > 0){
            return seasons.get(0);
        }else{
            return null;
        }
    }
}
