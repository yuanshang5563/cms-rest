package org.ys.crawler.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.util.SiteUtils;
import org.ys.crawler.model.FootballSeasonCategory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component("footballRoundPageProcessor")
public class FootballRoundPageProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        FootballSeasonCategory footballSeasonCategory = page.getRequest().getExtra(LeiDataCrawlerConstant.LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY);
        Json pageJson = page.getJson();
        if(null != pageJson){
            List<Selectable> totalNode = pageJson.nodes();
            //如果有轮数，按轮数查询,由于无轮数是1
            if(null != totalNode && totalNode.size() > 0 && !StringUtils.equals(totalNode.get(0).toString(),"[]")){
                String[] strings = totalNode.get(0).toString().replace("[", "").replace("]", "")
                        .split(",");
                footballSeasonCategory.setRoundCount(strings.length);
            }else{
                footballSeasonCategory.setRoundCount(-1);
            }
            page.putField("footballSeasonCategory",footballSeasonCategory);
        }
    }

    @Override
    public Site getSite() {
        return SiteUtils.generateSite();
    }

}
