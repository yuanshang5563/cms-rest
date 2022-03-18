package org.ys.crawler.pipeLine;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.common.util.UUIDGeneratorUtils;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballTeam;
import org.ys.crawler.service.FootballScoreService;
import org.ys.crawler.service.FootballTeamService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

@Component("footballScoreAndTeamPipeline")
public class FootballScoreAndTeamPipeline implements Pipeline {
    @Autowired
    private FootballScoreService footballScoreService;
    @Autowired
    private FootballTeamService footballTeamService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String,FootballTeam> teamMap = resultItems.get("teamMap");
        List<FootballScore> footballScoreList = resultItems.get("footballScoreList");
        try {
            if(null != teamMap && teamMap.size() > 0){
                for (String key : teamMap.keySet()) {
                    FootballTeam footballTeam = teamMap.get(key);
                    footballTeamService.save(footballTeam);
                }
            }
            if(null != footballScoreList && footballScoreList.size() > 0){
                for(FootballScore footballScore : footballScoreList){
                    if(StringUtils.isEmpty(footballScore.getFootballScoreId())){
                        footballScore.setFootballScoreId(UUIDGeneratorUtils.generateUUID());
                        footballScoreService.save(footballScore);
                    }else{
                        footballScoreService.updateById(footballScore);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
