package org.ys.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.ys.common.constant.DruidContant;
import org.ys.common.constant.RedisKeyContant;
import org.ys.core.model.CoreParameter;

/**
 * Druid配置类
 * @author Administrator
 *
 */
@Configuration
@SuppressWarnings("rawtypes")
public class DruidConfiguration {
	
	@Autowired
    private RedisTemplate redisTemplate ;

    /**
     * 配置监控服务器
     *
     * @return 返回监控注册的servlet对象
     */
    @SuppressWarnings({"unchecked" })
	@Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 添加IP白名单
        CoreParameter allowParam = (CoreParameter) redisTemplate.opsForValue().get(RedisKeyContant.CORE_PARAMETER+DruidContant.DRUID_CONF_ALLOW+":");
        if(null != allowParam) {
        	servletRegistrationBean.addInitParameter("allow", allowParam.getParamValue());
        }
        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
        CoreParameter denyParam = (CoreParameter) redisTemplate.opsForValue().get(RedisKeyContant.CORE_PARAMETER+DruidContant.DRUID_CONF_DENY+":");
        if(null != denyParam) {
        	servletRegistrationBean.addInitParameter("deny", denyParam.getParamValue());
        }
        // 添加控制台管理用户
        CoreParameter loginUsernameParam = (CoreParameter) redisTemplate.opsForValue().get(RedisKeyContant.CORE_PARAMETER+DruidContant.DRUID_CONF_LOGIN_USER_NAME+":");
        if(null != loginUsernameParam) {
        	 servletRegistrationBean.addInitParameter("loginUsername", loginUsernameParam.getParamValue());
        }
        CoreParameter loginPasswordParam = (CoreParameter) redisTemplate.opsForValue().get(RedisKeyContant.CORE_PARAMETER+DruidContant.DRUID_CONF_LOGIN_PASS+":");
        if(null != loginPasswordParam) {
        	servletRegistrationBean.addInitParameter("loginPassword", loginPasswordParam.getParamValue());
        }
        // 是否能够重置数据
        CoreParameter resetEnableParam = (CoreParameter) redisTemplate.opsForValue().get(RedisKeyContant.CORE_PARAMETER+DruidContant.DRUID_CONF_RESETABLE+":");
        if(null != resetEnableParam) {
        	servletRegistrationBean.addInitParameter("resetEnable", resetEnableParam.getParamValue());
        }
        return servletRegistrationBean;
    }

    /**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     */
    @SuppressWarnings({ "unchecked" })
	@Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        CoreParameter urlPatternParam = (CoreParameter) redisTemplate.opsForValue().get(RedisKeyContant.CORE_PARAMETER+DruidContant.DRUID_CONF_URL_PATTERN+":");
        if(null != urlPatternParam) {
        	filterRegistrationBean.addUrlPatterns(urlPatternParam.getParamValue());
        }
        // 忽略过滤格式
        CoreParameter exclusionsParam = (CoreParameter) redisTemplate.opsForValue().get(RedisKeyContant.CORE_PARAMETER+DruidContant.DRUID_CONF_EXCLUSIONS+":");
        if(null != exclusionsParam) {
        	filterRegistrationBean.addInitParameter("exclusions", exclusionsParam.getParamValue());
        }
        return filterRegistrationBean;
    }
}