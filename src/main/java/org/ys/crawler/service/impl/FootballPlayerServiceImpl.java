package org.ys.crawler.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.crawler.dao.FootballPlayerMapper;
import org.ys.crawler.model.FootballPlayer;
import org.ys.crawler.model.FootballPlayerExample;
import org.ys.crawler.service.FootballPlayerService;

import java.util.List;

@Service("footballPlayerService")
public class FootballPlayerServiceImpl implements FootballPlayerService {
    @Autowired
    private FootballPlayerMapper footballPlayerMapper;

    @Override
    public FootballPlayer queryFootballPlayerById(String footballPlayerId) throws Exception {
        if (StringUtils.isEmpty(footballPlayerId)){
            return null;
        }
        return footballPlayerMapper.selectByPrimaryKey(footballPlayerId);
    }

    @Override
    public int save(FootballPlayer footballPlayer) throws Exception {
        if(null != footballPlayer){
            return footballPlayerMapper.insert(footballPlayer);
        }
        return 0;
    }

    @Override
    public int updateById(FootballPlayer footballPlayer) throws Exception {
        if(null != footballPlayer){
            return footballPlayerMapper.updateByPrimaryKey(footballPlayer);
        }
        return 0;
    }

    @Override
    public int updateByExample(FootballPlayer footballPlayer, FootballPlayerExample example) throws Exception {
        if (null != footballPlayer && null != example){
            return footballPlayerMapper.updateByExample(footballPlayer,example);
        }
        return 0;
    }

    @Override
    public int delFootballPlayerById(String footballPlayerId) throws Exception {
        if(StringUtils.isNotEmpty(footballPlayerId)){
            return footballPlayerMapper.deleteByPrimaryKey(footballPlayerId);
        }
        return 0;
    }

    @Override
    public List<FootballPlayer> queryFootballPlayersByExample(FootballPlayerExample example) throws Exception {
        if(null == example){
            return null;
        }
        return footballPlayerMapper.selectByExample(example);
    }

    @Override
    public List<FootballPlayer> queryAll() throws Exception {
        FootballPlayerExample example = new FootballPlayerExample();
        return footballPlayerMapper.selectByExample(example);
    }

    @Override
    public PageBean<FootballPlayer> pageFootballPlayersByExample(FootballPlayerExample example, int pageNum, int pageSize) throws Exception {
        if(null == example) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<FootballPlayer> footballPlayers = footballPlayerMapper.selectByExample(example);
        return new PageBean<FootballPlayer>(footballPlayers);
    }

    @Override
    public List<FootballPlayer> queryFootballPlayersByTeamId(String footballTeamId) throws Exception {
        if(StringUtils.isEmpty(footballTeamId)){
            return null;
        }
        FootballPlayerExample example = new FootballPlayerExample();
        example.createCriteria().andFootballTeamIdEqualTo(StringUtils.trim(footballTeamId));
        List<FootballPlayer> footballPlayers = footballPlayerMapper.selectByExample(example);
        return footballPlayers;
    }

    @Override
    public List<FootballPlayer> queryFootballPlayersByName(String footballPlayerName) throws Exception {
        if(StringUtils.isEmpty(footballPlayerName)){
            return null;
        }
        FootballPlayerExample example = new FootballPlayerExample();
        example.createCriteria().andFootballPlayerNameEqualTo(StringUtils.trim(footballPlayerName));
        List<FootballPlayer> footballPlayers = footballPlayerMapper.selectByExample(example);
        return footballPlayers;
    }

    @Override
    public FootballPlayer queryFootballPlayerByTeamAndName(String footballTeamId, String footballPlayName) throws Exception {
        if(StringUtils.isEmpty(footballTeamId) || StringUtils.isEmpty(footballPlayName)){
            return null;
        }
        FootballPlayerExample example = new FootballPlayerExample();
        example.createCriteria().andFootballPlayerNameEqualTo(StringUtils.trim(footballPlayName))
        .andFootballTeamIdEqualTo(StringUtils.trim(footballTeamId));
        List<FootballPlayer> footballPlayers = footballPlayerMapper.selectByExample(example);
        if(null != footballPlayers && footballPlayers.size() > 0){
            return footballPlayers.get(0);
        }else{
            return null;
        }
    }

    @Override
    public FootballPlayer queryFootballPlayerByEntityId(String entityId) throws Exception {
        if(StringUtils.isEmpty(entityId)){
            return null;
        }
        FootballPlayerExample example = new FootballPlayerExample();
        example.createCriteria().andEntityIdEqualTo(StringUtils.trim(entityId));
        List<FootballPlayer> footballPlayers = footballPlayerMapper.selectByExample(example);
        if(null != footballPlayers && footballPlayers.size() > 0){
            return footballPlayers.get(0);
        }else{
            return null;
        }
    }
}
