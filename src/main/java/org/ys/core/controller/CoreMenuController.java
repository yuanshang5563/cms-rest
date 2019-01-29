package org.ys.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.core.model.CoreMenu;
import org.ys.core.model.CoreMenuExample;
import org.ys.core.service.CoreMenuService;

import java.util.List;

@RestController
@RequestMapping("/sys/coreMenu")
public class CoreMenuController {
    @Autowired
    private CoreMenuService coreMenuService;

    @GetMapping(value="/findNavTree")
    public HttpResult findNavTree(@RequestParam Long coreUserId) {
        if(null == coreUserId || coreUserId == 0){
            return HttpResult.error("用户id为空！");
        }
        List<CoreMenu> coreMenus = coreMenuService.findTree(11l,"1",null);
        return HttpResult.ok(coreMenus);
    }

    @GetMapping(value="/findCoreMenuTree")
    public HttpResult findCoreMenuTree(@RequestParam(required = false) String menuName) {
        List<CoreMenu> coreMenus = coreMenuService.findTree(null,"0",menuName);
        return HttpResult.ok(coreMenus);
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

    @DeleteMapping("/delete")
    public HttpResult delete(@RequestParam Long coreMenuId){
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

    @GetMapping(value="/find")
    public HttpResult find(@RequestParam Long coreMenuId) {
        if(null == coreMenuId || coreMenuId == 0){
            return HttpResult.ok();
        }
        try {
            CoreMenuExample example = new CoreMenuExample();
            example.createCriteria().andCoreMenuIdEqualTo(coreMenuId);
            CoreMenu coreMenu = coreMenuService.queryCoreMenuById(coreMenuId);
            if(null != coreMenu && null != coreMenu.getParentCoreMenuId() && coreMenu.getParentCoreMenuId() != 0l){
                CoreMenu parentCoreMenu = coreMenuService.queryCoreMenuById(coreMenu.getParentCoreMenuId());
                if(null != parentCoreMenu){
                    coreMenu.setParentMenuName(parentCoreMenu.getMenuName());
                }
            }
            return HttpResult.ok(coreMenu);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
