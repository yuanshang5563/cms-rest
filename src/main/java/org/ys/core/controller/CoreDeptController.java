package org.ys.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.core.controller.vo.CoreDeptCondition;
import org.ys.core.model.CoreDept;
import org.ys.core.model.CoreDeptExample;
import org.ys.core.service.CoreDeptService;

import java.util.ArrayList;

@RequestMapping("/sys/coreDept")
@RestController
public class CoreDeptController {
    @Autowired
    private CoreDeptService coreDeptService;

    @GetMapping(value="/findTree")
    public HttpResult findTree(@RequestParam(required = false) String deptName) {
        return HttpResult.ok(coreDeptService.findTree(deptName));
    }

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
