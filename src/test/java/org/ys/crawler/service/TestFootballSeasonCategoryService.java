package org.ys.crawler.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.model.FootballSeasonCategoryExample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestFootballSeasonCategoryService {
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;

    @Test
    public void testUpdateLeagueMatchId() throws Exception{
        List<FootballSeason> footballSeasonList = footballSeasonService.queryAll();
        Map<String,String> seasonMap = new HashMap<String,String>();
        if(null != footballSeasonList && footballSeasonList.size() > 0){
            for (FootballSeason season : footballSeasonList) {
                seasonMap.put(season.getFootballSeasonId(),season.getFootballLeagueMatchId());
            }
        }
        FootballSeasonCategoryExample example = new FootballSeasonCategoryExample();
        example.createCriteria().andFootballLeagueMatchIdIsNull();
        List<FootballSeasonCategory> seasonCategories = footballSeasonCategoryService.queryFootballSeasonCategoriesByExample(example);
        if(null != seasonCategories && seasonCategories.size() > 0){
            for (FootballSeasonCategory category : seasonCategories) {
                String seasonId = category.getFootballSeasonId();
                String leagueMatchId = seasonMap.get(seasonId);
                category.setFootballLeagueMatchId(leagueMatchId);
                footballSeasonCategoryService.updateById(category);
            }
        }
    }
}
