package org.ys.core.controller.vo;

import org.ys.common.vo.BasecCondition;

public class CoreDictCondition extends BasecCondition {
    private String dictCode;
    private String dictValue;
    private Long coreDictGroupId;

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public Long getCoreDictGroupId() {
        return coreDictGroupId;
    }

    public void setCoreDictGroupId(Long coreDictGroupId) {
        this.coreDictGroupId = coreDictGroupId;
    }
}
