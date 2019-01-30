package org.ys.core.controller.vo;

import org.ys.common.vo.BasecCondition;

public class CoreRoleCondition extends BasecCondition {
    private String roleName;
    private String role;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
