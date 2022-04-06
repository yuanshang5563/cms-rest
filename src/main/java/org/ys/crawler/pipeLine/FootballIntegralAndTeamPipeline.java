package org.ys.crawler.pipeLine;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.util.UUIDGeneratorUtils;
import org.ys.crawler.model.FootballIntegral;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballTeam;
import org.ys.crawler.service.FootballIntegralService;
import org.ys.crawler.service.FootballScoreService;
import org.ys.crawler.service.FootballTeamService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

@Component("footballIntegralPipeline")
public class FootballIntegralAndTeamPipeline implements Pipeline {
    @Autowired
    private FootballIntegralService footballIntegralService;
    @Autowired
    private FootballTeamService footballTeamService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<FootballIntegral> footballIntegralList = resultItems.get("footballIntegralList");
        List<FootballTeam> footballTeamsList = resultItems.get("footballTeamsList");
        try {
            if(null != footballTeamsList && footballTeamsList.size() > 0){
                for(FootballTeam footballTeam : footballTeamsList){
                    FootballTeam team = footballTeamService.queryFootballTeamByEntityId(footballTeam.getEntityId());
                    if(null == team){
                        footballTeamService.save(footballTeam);
                    }else{
                        footballTeamService.updateById(footballTeam);
                    }
                }
            }

            if(null != footballIntegralList && footballIntegralList.size() > 0){
                for(FootballIntegral footballIntegral : footballIntegralList){
                    if(StringUtils.isEmpty(footballIntegral.getFootballIntegralId())){
                        footballIntegral.setFootballIntegralId(UUIDGeneratorUtils.generateUUID());
                        footballIntegralService.save(footballIntegral);
                    }else{
                        footballIntegralService.updateById(footballIntegral);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
