package org.ys.crawler.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.util.MathUtils;
import org.ys.common.util.SiteUtils;
import org.ys.common.util.UUIDGeneratorUtils;
import org.ys.crawler.model.*;
import org.ys.crawler.service.FootballIntegralService;
import org.ys.crawler.service.FootballTeamService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import javax.xml.ws.Action;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("FootballIntegralPageProcessor")
public class FootballIntegralPageProcessor implements PageProcessor {
    @Autowired
    private FootballIntegralService footballIntegralService;
    @Autowired
    private FootballTeamService footballTeamService;

    @Override
    public void process(Page page) {
        try {
            //是比分数据
            Json pageJson = page.getJson();
            FootballSeasonCategory seasonCategory = page.getRequest().getExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY);
            if(null != seasonCategory){
                List<FootballIntegral> footballIntegralList = new ArrayList<FootballIntegral>();
                List<FootballTeam> footballTeamsList = new ArrayList<FootballTeam>();
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
                        String leiDataIntegralId = StringUtils.trim(idNode.asText());
                        FootballIntegral footballIntegral = footballIntegralService.queryFootballIntegralByLeiDataId(leiDataIntegralId);
                        if(null == footballIntegral){
                            footballIntegral = new FootballIntegral();
                            footballIntegral.setLeiDataIntegralId(leiDataIntegralId);
                            footballIntegral.setFootballSeasonId(seasonCategory.getFootballSeasonId());
                            footballIntegral.setFootballSeasonCategoryId(seasonCategory.getFootballSeasonCategoryId());
                            footballIntegral.setFootballLeagueMatchId(seasonCategory.getFootballLeagueMatchId());
                            JsonNode entityIdNode = node.path("EntityId");
                            if(null != entityIdNode){
                                String entityId = StringUtils.trim(entityIdNode.asText());
                                FootballTeam team = footballTeamService.queryFootballTeamByEntityId(entityId);
                                if(null == team){
                                   //找不到队伍就创建
                                    team = new FootballTeam();
                                    JsonNode entityNameNode = node.path("EntityName");
                                    if(null != entityNameNode){
                                        team.setTeamName(StringUtils.trim(entityNameNode.asText()));
                                    }
                                    team.setFootballTeamId(UUIDGeneratorUtils.generateUUID());
                                    team.setEntityId(StringUtils.trim(entityId));
                                    footballTeamsList.add(team);
                                }
                                footballIntegral.setFootballTeamId(team.getFootballTeamId());
                            }
                        }
                        JsonNode playedTotalNode = node.path("PlayedTotal");
                        if(null != playedTotalNode){
                            footballIntegral.setPlayedTotal(playedTotalNode.asInt());
                        }
                        JsonNode winTotalNode = node.path("WinTotal");
                        if(null != winTotalNode){
                            footballIntegral.setWinTotal(winTotalNode.asInt());
                        }
                        JsonNode drawTotalNode = node.path("DrawTotal");
                        if(null != drawTotalNode){
                            footballIntegral.setDrawTotal(drawTotalNode.asInt());
                        }
                        JsonNode lostTotalNode = node.path("LostTotal");
                        if(null != lostTotalNode){
                            footballIntegral.setLostTotal(lostTotalNode.asInt());
                        }
                        JsonNode scoredTotalNode = node.path("ScoredTotal");
                        if(null != scoredTotalNode){
                            footballIntegral.setGoalTotal(scoredTotalNode.asInt());
                        }
                        JsonNode againstTotalNode = node.path("AgainstTotal");
                        if(null != againstTotalNode){
                            footballIntegral.setFumbleTotal(againstTotalNode.asInt());
                        }
                        JsonNode differenceNode = node.path("DifferenceTotal");
                        if(null != differenceNode){
                            footballIntegral.setDifferenceTotal(differenceNode.asInt());
                        }
                        JsonNode pointsTotalNode = node.path("PointsTotal");
                        if(null != pointsTotalNode){
                            footballIntegral.setIntegralTotal(pointsTotalNode.asInt());
                        }
                        JsonNode winHomeNode = node.path("WinHome");
                        if(null != winHomeNode){
                            footballIntegral.setWinHome(winHomeNode.asInt());
                        }
                        JsonNode winAwayNode = node.path("WinAway");
                        if(null != winAwayNode){
                            footballIntegral.setWinAway(winAwayNode.asInt());
                        }
                        JsonNode drawHomeNode = node.path("DrawHome");
                        if(null != drawHomeNode){
                            footballIntegral.setDrawHome(drawHomeNode.asInt());
                        }
                        JsonNode drawAwayNode = node.path("DrawAway");
                        if(null != drawAwayNode){
                            footballIntegral.setDrawAway(drawAwayNode.asInt());
                        }
                        JsonNode lostHomeNode = node.path("LostHome");
                        if(null != lostHomeNode){
                            footballIntegral.setLostHome(lostHomeNode.asInt());
                        }
                        JsonNode lostAwayNode = node.path("LostAway");
                        if(null != lostAwayNode){
                            footballIntegral.setLostAway(lostAwayNode.asInt());
                        }
                        JsonNode scoredHomeNode = node.path("ScoredHome");
                        if(null != scoredHomeNode){
                            footballIntegral.setGoalHome(scoredHomeNode.asInt());
                        }
                        JsonNode scoredAwayNode = node.path("ScoredAway");
                        if(null != scoredAwayNode){
                            footballIntegral.setGoalAway(scoredAwayNode.asInt());
                        }
                        JsonNode againstHomeNode = node.path("AgainstHome");
                        if(null != againstHomeNode){
                            footballIntegral.setFumbleHome(againstHomeNode.asInt());
                        }
                        JsonNode againstAwayNode = node.path("AgainstAway");
                        if(null != againstAwayNode){
                            footballIntegral.setFumbleAway(againstAwayNode.asInt());
                        }
                        JsonNode roundNode = node.path("Round");
                        if(null != roundNode){
                            int round = roundNode.asInt();
                            if(null != footballIntegral.getGoalTotal()){
                                footballIntegral.setGoalPerPlayed(MathUtils.mathFloat(footballIntegral.getGoalTotal(),round));
                            }
                            if(null != footballIntegral.getFumbleTotal()){
                                footballIntegral.setFumblePerPlayed(MathUtils.mathFloat(footballIntegral.getFumbleTotal(),round));
                            }
                            if(null != footballIntegral.getGoalPerPlayed() && null != footballIntegral.getFumblePerPlayed()){
                                footballIntegral.setWinPerPlayed(footballIntegral.getGoalPerPlayed()-footballIntegral.getFumblePerPlayed());
                            }
                        }
                        footballIntegralList.add(footballIntegral);
                        page.putField("footballIntegralList",footballIntegralList);
                        page.putField("footballTeamsList",footballTeamsList);
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

}
