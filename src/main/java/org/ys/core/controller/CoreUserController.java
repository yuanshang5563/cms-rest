package org.ys.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.core.controller.vo.CoreUserCondition;
import org.ys.core.model.CoreUser;
import org.ys.core.model.CoreUserExample;
import org.ys.core.service.CoreUserService;

import java.util.ArrayList;

@RestController
@RequestMapping("/core/coreUsers")
public class CoreUserController {
    @Autowired
    private CoreUserService coreUserService;

    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody CoreUserCondition coreUserCondition){
        if(null == coreUserCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<CoreUser> pageBean = null;
        try {
            String userName = coreUserCondition.getUserName();
            CoreUserExample example = new CoreUserExample();
            CoreUserExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(userName)){
                criteria.andUserNameLike(userName.trim()+"%");
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
            if(null == coreUser.getCoreUserId() || coreUser.getCoreUserId() == 0l){
                coreUserService.save(coreUser);
            }else {
                coreUserService.updateById(coreUser);
            }
            return HttpResult.ok("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @DeleteMapping("/delete/{coreUserId}")
    public HttpResult delete(@PathVariable Long coreUserId){
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
}
