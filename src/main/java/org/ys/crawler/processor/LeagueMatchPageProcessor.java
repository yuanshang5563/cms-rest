package org.ys.crawler.processor;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.util.SiteUtils;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.common.util.UUIDGeneratorUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("leagueMatchPageProcessor")
public class LeagueMatchPageProcessor implements PageProcessor {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;

    @Override
    public void process(Page page) {
        Document document = page.getHtml().getDocument();
        Elements btnGroupDives = document.select("#app > main > div > div > div");
        if(null != btnGroupDives && btnGroupDives.size() > 0){
            List<FootballLeagueMatch> footballLeagueMatches = null;
            try {
                footballLeagueMatches = footballLeagueMatchService.queryAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String,FootballLeagueMatch> footballMatchMaps =  new HashMap<String,FootballLeagueMatch>();
            if (null != footballLeagueMatches && footballLeagueMatches.size() > 0){
                for(FootballLeagueMatch footballLeagueMatch : footballLeagueMatches){
                    footballMatchMaps.put(footballLeagueMatch.getFootballLeagueMatchLevel(),footballLeagueMatch);
                }
            }
            List<FootballLeagueMatch> matches = new ArrayList<FootballLeagueMatch>();
            for(Element btnGroupDiv : btnGroupDives){
                Elements btnGroupDives2 = btnGroupDiv.select("div.dropdown-menu");
                if(null != btnGroupDives2 && btnGroupDives2.size() > 0){
                    for(Element btnGroupDiv2 : btnGroupDives2){
                        String matchRegionName = null;
                        Elements h6Tag = btnGroupDiv2.getElementsByTag("h6");
                        if (null != h6Tag){
                            matchRegionName = h6Tag.text();
                        }
                        Elements aTags = btnGroupDiv2.getElementsByTag("a");
                        if(null != aTags && aTags.size() > 0){
                            for(Element aTag : aTags){
                                String matchUrl = aTag.attr("href");
                                String matchLevel = aTag.text();
                                if(StringUtils.isNotEmpty(matchLevel)){
                                    FootballLeagueMatch leagueMatch = footballMatchMaps.get(matchLevel.trim());
                                    if(null == leagueMatch){
                                        leagueMatch = new FootballLeagueMatch();
                                        leagueMatch.setFootballLeagueMatchId(UUIDGeneratorUtils.generateUUID());
                                        leagueMatch.setFootballLeagueMatchLevel(StringUtils.trim(matchLevel));
                                        leagueMatch.setRegionName(StringUtils.trim(matchRegionName));
                                        leagueMatch.setLeagueMatchUrl(StringUtils.trim(matchUrl));
                                        leagueMatch.setFootballLeagueMatchName(matchLevel);
                                        matches.add(leagueMatch);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            page.putField("matches",matches);
        }
    }

    @Override
    public Site getSite() {
        return SiteUtils.generateSite();
    }
}
