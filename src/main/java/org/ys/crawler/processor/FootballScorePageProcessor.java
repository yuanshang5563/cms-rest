package org.ys.crawler.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.util.SiteUtils;
import org.ys.common.util.UUIDGeneratorUtils;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.model.FootballTeam;
import org.ys.crawler.service.FootballScoreService;
import org.ys.crawler.service.FootballSeasonService;
import org.ys.crawler.service.FootballTeamService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("footballScorePageProcessor")
public class FootballScorePageProcessor implements PageProcessor {
    @Autowired
    private FootballScoreService footballScoreService;
    @Autowired
    private FootballTeamService footballTeamService;
    @Autowired
    private FootballSeasonService footballSeasonService;

    @Override
    public void process(Page page) {
        try {
            //是比分数据
            Json pageJson = page.getJson();
            FootballSeasonCategory seasonCategory = page.getRequest().getExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY);
            if(null != seasonCategory){
                List<FootballScore> footballScoreList = new ArrayList<FootballScore>();
                Map<String,FootballTeam> teamMap = new HashMap<String,FootballTeam>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setDateFormat(simpleDateFormat);
                JsonNode jsonNode = objectMapper.readTree(pageJson.toString());
                Iterator<JsonNode> jsonNodeIter = jsonNode.iterator();
                while (jsonNodeIter.hasNext()){
                    try {
                        JsonNode node = jsonNodeIter.next();
                        JsonNode idNode = node.path("Id");
                        //没有id直接跳过
                        if(null == idNode){
                            continue;
                        }
                        String leidataScoreId = StringUtils.trim(idNode.asText());
                        FootballScore footballScore = footballScoreService.queryFootballScoreByLeiDataId(leidataScoreId);
                        if(null == footballScore){
                            footballScore = new FootballScore();
                            footballScore.setLeidataScoreId(leidataScoreId);
                            footballScore.setFootballSeasonId(seasonCategory.getFootballSeasonId());
                            footballScore.setFootballSeasonCategoryId(seasonCategory.getFootballSeasonCategoryId());
                            FootballSeason footballSeason = footballSeasonService.queryFootballSeasonById(seasonCategory.getFootballSeasonId());
                            if(null != footballSeason){
                                footballScore.setFootballLeagueMatchId(footballSeason.getFootballLeagueMatchId());
                            }
                        }
                        JsonNode roundNode = node.path("Round");
                        if(null != roundNode){
                            footballScore.setRound(StringUtils.trim(roundNode.asText()));
                        }
                        JsonNode dateNode = node.path("Date");
                        if(null != dateNode){
                            footballScore.setMatchDate(simpleDateFormat.parse(StringUtils.trim(dateNode.asText())));
                        }
                        JsonNode matchResultsNode = node.path("MatchResults");
                        if(null != matchResultsNode && matchResultsNode.size() > 0){
                            Iterator<JsonNode> resultsIter = matchResultsNode.iterator();
                            while (resultsIter.hasNext()){
                                JsonNode result = resultsIter.next();
                                JsonNode typeName = result.path("TypeName");
                                if(null != typeName && StringUtils.equals("半场",typeName.asText())){
                                    footballScore.setHalfHomeScore(result.path("HomeScore").asInt());
                                    footballScore.setHalfAwayScore(result.path("AwayScore").asInt());
                                }
                                if(null != typeName && StringUtils.equals("全场",typeName.asText())){
                                    footballScore.setHomeScore(result.path("HomeScore").asInt());
                                    footballScore.setAwayScore(result.path("AwayScore").asInt());
                                }
                            }
                        }
                        JsonNode regionNameNode = node.path("RegionName");
                        String regionName = null;
                        //没有id直接跳过
                        if(null != regionNameNode){
                            regionName = regionNameNode.asText();
                        }
                        //如果球队不存在，创建球队
                        JsonNode homeTeamNode = node.path("HomeTeam");
                        if(null != homeTeamNode && null != homeTeamNode.get(0)){
                            JsonNode home = homeTeamNode.get(0);
                            if(null != home){
                                FootballTeam homeTeam = getFootballTeam(teamMap,home,regionName);
                                footballScore.setHomeFootballTeamId(homeTeam.getFootballTeamId());
                            }
                        }
                        JsonNode awayTeamNode = node.path("AwayTeam");
                        if(null != awayTeamNode && null != awayTeamNode.get(0)){
                            JsonNode away = awayTeamNode.get(0);
                            if(null != away){
                                FootballTeam awayTeam = getFootballTeam(teamMap,away,regionName);
                                footballScore.setAwayFootballTeamId(awayTeam.getFootballTeamId());
                            }
                        }
                        footballScoreList.add(footballScore);
                        page.putField("teamMap",teamMap);
                        page.putField("footballScoreList",footballScoreList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return SiteUtils.generateSite();
    }

    private FootballTeam getFootballTeam(Map<String,FootballTeam> teamMap,JsonNode node,String regionName){
        FootballTeam team = null;
        try {
            String entityId = StringUtils.trim(node.path("id").asText());
            String teamName = StringUtils.trim(node.path("name").asText());
            team = footballTeamService.queryFootballTeamByEntityId(entityId);
            if(null == team){
                team = teamMap.get(teamName);
                if(null == team){
                    team = new FootballTeam();
                    team.setTeamName(StringUtils.trim(teamName));
                    team.setFootballTeamId(UUIDGeneratorUtils.generateUUID());
                    team.setCountry(StringUtils.trim(regionName));
                    team.setEntityId(StringUtils.trim(entityId));
                    teamMap.put(teamName,team);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return team;
    }
}
