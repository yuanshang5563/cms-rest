package org.ys.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.core.controller.vo.CoreDictGroupCondition;
import org.ys.core.model.CoreDictionariesGroup;
import org.ys.core.model.CoreDictionariesGroupExample;
import org.ys.core.service.CoreDictionariesGroupService;

import java.util.List;

@RestController
@RequestMapping("/sys/CoreDictionariesGroup")
public class CoreDictionariesGroupController {
    @Autowired
    private CoreDictionariesGroupService coreDictionariesGroupService;

    @PostMapping(value="/findTree")
    public HttpResult findTree(@RequestBody CoreDictGroupCondition coreDictGroupCondition) {
        return HttpResult.ok(coreDictionariesGroupService.findTree(coreDictGroupCondition));
    }

    @GetMapping(value="/find")
    public HttpResult find(@RequestParam Long coreDictGroupId) {
        if(null == coreDictGroupId || coreDictGroupId == 0l){
            return HttpResult.ok("参数为空");
        }
        try {
            CoreDictionariesGroup coreDictionariesGroup = coreDictionariesGroupService.queryCoreDictionariesGroupById(coreDictGroupId);
            if(null != coreDictionariesGroup && coreDictionariesGroup.getParentCoreDictGroupId() != null){
                CoreDictionariesGroup parentCoreDictionariesGroup = coreDictionariesGroupService.queryCoreDictionariesGroupById(coreDictionariesGroup.getParentCoreDictGroupId());
                if(null != parentCoreDictionariesGroup){
                    coreDictionariesGroup.setParentDictGroupNameName(parentCoreDictionariesGroup.getDictGroupName());
                }
            }
            return HttpResult.ok(coreDictionariesGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @PostMapping("/saveOrEdit")
    public HttpResult saveOrEdit(@RequestBody CoreDictionariesGroup coreDictionariesGroup){
        if(null == coreDictionariesGroup){
            return HttpResult.error("参数为空");
        }
        try {
            CoreDictionariesGroupExample example = new CoreDictionariesGroupExample();
            example.createCriteria().andDictGroupCodeEqualTo(coreDictionariesGroup.getDictGroupCode());
            List<CoreDictionariesGroup> coreDictionariesGroups = coreDictionariesGroupService.queryCoreDictionariesGroupsByExample(example);
            if(null != coreDictionariesGroups && coreDictionariesGroups.size() > 0){
                if(!(coreDictionariesGroups.size() == 1 && coreDictionariesGroup.getCoreDictGroupId() != null && coreDictionariesGroup.getCoreDictGroupId() != 0l)){
                    return HttpResult.error("字典组已经存在！");
                }
            }
            if(null == coreDictionariesGroup.getCoreDictGroupId() || coreDictionariesGroup.getCoreDictGroupId() == 0l){
                coreDictionariesGroupService.save(coreDictionariesGroup);
            }else {
                coreDictionariesGroupService.updateById(coreDictionariesGroup);
            }
            return HttpResult.ok("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @DeleteMapping("/delete")
    public HttpResult delete(@RequestParam Long coreDictGroupId){
        if(null == coreDictGroupId || coreDictGroupId == 0){
            return HttpResult.error("参数为为空！");
        }
        try {
            coreDictionariesGroupService.delCoreDictionariesGroupById(coreDictGroupId);
            return HttpResult.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
