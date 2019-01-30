package org.ys.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.core.controller.vo.CoreRoleCondition;
import org.ys.core.model.CoreMenu;
import org.ys.core.model.CoreRole;
import org.ys.core.model.CoreRoleExample;
import org.ys.core.model.CoreRoleMenu;
import org.ys.core.service.CoreMenuService;
import org.ys.core.service.CoreRoleMenuService;
import org.ys.core.service.CoreRoleService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sys/coreRole")
public class CoreRoleController {
    @Autowired
    private CoreRoleService coreRoleService;

    @Autowired
    private CoreRoleMenuService coreRoleMenuService;

    @Autowired
    private CoreMenuService coreMenuService;

    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody CoreRoleCondition coreRoleCondition){
        if(null == coreRoleCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<CoreRole> pageBean = null;
        try {
            String roleName = coreRoleCondition.getRoleName();
            String role = coreRoleCondition.getRole();
            CoreRoleExample example = new CoreRoleExample();
            CoreRoleExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(roleName)){
                criteria.andRoleNameLike(roleName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(role)){
                criteria.andRoleLike(role.trim()+"%");
            }
            pageBean = coreRoleService.pageCoreRolesByExample(example, coreRoleCondition.getPageNum(), coreRoleCondition.getPageSize());
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
    public HttpResult saveOrEdit(@RequestBody CoreRole coreRole){
        if(null == coreRole || StringUtils.isEmpty(coreRole.getRole())){
            return HttpResult.error("参数为空");
        }
        try {
            CoreRoleExample example = new CoreRoleExample();
            example.createCriteria().andRoleEqualTo(coreRole.getRole().trim());
            List<CoreRole> coreRoles = coreRoleService.queryCoreRolesByExample(example);
            if(null != coreRoles && coreRoles.size() > 0){
                if(!(coreRoles.size() == 1 && coreRole.getCoreRoleId() != null && coreRole.getCoreRoleId() != 0l)){
                    return HttpResult.error("角色已经存在！");
                }
            }
            if(null == coreRole.getCoreRoleId() || coreRole.getCoreRoleId() == 0l){
                coreRole.setCreatedTime(new Date());
                coreRole.setModifiedTime(new Date());
                coreRoleService.save(coreRole);
            }else {
                coreRole.setModifiedTime(new Date());
                coreRoleService.updateById(coreRole);
            }
            return HttpResult.ok("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @PostMapping("/saveRoleMenus")
    public HttpResult saveRoleMenus(@RequestBody List<CoreRoleMenu> coreRoleMenuList){
        if(null == coreRoleMenuList || coreRoleMenuList.isEmpty()){
            return HttpResult.error("参数为空");
        }
        try {
            coreRoleMenuService.saveRoleMenus(coreRoleMenuList);
            return HttpResult.ok("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @DeleteMapping("/delete")
    public HttpResult delete(@RequestParam Long coreRoleId){
        if(null == coreRoleId || coreRoleId == 0){
            return HttpResult.error("参数为为空！");
        }
        try {
            coreRoleService.delCoreRoleById(coreRoleId);
            return HttpResult.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @GetMapping("/find")
    public HttpResult find(@RequestParam Long coreRoleId){
        if(null == coreRoleId || coreRoleId == 0){
            return HttpResult.ok();
        }
        try {
            CoreRole coreRole = coreRoleService.queryCoreRoleById(coreRoleId);
            return HttpResult.ok(coreRole);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @GetMapping("/findAll")
    public HttpResult findAll(){
        try {
            CoreRoleExample example = new CoreRoleExample();
            List<CoreRole> coreRoles = coreRoleService.queryCoreRolesByExample(example);
            return HttpResult.ok(coreRoles);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @GetMapping("/findRoleMenus")
    public HttpResult findRoleMenus(@RequestParam Long coreRoleId){
        if(null == coreRoleId || coreRoleId == 0){
            return HttpResult.ok();
        }
        try {
            List<CoreMenu> coreMenus = coreMenuService.listCoreMenusByRoleId(coreRoleId);
            return HttpResult.ok(coreMenus);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
