package org.ys.crawler.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.constant.LeidataCrawlerConstant;
import org.ys.common.util.SiteUtils;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.service.FootballSeasonService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component("footballSeasonPageProcessor")
public class FootballSeasonPageProcessor implements PageProcessor {
    @Autowired
    private FootballSeasonService footballSeasonService;

    @Override
    public void process(Page page) {
        try {
            String footballLeagueMatchId = page.getRequest().getExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_LEAGUE_MATCH_ID);
            if(StringUtils.isNotEmpty(footballLeagueMatchId)){
                List<FootballSeason> seasonList = new ArrayList<FootballSeason>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Json pageJson = page.getJson();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setDateFormat(simpleDateFormat);
                JsonNode jsonNode = objectMapper.readTree(pageJson.toString());
                JsonNode seasonTreeList = jsonNode.get("SeasonTreeList");
                if(null != seasonTreeList){
                    Iterator<JsonNode> iterator = seasonTreeList.iterator();
                    while (iterator.hasNext()){
                        JsonNode listJsonNode = iterator.next();
                        JsonNode itemNode = listJsonNode.get("Item");
                        if(null != itemNode){
                            JsonNode nameNode = itemNode.findValue("Name");
                            if(null != nameNode && StringUtils.isNotEmpty(nameNode.asText())){
                                String seasonName = StringUtils.trim(nameNode.asText());
                                String seasonUrl = StringUtils.trim(itemNode.findValue("Path").asText());
                                String beginDateStr = itemNode.findValue("BeginDate").asText();
                                Date beginDate = simpleDateFormat.parse(beginDateStr);
                                String endDateStr = itemNode.findValue("EndDate").asText();
                                Date endDate = simpleDateFormat.parse(endDateStr);
                                FootballSeason season = footballSeasonService.queryFootballSeasonByLeagueMatchAndName(footballLeagueMatchId,seasonName);
                                if(null == season){
                                    season = new FootballSeason();
                                    season.setFootballLeagueMatchId(StringUtils.trim(footballLeagueMatchId));
                                    season.setFootballSeasonName(seasonName);
                                    season.setFootballSeasonUrl(seasonUrl);
                                    season.setSeasonBeginDate(beginDate);
                                    season.setSeasonEndDate(endDate);
                                }else{
                                    //如果存在更新url
                                    season.setFootballSeasonUrl(seasonUrl);
                                }
                                seasonList.add(season);
                            }
                        }
                    }
                }
                page.putField("seasonList",seasonList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return SiteUtils.generateSite();
    }
}
