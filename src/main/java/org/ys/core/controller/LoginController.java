package org.ys.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.core.controller.vo.LoginBean;
import org.ys.core.model.CoreUser;
import org.ys.core.model.CoreUserExample;
import org.ys.core.service.CoreUserService;

import java.util.List;

@RestController
@RequestMapping("/core/loginController")
public class LoginController {
    @Autowired
    private CoreUserService coreUserService;

    @PostMapping(value = "/login")
    public HttpResult login(@RequestBody LoginBean loginBean){
        try {
            CoreUserExample example = new CoreUserExample();
            example.createCriteria().andUserNameEqualTo(loginBean.getUsername());
            List<CoreUser> coreUserList = coreUserService.queryCoreUsersByExample(example);
            if(null != coreUserList && coreUserList.size() > 0){
                return HttpResult.ok(coreUserList.get(0));
            }
            return HttpResult.ok("登陆成功");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("登陆失败");
        }
    }
}
