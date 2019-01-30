package org.ys.core.controller.vo;

import org.ys.common.vo.BasecCondition;

public class CoreParameterCondition extends BasecCondition {
    private String paramName;
    private String paramType;
    private String paramCode;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }
}
