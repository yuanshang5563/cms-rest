package org.ys.crawler.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.crawler.dao.FootballTeamMapper;
import org.ys.crawler.model.FootballTeam;
import org.ys.crawler.model.FootballTeamExample;
import org.ys.crawler.service.FootballTeamService;

import java.util.List;

@Service("footballTeamService")
public class FootballTeamServiceImpl implements FootballTeamService {
    @Autowired
    private FootballTeamMapper footballTeamMapper;

    @Override
    public FootballTeam queryFootballTeamById(String footballTeamId) throws Exception {
        if (StringUtils.isEmpty(footballTeamId)){
            return null;
        }
        return footballTeamMapper.selectByPrimaryKey(footballTeamId);
    }

    @Override
    public int save(FootballTeam footballTeam) throws Exception {
        if(null != footballTeam){
            return footballTeamMapper.insert(footballTeam);
        }
        return 0;
    }

    @Override
    public int updateById(FootballTeam footballTeam) throws Exception {
        if(null != footballTeam){
            return footballTeamMapper.updateByPrimaryKey(footballTeam);
        }
        return 0;
    }

    @Override
    public int updateByExample(FootballTeam footballTeam, FootballTeamExample example) throws Exception {
        if (null != footballTeam && null != example){
            return footballTeamMapper.updateByExample(footballTeam,example);
        }
        return 0;
    }

    @Override
    public int delFootballTeamById(String footballTeamId) throws Exception {
        if(StringUtils.isNotEmpty(footballTeamId)){
            return footballTeamMapper.deleteByPrimaryKey(footballTeamId);
        }
        return 0;
    }

    @Override
    public List<FootballTeam> queryFootballTeamsByExample(FootballTeamExample example) throws Exception {
        if(null == example){
            return null;
        }
        return footballTeamMapper.selectByExample(example);
    }

    @Override
    public List<FootballTeam> queryAll() throws Exception {
        FootballTeamExample example = new FootballTeamExample();
        return footballTeamMapper.selectByExample(example);
    }

    @Override
    public PageBean<FootballTeam> pageFootballTeamsByExample(FootballTeamExample example, int pageNum, int pageSize) throws Exception {
        if(null == example) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<FootballTeam> footballTeams = footballTeamMapper.selectByExample(example);
        return new PageBean<FootballTeam>(footballTeams);
    }

    @Override
    public FootballTeam queryFootballTeamByTeamName(String teamName) throws Exception {
        if(StringUtils.isEmpty(teamName)){
            return null;
        }
        FootballTeamExample example = new FootballTeamExample();
        FootballTeamExample.Criteria criteria = example.createCriteria().andTeamNameEqualTo(StringUtils.trim(teamName));
        FootballTeamExample.Criteria criteria1 = example.createCriteria().andTeamOtherNameEqualTo(StringUtils.trim(teamName));
        example.or(criteria1);
        List<FootballTeam> footballTeams = footballTeamMapper.selectByExample(example);
        if(null != footballTeams && footballTeams.size() > 0){
            return footballTeams.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<FootballTeam> queryFootballTeamsByCountry(String country) throws Exception {
        if(StringUtils.isEmpty(country)){
            return null;
        }
        FootballTeamExample example = new FootballTeamExample();
        example.createCriteria().andCountryEqualTo(StringUtils.trim(country));
        return footballTeamMapper.selectByExample(example);
    }
}
