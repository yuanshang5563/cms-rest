package org.ys.core.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.core.controller.vo.CoreDeptCondition;
import org.ys.core.model.CoreDept;
import org.ys.core.service.CoreDeptService;

@Api("机构控制器")
@RequestMapping("/sys/coreDept")
@RestController
public class CoreDeptController {
    @Autowired
    private CoreDeptService coreDeptService;

    @ApiOperation(value="查找机构树", notes="根据相关条件查找机构树")
    @ApiImplicitParam(name = "coreDeptCondition", value = "机构查询实体", required = true, dataType = "CoreDeptCondition")
    @PostMapping(value="/findTree")
    public HttpResult findTree(@RequestBody CoreDeptCondition coreDeptCondition) {
        return HttpResult.ok(coreDeptService.findTree(coreDeptCondition));
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_DEPT_EDIT_VIEW')")
    @GetMapping(value="/find")
    public HttpResult find(@RequestParam Long coreDeptId) {
        if(null == coreDeptId || coreDeptId == 0l){
            return HttpResult.error("参数为空");
        }
        try {
            CoreDept coreDept = coreDeptService.queryCoreDeptById(coreDeptId);
            if(null != coreDept && coreDept.getParentCoreDeptId() != null){
                CoreDept parentCoreDept = coreDeptService.queryCoreDeptById(coreDept.getParentCoreDeptId());
                if(null != parentCoreDept){
                    coreDept.setParentDeptName(parentCoreDept.getDeptName());
                }
            }
            return HttpResult.ok(coreDept);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_DEPT_ADD_EDIT')")
    @PostMapping("/saveOrEdit")
    public HttpResult saveOrEdit(@RequestBody CoreDept coreDept){
        if(null == coreDept){
            return HttpResult.error("参数为空");
        }
        try {
            if(null == coreDept.getCoreDeptId() || coreDept.getCoreDeptId() == 0l){
                coreDeptService.save(coreDept);
            }else {
                coreDeptService.updateById(coreDept);
            }
            return HttpResult.ok("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_DEPT_DEL')")
    @DeleteMapping("/delete")
    public HttpResult delete(@RequestParam Long coreDeptId){
        if(null == coreDeptId || coreDeptId == 0){
            return HttpResult.error("参数为为空！");
        }
        try {
            coreDeptService.delCoreDeptById(coreDeptId);
            return HttpResult.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
