package org.ys.crawler.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.constant.LeidataCrawlerConstant;
import org.ys.common.util.SiteUtils;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.service.FootballSeasonCategoryService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component("footballSeasonCategoryPageProcessor")
public class FootballSeasonCategoryPageProcessor implements PageProcessor {
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;

    @Override
    public void process(Page page) {
        try {
            FootballSeason footballSeason = page.getRequest().getExtra(LeidataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON);
            if(null != footballSeason){
                List<FootballSeasonCategory> seasonCategoryList = new ArrayList<FootballSeasonCategory>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Json pageJson = page.getJson();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setDateFormat(simpleDateFormat);
                JsonNode jsonNode = objectMapper.readTree(pageJson.toString());
                List<JsonNode> itemList = jsonNode.findValues("Item");
                if(null != itemList && itemList.size() > 0){
                    Iterator<JsonNode> iterator = itemList.iterator();
                    while (iterator.hasNext()){
                        JsonNode itemNode = iterator.next();
                        if(null != itemNode){
                            JsonNode nameNode = itemNode.findValue("Name");
                            if(null != nameNode && StringUtils.isNotEmpty(nameNode.asText())){
                                JsonNode parentIdNode = itemNode.findValue("ParentId");
                                String parentId = footballSeason.getFootballSeasonUrl().replaceAll("/","");
                                if(null != parentIdNode && StringUtils.equals(parentId,StringUtils.trim(parentIdNode.asText()))){
                                    String seasonCategoryName = StringUtils.trim(nameNode.asText());
                                    String seasonCategoryUrl = StringUtils.trim(itemNode.findValue("Id").asText());
                                    FootballSeasonCategory seasonCategory = footballSeasonCategoryService.queryFootballSeasonCategoryBySeasonAndName(footballSeason.getFootballSeasonId(), seasonCategoryName);
                                    if(null == seasonCategory){
                                        seasonCategory = new FootballSeasonCategory();
                                        seasonCategory.setFootballSeasonId(footballSeason.getFootballSeasonId());
                                        seasonCategory.setFootballSeasonCategoryName(seasonCategoryName);
                                    }
                                    seasonCategory.setFootballSeasonCategoryUrl(seasonCategoryUrl);
                                    seasonCategoryList.add(seasonCategory);
                                }
                            }
                        }
                    }
                }
                page.putField("seasonCategoryList",seasonCategoryList);
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
