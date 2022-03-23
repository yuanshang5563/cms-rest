package org.ys.crawler.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.crawler.dao.FootballScoreMapper;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballScoreExample;
import org.ys.crawler.service.FootballScoreService;

import java.util.List;
import java.util.Set;

@Service("footballScoreService")
public class FootballScoreServiceImpl implements FootballScoreService {
    @Autowired
    private FootballScoreMapper footballScoreMapper;

    @Override
    public FootballScore queryFootballScoreById(String footballScoreId) throws Exception {
        if (StringUtils.isEmpty(footballScoreId)){
            return null;
        }
        return footballScoreMapper.selectByPrimaryKey(footballScoreId);
    }

    @Override
    public int save(FootballScore footballScore) throws Exception {
        if(null != footballScore){
            return footballScoreMapper.insert(footballScore);
        }
        return 0;
    }

    @Override
    public int updateById(FootballScore footballScore) throws Exception {
        if(null != footballScore){
            return footballScoreMapper.updateByPrimaryKey(footballScore);
        }
        return 0;
    }

    @Override
    public int updateByExample(FootballScore footballScore, FootballScoreExample example) throws Exception {
        if (null != footballScore && null != example){
            return footballScoreMapper.updateByExample(footballScore,example);
        }
        return 0;
    }

    @Override
    public int delFootballScoreById(String footballScoreId) throws Exception {
        if(StringUtils.isNotEmpty(footballScoreId)){
            return footballScoreMapper.deleteByPrimaryKey(footballScoreId);
        }
        return 0;
    }

    @Override
    public List<FootballScore> queryFootballScoresByExample(FootballScoreExample example) throws Exception {
        if(null == example){
            return null;
        }
        return footballScoreMapper.selectByExample(example);
    }

    @Override
    public List<FootballScore> queryAll() throws Exception {
        FootballScoreExample example = new FootballScoreExample();
        return footballScoreMapper.selectByExample(example);
    }

    @Override
    public PageBean<FootballScore> pageFootballScoresByExample(FootballScoreExample example, int pageNum, int pageSize) throws Exception {
        if(null == example) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize, true);
        List<FootballScore> footballScores = footballScoreMapper.selectByExample(example);
        return new PageBean<FootballScore>(footballScores);
    }

    @Override
    public FootballScore queryFootballScoreByLeiDataId(String leidataScoreId) throws Exception {
        if(StringUtils.isEmpty(leidataScoreId)){
            return null;
        }
        FootballScoreExample example = new FootballScoreExample();
        example.createCriteria().andLeidataScoreIdEqualTo(StringUtils.trim(leidataScoreId));
        List<FootballScore> footballScores = footballScoreMapper.selectByExample(example);
        if(null != footballScores && footballScores.size() > 0){
            return footballScores.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<FootballScore> queryFootballScoresBySeasonId(String footballSeasonId) throws Exception {
        if(StringUtils.isEmpty(footballSeasonId)){
            return null;
        }
        FootballScoreExample example = new FootballScoreExample();
        example.createCriteria().andFootballSeasonIdEqualTo(StringUtils.trim(footballSeasonId));
        List<FootballScore> footballScores = footballScoreMapper.selectByExample(example);
        return footballScores;
    }

    @Override
    public List<FootballScore> queryFootballScoresByLeagueMatchId(String footballLeagueMatchId) throws Exception {
        if(StringUtils.isEmpty(footballLeagueMatchId)){
            return null;
        }
        FootballScoreExample example = new FootballScoreExample();
        example.createCriteria().andFootballLeagueMatchIdEqualTo(StringUtils.trim(footballLeagueMatchId));
        return footballScoreMapper.selectByExample(example);
    }

    @Override
    public List<FootballScore> queryFootballScoresBySeasonCategoryId(String footballSeasonCategoryId) throws Exception {
        if(StringUtils.isEmpty(footballSeasonCategoryId)){
            return null;
        }
        FootballScoreExample example = new FootballScoreExample();
        example.createCriteria().andFootballSeasonCategoryIdEqualTo(StringUtils.trim(footballSeasonCategoryId));
        return footballScoreMapper.selectByExample(example);
    }

    @Override
    public boolean isExistsScoreByCategoryId(Set<String> categoryIds, String footballSeasonCategoryId) throws Exception {
        if(StringUtils.isEmpty(footballSeasonCategoryId) || null == categoryIds){
            return false;
        }
        boolean containsFlag = categoryIds.contains(footballSeasonCategoryId);
        if(containsFlag){
            return true;
        }else{
            List<FootballScore> scores = queryFootballScoresBySeasonCategoryId(footballSeasonCategoryId);
            if(null != scores && scores.size() > 0){
                categoryIds.add(footballSeasonCategoryId);
                return true;
            }
        }
        return false;
    }
}
