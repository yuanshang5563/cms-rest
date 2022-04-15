package org.ys.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.http.HttpResult;
import org.ys.common.vo.CascaderTreeItem;
import org.ys.crawler.service.FootballLeagueMatchService;
import org.ys.crawler.service.FootballSeasonCategoryService;
import org.ys.crawler.service.FootballSeasonService;

import java.util.List;

@RequestMapping("/common/footballCommonController")
@RestController
public class FootballCommonController {
    @Autowired
    private FootballLeagueMatchService footballLeagueMatchService;
    @Autowired
    private FootballSeasonService footballSeasonService;
    @Autowired
    private FootballSeasonCategoryService footballSeasonCategoryService;

    /**
     * 获取联赛的级联数据
     * @return
     */
    @GetMapping("/findLeagueMatchCascaderItem")
    public HttpResult findLeagueMatchCascaderItem(){
        try {
            List<CascaderTreeItem> cascaderItemList = footballLeagueMatchService.findLeagueMatchCascaderItem();
            return HttpResult.ok(cascaderItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    /**
     * 根据联赛id获取赛季的级联数据
     * @return
     */
    @GetMapping("/findSeasonCascaderItemByLeagueMatchId")
    public HttpResult findSeasonCascaderItemByLeagueMatchId(@RequestParam String footballLeagueMatchId){
        try {
            List<CascaderTreeItem> cascaderItemList = footballSeasonService.findSeasonCascaderItemByLeagueMatchId(footballLeagueMatchId);
            return HttpResult.ok(cascaderItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    /**
     * 根据赛季id获取赛季类别的级联数据
     * @return
     */
    @GetMapping("/findSeasonCascaderItemBySeasonId")
    public HttpResult findSeasonCascaderItemBySeasonId(@RequestParam String footballSeasonId){
        try {
            List<CascaderTreeItem> cascaderItemList = footballSeasonCategoryService.findSeasonCascaderItemBySeasonId(footballSeasonId);
            return HttpResult.ok(cascaderItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
