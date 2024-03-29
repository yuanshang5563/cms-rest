package org.ys.core.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.ys.common.http.HttpResult;
import org.ys.common.util.IOUtils;
import org.ys.common.util.PasswordUtils;
import org.ys.common.util.SecurityUtils;
import org.ys.core.controller.vo.LoginBean;
import org.ys.core.model.CoreUser;
import org.ys.core.service.CoreUserService;
import org.ys.security.JwtAuthenticatioToken;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class LoginController {
    @Autowired
    private CoreUserService coreUserService;
    @Autowired
    private Producer producer;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        // 生成文字验证码
        String text = producer.createText();
        // 生成图片验证码
        BufferedImage image = producer.createImage(text);
        // 保存到验证码到 session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录接口
     */
    @PostMapping(value = "/login")
    public HttpResult login(@RequestBody LoginBean loginBean, HttpServletRequest request) throws IOException {
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();
        String captcha = loginBean.getCaptcha();

        // 从session中获取之前保存的验证码跟前台传来的验证码进行匹配
        Object kaptcha = request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if(kaptcha == null){
            return HttpResult.error("验证码已失效");
        }
		if(!captcha.equals(kaptcha)){
			return HttpResult.error("验证码不正确");
		}

        // 用户信息
        CoreUser coreUser = null;
        try {
            coreUser = coreUserService.queryCoreUserByUserName(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 账号不存在、密码错误
        if (coreUser == null) {
            return HttpResult.error("账号不存在");
        }
        if (!PasswordUtils.matches(password, coreUser.getPassword())) {
            return HttpResult.error("密码不正确");
        }

        // 账号锁定
        if (StringUtils.equals(coreUser.getStatus(),"coreUserStatus.1")) {
            return HttpResult.error("账号已被锁定,请联系管理员");
        }

        // 系统登录认证
        JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);
        return HttpResult.ok(token);
    }
}
