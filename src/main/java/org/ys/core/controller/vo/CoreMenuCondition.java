package org.ys.core.controller.vo;

import org.ys.common.vo.BasecCondition;

public class CoreMenuCondition extends BasecCondition {
    private Long coreUserId;
    private String userName;
    private String menuName;

    public Long getCoreUserId() {
        return coreUserId;
    }

    public void setCoreUserId(Long coreUserId) {
        this.coreUserId = coreUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
