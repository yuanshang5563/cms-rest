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

@Component("footballSeasonCategoryPipeline")
public class FootballSeasonCategoryPipeline implements Pipeline {
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<FootballSeasonCategory> seasonCategoryList = resultItems.get("seasonCategoryList");
        if(null != seasonCategoryList && seasonCategoryList.size() > 0){
            try {
                for(FootballSeasonCategory seasonCategory : seasonCategoryList){
                    if(StringUtils.isEmpty(seasonCategory.getFootballSeasonCategoryId())){
                        seasonCategory.setFootballSeasonCategoryId(UUIDGeneratorUtils.generateUUID());
                        footballSeasonCategoryService.save(seasonCategory);
                    }else{
                        footballSeasonCategoryService.updateById(seasonCategory);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
