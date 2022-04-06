package org.ys.crawler.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.crawler.dao.FootballIntegralMapper;
import org.ys.crawler.model.*;
import org.ys.crawler.service.*;

import java.util.*;

@Service("footballIntegralService")
public class FootballIntegralServiceImpl implements FootballIntegralService {
    @Autowired
    private FootballTeamService footballTeamService;
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;
    @Autowired
    private FootballIntegralMapper footballIntegralMapper;

    @Override
    public FootballIntegral queryFootballIntegralById(String footballIntegralId) throws Exception {
        if (StringUtils.isEmpty(footballIntegralId)){
            return null;
        }
        return footballIntegralMapper.selectByPrimaryKey(footballIntegralId);
    }

    @Override
    public int save(FootballIntegral footballIntegral) throws Exception {
        if(null != footballIntegral){
            return footballIntegralMapper.insert(footballIntegral);
        }
        return 0;
    }

    @Override
    public int updateById(FootballIntegral footballIntegral) throws Exception {
        if(null != footballIntegral){
            return footballIntegralMapper.updateByPrimaryKey(footballIntegral);
        }
        return 0;
    }

    @Override
    public int updateByExample(FootballIntegral footballIntegral, FootballIntegralExample example) throws Exception {
        if (null != footballIntegral && null != example){
            return footballIntegralMapper.updateByExample(footballIntegral,example);
        }
        return 0;
    }

    @Override
    public int delFootballIntegralById(String footballIntegralId) throws Exception {
        if(StringUtils.isNotEmpty(footballIntegralId)){
            return footballIntegralMapper.deleteByPrimaryKey(footballIntegralId);
        }
        return 0;
    }

    @Override
    public List<FootballIntegral> queryFootballIntegralsByExample(FootballIntegralExample example) throws Exception {
        if(null == example){
            return null;
        }
        return footballIntegralMapper.selectByExample(example);
    }

    @Override
    public List<FootballIntegral> queryAll() throws Exception {
        FootballIntegralExample example = new FootballIntegralExample();
        return footballIntegralMapper.selectByExample(example);
    }

    @Override
    public PageBean<FootballIntegral> pageFootballIntegralsByExample(FootballIntegralExample example, int pageNum, int pageSize) throws Exception {
        if(null == example) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<FootballIntegral> footballIntegrals = footballIntegralMapper.selectByExample(example);
        if(null != footballIntegrals && footballIntegrals.size() > 0){
            Map<String, FootballTeam> teamMap = new HashMap<String,FootballTeam>();
            Map<String,FootballLeagueMatch> leagueMatchMap = new HashMap<String,FootballLeagueMatch>();
            Map<String,FootballSeason> seasonMap = new HashMap<String,FootballSeason>();
            Map<String,FootballSeasonCategory> categoryMap = new HashMap<String,FootballSeasonCategory>();
            for (FootballIntegral integral : footballIntegrals) {
                //先填队名
                FootballTeam team = teamMap.get(integral.getFootballTeamId());
                if(null == team){
                    team = footballTeamService.queryFootballTeamById(integral.getFootballTeamId());
                    if(null != team){
                        teamMap.put(team.getFootballTeamId(),team);
                    }
                }
                if(null != team){
                    integral.setTeamName(team.getTeamName());
                }
                //填充联赛，赛季，类别的名称
                FootballLeagueMatch leagueMatch = leagueMatchMap.get(integral.getFootballLeagueMatchId());
                if(null == leagueMatch){
                    leagueMatch = footballLeagueMatchService.queryFootballLeagueMatchById(integral.getFootballLeagueMatchId());
                    if(null != leagueMatch){
                        leagueMatchMap.put(integral.getFootballLeagueMatchId(),leagueMatch);
                    }
                }
                if(null != leagueMatch){
                    integral.setFootballLeagueMatchName(leagueMatch.getFootballLeagueMatchName());
                }
                FootballSeason season = seasonMap.get(integral.getFootballSeasonId());
                if(null == season){
                    season = footballSeasonService.queryFootballSeasonById(integral.getFootballSeasonId());
                    if(null != season){
                        seasonMap.put(integral.getFootballSeasonId(),season);
                    }
                }
                if(null != season){
                    integral.setFootballSeasonName(season.getFootballSeasonName());
                }
                FootballSeasonCategory seasonCategory = categoryMap.get(integral.getFootballSeasonCategoryId());
                if(null == seasonCategory){
                    seasonCategory = footballSeasonCategoryService.queryFootballSeasonCategoryById(integral.getFootballSeasonCategoryId());
                    if(null != seasonCategory){
                        categoryMap.put(integral.getFootballSeasonCategoryId(),seasonCategory);
                    }
                }
                if(null != seasonCategory){
                    integral.setFootballSeasonCategoryName(seasonCategory.getFootballSeasonCategoryName());
                }
            }
        }
        return new PageBean<FootballIntegral>(footballIntegrals);
    }

    @Override
    public FootballIntegral queryFootballIntegralByLeiDataId(String leiDataIntegralId) throws Exception {
        if(StringUtils.isEmpty(leiDataIntegralId)){
            return null;
        }
        FootballIntegralExample example = new FootballIntegralExample();
        example.createCriteria().andLeiDataIntegralIdEqualTo(StringUtils.trim(leiDataIntegralId));
        List<FootballIntegral> footballIntegrals = footballIntegralMapper.selectByExample(example);
        if(null != footballIntegrals && footballIntegrals.size() > 0){
            return footballIntegrals.get(0);
        }else {
            return null;
        }
    }

    @Override
    public boolean isExistsIntegralByCategoryId(Set<String> categoryIds, FootballSeasonCategory category) throws Exception {
        if(null == category || null == categoryIds){
            return false;
        }
        boolean containsFlag = categoryIds.contains(category.getFootballSeasonCategoryId());
        if(containsFlag){
            return true;
        }else{
            List<FootballIntegral> integrals = queryFootballIntegralsBySeasonCategoryId(category.getFootballSeasonCategoryId());
            if(null != integrals && integrals.size() > 0){
                categoryIds.add(category.getFootballSeasonCategoryId());
                return true;
            }
        }
        return false;
    }

    @Override
    public List<FootballIntegral> queryFootballIntegralsByLeagueMatchId(String footballLeagueMatchId) throws Exception {
        if(StringUtils.isEmpty(footballLeagueMatchId)){
            return null;
        }
        FootballIntegralExample example = new FootballIntegralExample();
        example.createCriteria().andFootballLeagueMatchIdEqualTo(StringUtils.trim(footballLeagueMatchId));
        List<FootballIntegral> integrals = footballIntegralMapper.selectByExample(example);
        return integrals;
    }

    @Override
    public List<FootballIntegral> queryFootballIntegralsBySeasonId(String footballSeasonId) throws Exception {
        if(StringUtils.isEmpty(footballSeasonId)){
            return null;
        }
        FootballIntegralExample example = new FootballIntegralExample();
        example.createCriteria().andFootballSeasonIdEqualTo(StringUtils.trim(footballSeasonId));
        List<FootballIntegral> integrals = footballIntegralMapper.selectByExample(example);
        return integrals;
    }

    @Override
    public List<FootballIntegral> queryFootballIntegralsBySeasonCategoryId(String footballSeasonCategoryId) throws Exception {
        if(StringUtils.isEmpty(footballSeasonCategoryId)){
            return null;
        }
        FootballIntegralExample example = new FootballIntegralExample();
        example.createCriteria().andFootballSeasonCategoryIdEqualTo(StringUtils.trim(footballSeasonCategoryId));
        return footballIntegralMapper.selectByExample(example);
    }

    @Override
    public List<FootballTeam> queryFootballTeamsBySeasonCategoryId(String footballSeasonCategoryId) throws Exception {
        List<FootballIntegral> integrals = queryFootballIntegralsBySeasonCategoryId(footballSeasonCategoryId);
        return getFootballTeams(integrals);
    }

    @Override
    public List<FootballTeam> queryFootballTeamsBySeasonId(String footballSeasonId)  throws Exception {
        List<FootballIntegral> integrals = queryFootballIntegralsBySeasonId(footballSeasonId);
        return getFootballTeams(integrals);
    }

    @Override
    public List<FootballTeam> queryFootballTeamsByLeagueMatchId(String footballLeagueMatchId) throws Exception {
        List<FootballIntegral> integrals = queryFootballIntegralsByLeagueMatchId(footballLeagueMatchId);
        return getFootballTeams(integrals);
    }

    private List<FootballTeam> getFootballTeams(List<FootballIntegral> integrals) throws Exception {
        if(null != integrals && integrals.size() > 0){
            List<String> teamIds = new ArrayList<String>();
            for (FootballIntegral integral : integrals) {
                teamIds.add(integral.getFootballTeamId());
            }
            FootballTeamExample teamExample = new FootballTeamExample();
            teamExample.createCriteria().andFootballTeamIdIn(teamIds);
            return footballTeamService.queryFootballTeamsByExample(teamExample);
        }else{
            return null;
        }
    }
}
