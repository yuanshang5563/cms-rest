package org.ys.crawler.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballScoreExample;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestFootballScoreService {
    @Autowired
    private FootballScoreService footballScoreService;

    @Test
    public void TestSelectLeagueMatches() throws Exception{
        String homeFootballTeamId = "260752da1527404cbb592b6c16330dfb";
        String awayFootballTeamId = "23135cf7fce245b4838ca7ca3b8f9299";
        FootballScoreExample example = new FootballScoreExample();
        FootballScoreExample.Criteria criteria1 = example.createCriteria();
        FootballScoreExample.Criteria criteria2 = example.createCriteria();
        FootballScoreExample.Criteria criteria3 = example.createCriteria();
        FootballScoreExample.Criteria criteria4 = example.createCriteria();
        if(StringUtils.isNotEmpty(homeFootballTeamId)){
            criteria1.andHomeFootballTeamIdEqualTo(homeFootballTeamId);
            criteria2.andAwayFootballTeamIdEqualTo(homeFootballTeamId);
        }
        if(StringUtils.isNotEmpty(awayFootballTeamId)){
            criteria3.andHomeFootballTeamIdEqualTo(awayFootballTeamId);
            criteria4.andAwayFootballTeamIdEqualTo(awayFootballTeamId);
        }
        example.or(criteria2);
        example.or(criteria3);
        example.or(criteria4);
        List<String> leagueMatchIds = footballScoreService.selectLeagueMatchesByExample(example);
        if(null != leagueMatchIds && leagueMatchIds.size() > 0){
            for (String leagueMatchId : leagueMatchIds) {
                System.out.println("------------ " + leagueMatchId);
            }
        }
    }
}
