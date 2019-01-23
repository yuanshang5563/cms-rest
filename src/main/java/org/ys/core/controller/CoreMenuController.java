package org.ys.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.common.constant.CoreMenuContant;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.core.controller.vo.CoreMenuCondition;
import org.ys.core.model.CoreMenu;
import org.ys.core.model.CoreMenuExample;
import org.ys.core.service.CoreMenuService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/core/coreMenus")
public class CoreMenuController {
    @Autowired
    private CoreMenuService coreMenuService;

    @GetMapping(value="/findNavTree")
    public HttpResult findNavTree(@RequestParam Long coreUserId) {
        if(null == coreUserId || coreUserId == 0){
            return HttpResult.error("用户id为空！");
        }
        List<CoreMenu> coreMenus = coreMenuService.findTree(coreUserId,"1");
        return HttpResult.ok(coreMenus);
    }

    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody CoreMenuCondition coreMenuCondition){
        if(null == coreMenuCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<CoreMenu> pageBean = null;
        try {
            String menuName = coreMenuCondition.getMenuName();
            CoreMenuExample example = new CoreMenuExample();
            CoreMenuExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(menuName)){
                criteria.andMenuNameLike(menuName.trim()+"%");
            }
            pageBean = coreMenuService.pageCoreMenusByExample(example, coreMenuCondition.getPageNum(), coreMenuCondition.getPageSize());
            if(null == pageBean) {
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PostMapping("/saveOrEdit")
    public HttpResult saveOrEdit(@RequestBody CoreMenu coreMenu){
        if(null == coreMenu){
            return HttpResult.error("参数为空");
        }
        try {
            if(null == coreMenu.getCoreMenuId() || coreMenu.getCoreMenuId() == 0l){
                coreMenuService.save(coreMenu);
            }else {
                coreMenuService.updateById(coreMenu);
            }
            return HttpResult.ok("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @DeleteMapping("/delete/{coreMenuId}")
    public HttpResult delete(@PathVariable Long coreMenuId){
        if(null == coreMenuId || coreMenuId == 0){
            return HttpResult.error("参数为为空！");
        }
        try {
            coreMenuService.delCoreMenuById(coreMenuId);
            return HttpResult.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
