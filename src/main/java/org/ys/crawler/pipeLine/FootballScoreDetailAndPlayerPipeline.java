package org.ys.crawler.pipeLine;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.crawler.model.FootballPlayer;
import org.ys.crawler.model.FootballScore;
import org.ys.crawler.model.FootballScoreDetail;
import org.ys.crawler.service.FootballPlayerService;
import org.ys.crawler.service.FootballScoreDetailService;
import org.ys.crawler.service.FootballScoreService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component("footballScoreDetailAndPlayerPipeline")
public class FootballScoreDetailAndPlayerPipeline implements Pipeline {
    @Autowired
    private FootballScoreDetailService footballScoreDetailService;
    @Autowired
    private FootballScoreService footballScoreService;
    @Autowired
    private FootballPlayerService footballPlayerService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, FootballPlayer> playerMap = resultItems.get("playerMap");
        Map<String,Set<FootballScoreDetail>> scoreDetailMap = resultItems.get("scoreDetailMap");
        Set<String> delScoreIdsSet = new HashSet<String>();
        try {
            if(null != playerMap && playerMap.size() > 0){
                for (String key : playerMap.keySet()) {
                    FootballPlayer footballPlayer = playerMap.get(key);
                    FootballPlayer existsFootballPlayer = footballPlayerService.queryFootballPlayerByEntityId(key);
                    if(null != existsFootballPlayer){
                        footballPlayerService.updateById(footballPlayer);
                    }else{
                        footballPlayerService.save(footballPlayer);
                    }
                }
            }
            if(null != scoreDetailMap && scoreDetailMap.size() > 0){
                Set<String> keySet = scoreDetailMap.keySet();
                if(null != keySet && keySet.size() > 0){
                    for (String scoreId : keySet) {
                        FootballScore footballScore = footballScoreService.queryFootballScoreById(scoreId);
                        if(null != footballScore){
                            int goalCount = footballScore.getAwayScore() + footballScore.getHomeScore();
                            Set<FootballScoreDetail> scoreDetailSet = scoreDetailMap.get(scoreId);
                            /**
                            List<FootballScoreDetail> existsScoreDetails = footballScoreDetailService.queryFootballScoreDetailsByScoreId(scoreId);
                            if(null != existsScoreDetails && existsScoreDetails.size() > 0){
                                //如果已存在详情数据,数据正确就不再保存了
                                if(existsScoreDetails.size() == goalCount){
                                    continue;
                                }
                            }*/
                            if(null != scoreDetailSet && scoreDetailSet.size() > 0){
                                //先清除先前的数据
                                if(null != footballScore && !delScoreIdsSet.contains(footballScore.getFootballScoreId())){
                                    int result = footballScoreDetailService.delFootballScoreDetailsByScoreId(footballScore.getFootballScoreId());
                                    if(result != 0){
                                        delScoreIdsSet.add(footballScore.getFootballScoreId());
                                    }
                                }
                                for(FootballScoreDetail footballScoreDetail : scoreDetailSet){
                                    footballScoreDetailService.save(footballScoreDetail);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
