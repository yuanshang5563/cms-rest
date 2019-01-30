package org.ys.core.controller.vo;

import org.ys.common.vo.BasecCondition;

public class CoreUserCondition extends BasecCondition {
    private String userName;
    private String realName;
    private String sex;
    private Long coreDeptId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getCoreDeptId() {
        return coreDeptId;
    }

    public void setCoreDeptId(Long coreDeptId) {
        this.coreDeptId = coreDeptId;
    }
}
