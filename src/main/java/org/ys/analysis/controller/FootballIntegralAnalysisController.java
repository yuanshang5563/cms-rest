package org.ys.analysis.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.constant.LeiDataCrawlerConstant;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.crawler.controller.BaseCrawlerController;
import org.ys.crawler.controller.vo.FootballIntegralCondition;
import org.ys.crawler.model.*;
import org.ys.crawler.pipeLine.FootballIntegralAndTeamPipeline;
import org.ys.crawler.processor.FootballIntegralPageProcessor;
import org.ys.crawler.service.*;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/analysis/footballIntegralAnalysisController")
@RestController
public class FootballIntegralAnalysisController extends BaseCrawlerController {
    @Autowired
    private FootballIntegralService footballIntegralService;

    @GetMapping("/findIntegralList")
    public HttpResult findIntegralList(@RequestParam String footballSeasonCategoryId){
        if(StringUtils.isEmpty(footballSeasonCategoryId)){
            return HttpResult.error("参数为空!");
        }
        try {
            List<FootballIntegral> footballIntegrals = footballIntegralService.queryFootballIntegralsBySeasonCategoryId(footballSeasonCategoryId.trim());
            footballIntegralService.fillFootballIntegrals(footballIntegrals);
            return HttpResult.ok(footballIntegrals);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
