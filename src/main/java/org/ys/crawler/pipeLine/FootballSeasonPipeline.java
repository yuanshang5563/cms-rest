package org.ys.crawler.pipeLine;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.util.UUIDGeneratorUtils;
import org.ys.crawler.model.FootballSeason;
import org.ys.crawler.service.FootballSeasonService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("footballSeasonPipeline")
public class FootballSeasonPipeline implements Pipeline {
    @Autowired
    private FootballSeasonService footballSeasonService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<FootballSeason> seasonList = resultItems.get("seasonList");
        if(null != seasonList && seasonList.size() > 0){
            try {
                for(FootballSeason season : seasonList){
                    if(StringUtils.isEmpty(season.getFootballSeasonId())){
                        season.setFootballSeasonId(UUIDGeneratorUtils.generateUUID());
                        footballSeasonService.save(season);
                    }else{
                        footballSeasonService.updateById(season);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
