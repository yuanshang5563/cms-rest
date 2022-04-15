package org.ys.analysis.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.http.HttpResult;
import org.ys.crawler.model.FootballSeasonCategory;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballSeasonCategoryService;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/analysis/footballSeasonCategoryAnalysisController")
@RestController
public class FootballSeasonCategoryAnalysisController {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;

    @GetMapping("/findSeasonCategoryList")
    public HttpResult findSeasonCategoryList(@RequestParam String footballSeasonId){
        if(StringUtils.isEmpty(footballSeasonId)){
            return HttpResult.error("查询参数为空");
        }
        List<FootballSeasonCategory> footballSeasonCategories =  null;
        try {
            footballSeasonCategories = footballSeasonCategoryService.queryFootballSeasonCategoryBySeasonId(footballSeasonId);
            if(null == footballSeasonCategories){
                footballSeasonCategories = new ArrayList<FootballSeasonCategory>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(footballSeasonCategories);
    }
}
