package org.ys.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.constant.CoreMenuContant;
import org.ys.common.http.HttpResult;
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
        List<CoreMenu> coreMenus = coreMenuService.listCoreMenusByUserId(coreUserId);
        List<CoreMenu> coreMenuList = new ArrayList<>();
        if(null != coreMenus && coreMenus.size() > 0){
            for (CoreMenu coreMenu:coreMenus){
                if(StringUtils.isNotEmpty(coreMenu.getMenuType())){
                    if(CoreMenuContant.MENU_TYPE_PERMISSION == coreMenu.getMenuType()){
                        continue;
                    }
                    coreMenuList.add(coreMenu);
                }
            }
        }
        return HttpResult.ok(coreMenuList);
    }
}
