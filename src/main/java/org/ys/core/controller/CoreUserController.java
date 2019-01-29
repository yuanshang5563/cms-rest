package org.ys.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.core.controller.vo.CoreUserCondition;
import org.ys.core.model.CoreDept;
import org.ys.core.model.CoreUser;
import org.ys.core.model.CoreUserExample;
import org.ys.core.model.CoreUserRole;
import org.ys.core.service.CoreUserRoleService;
import org.ys.core.service.CoreUserService;
import org.ys.core.service.CoreDeptService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sys/coreUser")
public class CoreUserController {
    @Autowired
    private CoreUserService coreUserService;
    @Autowired
    private CoreDeptService coreDeptService;
    @Autowired
    private CoreUserRoleService coreUserRoleService;

    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody CoreUserCondition coreUserCondition){
        if(null == coreUserCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<CoreUser> pageBean = null;
        try {
            String userName = coreUserCondition.getUserName();
            String realName = coreUserCondition.getRealName();
            String sex = coreUserCondition.getSex();
            CoreUserExample example = new CoreUserExample();
            CoreUserExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(userName)){
                criteria.andUserNameLike("%"+userName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(realName)){
                criteria.andRealNameLike("%"+realName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(sex)){
                criteria.andSexEqualTo(sex.trim());
            }
            pageBean = coreUserService.pageCoreUsersByExample(example, coreUserCondition.getPageNum(), coreUserCondition.getPageSize());
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
    public HttpResult saveOrEdit(@RequestBody CoreUser coreUser){
        if(null == coreUser){
            return HttpResult.error("参数为空");
        }
        try {
            CoreUserExample example = new CoreUserExample();
            example.createCriteria().andUserNameEqualTo(coreUser.getUserName());
            List<CoreUser> coreUsers = coreUserService.queryCoreUsersByExample(example);
            if(null != coreUsers && coreUsers.size() > 0){
                if(!(coreUsers.size() == 1 && coreUser.getCoreUserId() != null && coreUser.getCoreUserId() != 0l)){
                    return HttpResult.error("角色已经存在！");
                }
            }
            coreUserService.updateCoreUserAndRoles(coreUser);
            return HttpResult.ok("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @DeleteMapping("/delete")
    public HttpResult delete(@RequestParam Long coreUserId){
        if(null == coreUserId || coreUserId == 0){
            return HttpResult.error("参数为为空！");
        }
        try {
            coreUserService.delCoreUserById(coreUserId);
            return HttpResult.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @GetMapping("/find")
    public HttpResult find(@RequestParam Long coreUserId){
        if(null == coreUserId || coreUserId == 0){
            return HttpResult.ok();
        }
        try {
            CoreUser coreUser = coreUserService.queryCoreUserById(coreUserId);
            if(null != coreUser){
                List<CoreUserRole> coreUserRoles = coreUserRoleService.findCoreUserRoleByUserId(coreUserId);
                if(null != coreUserRoles && coreUserRoles.size() > 0){
                    coreUser.setUserRoles(coreUserRoles);
                }
                CoreDept coreDept = coreDeptService.queryCoreDeptById(coreUser.getCoreDeptId());
                if(null != coreDept){
                    coreUser.setDeptName(coreDept.getDeptName());
                }
            }
            return HttpResult.ok(coreUser);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
