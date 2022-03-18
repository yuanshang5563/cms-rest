package org.ys.crawler.pipeLine;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.util.UUIDGeneratorUtils;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.service.FootballSeasonCategoryService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component("footballRoundPipeline")
public class FootballRoundPipeline implements Pipeline {
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        FootballSeasonCategory seasonCategory = resultItems.get("footballSeasonCategory");
        if(null != seasonCategory){
            try {
                footballSeasonCategoryService.updateById(seasonCategory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
