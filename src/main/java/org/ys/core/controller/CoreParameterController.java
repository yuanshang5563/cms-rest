package org.ys.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.core.controller.vo.CoreParameterCondition;
import org.ys.core.model.CoreParameter;
import org.ys.core.model.CoreParameterExample;
import org.ys.core.service.CoreParameterService;
import org.ys.core.service.RedisService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sys/coreParameter")
public class CoreParameterController {
    @Autowired
    private CoreParameterService coreParameterService;
    @Autowired
    private RedisService redisService;

    @PreAuthorize("hasAuthority('ROLE_CORE_PARAM_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody CoreParameterCondition coreParameterCondition){
        if(null == coreParameterCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<CoreParameter> pageBean = null;
        try {
            String paramName = coreParameterCondition.getParamName();
            String paramType = coreParameterCondition.getParamType();
            String paramCode = coreParameterCondition.getParamCode();
            CoreParameterExample example = new CoreParameterExample();
            CoreParameterExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(paramName)){
                criteria.andParamNameLike("%"+paramName.trim()+"%");
            }
            if(StringUtils.isNotEmpty(paramType)){
                criteria.andParamTypeEqualTo(paramType.trim());
            }
            if(StringUtils.isNotEmpty(paramCode)){
                criteria.andParamCodeEqualTo(paramCode.trim());
            }
            pageBean = coreParameterService.pageCoreParametersByExample(example, coreParameterCondition.getPageNum(), coreParameterCondition.getPageSize());
            if(null == pageBean) {
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_PARAM_ADD_EDIT')")
    @PostMapping("/saveOrEdit")
    public HttpResult saveOrEdit(@RequestBody CoreParameter coreParameter){
        if(null == coreParameter || StringUtils.isEmpty(coreParameter.getParamCode())){
            return HttpResult.error("参数为空");
        }
        try {
            CoreParameterExample example = new CoreParameterExample();
            example.createCriteria().andParamCodeEqualTo(coreParameter.getParamCode().trim());
            List<CoreParameter> coreParameters = coreParameterService.queryCoreParametersByExample(example);
            if(null != coreParameters && coreParameters.size() > 0){
                if(!(coreParameters.size() == 1 && coreParameter.getCoreParamId() != null && coreParameter.getCoreParamId() != 0l)){
                    return HttpResult.error("参数已经存在！");
                }
            }
            if(null == coreParameter || coreParameter.getCoreParamId() == 0l){
                coreParameter.setCreatedTime(new Date());
                coreParameter.setModifiedTime(new Date());
                coreParameterService.save(coreParameter);
            }else {
                coreParameter.setModifiedTime(new Date());
                coreParameterService.updateById(coreParameter);
            }
            redisService.refreshCoreParameter(coreParameter);
            return HttpResult.ok("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_PARAM_DEL')")
    @DeleteMapping("/delete")
    public HttpResult delete(@RequestParam Long coreParamId){
        if(null == coreParamId || coreParamId == 0){
            return HttpResult.error("参数为为空！");
        }
        try {
            coreParameterService.delCoreParameterById(coreParamId);
            CoreParameter parameter = coreParameterService.queryCoreParameterById(coreParamId);
            redisService.refreshCoreParameter(parameter);
            return HttpResult.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_PARAM_EDIT_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam Long coreParamId){
        if(null == coreParamId || coreParamId == 0){
            return HttpResult.ok();
        }
        try {
            CoreParameter coreParameter = coreParameterService.queryCoreParameterById(coreParamId);
            return HttpResult.ok(coreParameter);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
