package org.ys.crawler.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.util.SiteUtils;
import org.ys.common.util.UUIDGeneratorUtils;
import org.ys.crawler.model.FootballPlayer;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballScoreDetail;
import org.ys.crawler.service.FootballPlayerService;
import org.ys.crawler.service.FootballScoreDetailService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("footballScoreDetailPageProcessor")
public class FootballScoreDetailPageProcessor implements PageProcessor {
    @Autowired
    private FootballScoreDetailService footballScoreDetailService;
    @Autowired
    private FootballPlayerService footballPlayerService;

    @Override
    public void process(Page page) {
        try {
            //是比分数据
            Json pageJson = page.getJson();
            FootballScore footballScore = page.getRequest().getExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SCORE);
            if(null != footballScore){
                List<FootballScore> footballScoreList = new ArrayList<FootballScore>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setDateFormat(simpleDateFormat);
                JsonNode jsonNode = objectMapper.readTree(pageJson.toString());
                try {
                    Map<String,FootballPlayer> playerMap = new HashMap<String,FootballPlayer>();
                    Map<String,Set<FootballScoreDetail>> scoreDetailMap = new HashMap<String,Set<FootballScoreDetail>>();
                    //先解析球员，如果不在就创建
                    JsonNode matchLineupsNode = jsonNode.path("MatchLineups");
                    if(null != matchLineupsNode && matchLineupsNode.size() > 0){
                        Iterator<JsonNode> lineupsIter = matchLineupsNode.iterator();
                        while (lineupsIter.hasNext()){
                            JsonNode lineup = lineupsIter.next();
                            JsonNode entityIdNode = lineup.path("EntityId");
                            if(null == entityIdNode){
                                continue;
                            }
                            String entityId = StringUtils.trim(entityIdNode.asText());
                            FootballPlayer footballPlayer = footballPlayerService.queryFootballPlayerByEntityId(entityId);
                            if(null == footballPlayer){
                                footballPlayer = new FootballPlayer();
                                footballPlayer.setFootballPlayerId(UUIDGeneratorUtils.generateUUID());
                                footballPlayer.setActiveFlag(true);
                                footballPlayer.setEntityId(entityId);
                            }
                            JsonNode entityNameNode = lineup.path("EntityName");
                            if(null != entityNameNode){
                                footballPlayer.setFootballPlayerName(StringUtils.trim(entityNameNode.asText()));
                            }
                            JsonNode positionNameNode = lineup.path("PositionName");
                            if(null != positionNameNode){
                                footballPlayer.setFootballPlayerType(StringUtils.trim(positionNameNode.asText()));
                            }
                            JsonNode playerIsHomeNode = lineup.path("IsHome");
                            if(null != playerIsHomeNode){
                                if(playerIsHomeNode.asBoolean()){
                                    footballPlayer.setFootballTeamId(footballScore.getHomeFootballTeamId());
                                }else{
                                    footballPlayer.setFootballTeamId(footballScore.getAwayFootballTeamId());
                                }
                            }
                            playerMap.put(entityId,footballPlayer);
                        }
                    }
                    //比赛事件数据解析
                    JsonNode matchEventsNode = jsonNode.path("MatchEvents");
                    JsonNode idNode = jsonNode.path("Id");
                    if(null != matchEventsNode && matchEventsNode.size() > 0){
                        Iterator<JsonNode> eventsIter = matchEventsNode.iterator();
                        while (eventsIter.hasNext()){
                            JsonNode event = eventsIter.next();
                            JsonNode typeName = event.path("TypeName");
                            if(null != typeName && StringUtils.equals("进球",typeName.asText())){
                                FootballScoreDetail scoreDetail = new FootballScoreDetail();
                                if(null != idNode){
                                    String id = idNode.asText();
                                    scoreDetail.setLeidataScoreId(StringUtils.trim(id));
                                }
                                scoreDetail.setFootballScoreDetailId(UUIDGeneratorUtils.generateUUID());
                                scoreDetail.setFootballScoreId(footballScore.getFootballScoreId());
                                JsonNode timeNode = event.path("Time");
                                if(null != timeNode){
                                    scoreDetail.setGoalTime(simpleDateFormat.parse(StringUtils.trim(timeNode.asText())));
                                }
                                JsonNode isHomeNode = event.path("IsHome");
                                if(null != isHomeNode){
                                    boolean isHome = isHomeNode.asBoolean();
                                    scoreDetail.setIsHome(isHome);
                                    if(isHome){
                                        scoreDetail.setGoalFootballTeamId(footballScore.getHomeFootballTeamId());
                                    }else{
                                        scoreDetail.setGoalFootballTeamId(footballScore.getAwayFootballTeamId());
                                    }
                                }
                                JsonNode periodNameNode = event.path("PeriodName");
                                if(null != periodNameNode){
                                    scoreDetail.setFootballPeriodName(StringUtils.trim(periodNameNode.asText()));
                                }
                                JsonNode entityId1Node = event.path("EntityId1");
                                if(null != entityId1Node){
                                    String entityId1 = StringUtils.trim(entityId1Node.asText());
                                    FootballPlayer footballPlayer = playerMap.get(entityId1);
                                    if(null != footballPlayer){
                                        scoreDetail.setFootballPlayerId(footballPlayer.getFootballPlayerId());
                                    }
                                }
                                String scoreId = footballScore.getFootballScoreId();
                                Set<FootballScoreDetail> detailSet = scoreDetailMap.get(scoreId);
                                if(null == detailSet){
                                    detailSet = new HashSet<FootballScoreDetail>();
                                }
                                detailSet.add(scoreDetail);
                                scoreDetailMap.put(scoreId,detailSet);
                            }
                        }
                    }
                    page.putField("scoreDetailMap",scoreDetailMap);
                    page.putField("playerMap",playerMap);
                } catch (Exception e) {
                    e.printStackTrace();
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
}
