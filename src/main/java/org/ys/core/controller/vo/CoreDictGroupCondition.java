package org.ys.core.controller.vo;

import org.ys.common.vo.BasecCondition;

public class CoreDictGroupCondition extends BasecCondition {
    private String dictGroupName;
    private String dictGroupCode;

    public String getDictGroupName() {
        return dictGroupName;
    }

    public void setDictGroupName(String dictGroupName) {
        this.dictGroupName = dictGroupName;
    }

    public String getDictGroupCode() {
        return dictGroupCode;
    }

    public void setDictGroupCode(String dictGroupCode) {
        this.dictGroupCode = dictGroupCode;
    }
}
