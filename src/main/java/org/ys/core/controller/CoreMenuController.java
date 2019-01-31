package org.ys.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.ys.common.http.HttpResult;
import org.ys.core.controller.vo.CoreMenuCondition;
import org.ys.core.model.*;
import org.ys.core.service.CoreMenuService;
import org.ys.core.service.CoreRoleMenuService;
import org.ys.core.service.CoreRoleService;
import org.ys.core.service.CoreUserService;

import java.util.*;

@RestController
@RequestMapping("/sys/coreMenu")
public class CoreMenuController {
    @Autowired
    private CoreMenuService coreMenuService;
    @Autowired
    private CoreUserService coreUserService;
    @Autowired
    private CoreRoleService coreRoleService;
    @Autowired
    private CoreRoleMenuService coreRoleMenuService;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @PostMapping(value="/findNavTree")
    public HttpResult findNavTree(@RequestBody CoreMenuCondition coreMenuCondition) {
        String userName = coreMenuCondition.getUserName();
        Long coreUserId = null;
        CoreUser coreUser = null;
        try {
            coreUser = coreUserService.queryCoreUserByUserName(userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null != coreUser){
            coreUserId = coreUser.getCoreUserId();
        }
        if(null == coreUserId || coreUserId == 0){
            return HttpResult.error("用户id为空！");
        }
        coreMenuCondition.setCoreUserId(coreUserId);
        coreMenuCondition.setMenuName(null);
        List<CoreMenu> coreMenus = coreMenuService.findTree( coreMenuCondition,"1");
        return HttpResult.ok(coreMenus);
    }

    @PostMapping(value="/findCoreMenuTree")
    public HttpResult findCoreMenuTree(@RequestBody CoreMenuCondition coreMenuCondition) {
        coreMenuCondition.setCoreUserId(null);
        List<CoreMenu> coreMenus = coreMenuService.findTree(coreMenuCondition,"0");
        return HttpResult.ok(coreMenus);
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_MENU_ADD_EDIT')")
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

    @PreAuthorize("hasAuthority('ROLE_CORE_MENU_DEL')")
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

    @PreAuthorize("hasAuthority('ROLE_CORE_MENU_EDIT_VIEW')")
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
