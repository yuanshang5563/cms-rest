package org.ys.crawler.pipeLine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.crawler.model.FootballLeagueMatch;
import org.ys.crawler.service.FootballLeagueMatchService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component("footballLeagueMatchPipeline")
public class FootballLeagueMatchPipeline implements Pipeline {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<FootballLeagueMatch> matches = resultItems.get("matches");
        if(null != matches && matches.size() > 0){
            try {
                for(FootballLeagueMatch footballLeagueMatch : matches){
                    footballLeagueMatchService.save(footballLeagueMatch);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
