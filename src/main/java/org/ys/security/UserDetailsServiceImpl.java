package org.ys.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ys.common.constant.CoreMenuContant;
import org.ys.core.model.CoreMenu;
import org.ys.core.model.CoreUser;
import org.ys.core.service.CoreMenuService;
import org.ys.core.service.CoreUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户登录认证信息查询
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CoreUserService coreUserService;

    @Autowired
    private CoreMenuService coreMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CoreUser coreUser = null;
        try {
            coreUser = coreUserService.queryCoreUserByUserName(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (coreUser == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<CoreMenu> coreMenus = coreMenuService.listCoreMenusByUserId(coreUser.getCoreUserId());
        if(null != coreMenus && coreMenus.size() > 0){
            for (CoreMenu coreMenu : coreMenus) {
                if(StringUtils.equals(CoreMenuContant.MENU_TYPE_PERMISSION,coreMenu.getMenuType())){
                    grantedAuthorities.add(new GrantedAuthorityImpl(coreMenu.getPermission()));
                }
            }
        }
        return new JwtUserDetails(coreUser.getUserName(), coreUser.getPassword(), grantedAuthorities);
    }
}