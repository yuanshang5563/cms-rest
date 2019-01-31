package org.ys.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ys.common.http.HttpResult;
import org.ys.common.page.PageBean;
import org.ys.core.controller.vo.CoreDictCondition;
import org.ys.core.model.CoreDictionaries;
import org.ys.core.model.CoreDictionariesExample;
import org.ys.core.model.CoreDictionariesGroup;
import org.ys.core.service.CoreDictionariesGroupService;
import org.ys.core.service.CoreDictionariesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sys/coreDictionaries")
public class CoreDictionariesController {
    @Autowired
    private CoreDictionariesService coreDictionariesService;
    @Autowired
    private CoreDictionariesGroupService coreDictionariesGroupService;

    @PreAuthorize("hasAuthority('ROLE_CORE_DICT_LIST')")
    @PostMapping("/findPage")
    public HttpResult findPage(@RequestBody CoreDictCondition coreDictCondition){
        if(null == coreDictCondition){
            return HttpResult.error("查询参数为空");
        }
        PageBean<CoreDictionaries> pageBean = null;
        try {
            String dictCode = coreDictCondition.getDictCode();
            String dictValue = coreDictCondition.getDictValue();
            Long coreDictGroupId = coreDictCondition.getCoreDictGroupId();
            CoreDictionariesExample example = new CoreDictionariesExample();
            CoreDictionariesExample.Criteria criteria = example.createCriteria();
            if(StringUtils.isNotEmpty(dictCode)){
                criteria.andDictCodeEqualTo(dictCode.trim());
            }
            if(StringUtils.isNotEmpty(dictValue)){
                criteria.andDictValueLike("%"+dictValue+"%");
            }
            if(null != coreDictGroupId){
                Set<CoreDictionariesGroup> coreDictionariesGroups = coreDictionariesGroupService.queryAllSubCoreDictionariesGroupsByDictGroupId(coreDictGroupId);
                if(null != coreDictionariesGroups && coreDictionariesGroups.size() > 0){
                    List<Long> groupIds = new ArrayList<>();
                    for (CoreDictionariesGroup coreDictionariesGroup : coreDictionariesGroups) {
                        groupIds.add(coreDictionariesGroup.getCoreDictGroupId());
                    }
                    criteria.andCoreDictGroupIdIn(groupIds);
                }
            }
            pageBean = coreDictionariesService.pageCoreDictionariesByExample(example, coreDictCondition.getPageNum(), coreDictCondition.getPageSize());
            if(null == pageBean) {
                pageBean = new PageBean<>(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
        return HttpResult.ok(pageBean);
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_DICT_ADD_EDIT')")
    @PostMapping("/saveOrEdit")
    public HttpResult saveOrEdit(@RequestBody CoreDictionaries coreDictionaries){
        if(null == coreDictionaries || StringUtils.isEmpty(coreDictionaries.getDictCode())){
            return HttpResult.error("参数为空");
        }
        try {
            CoreDictionariesExample example = new CoreDictionariesExample();
            example.createCriteria().andDictCodeEqualTo(coreDictionaries.getDictCode().trim());
            List<CoreDictionaries> coreDictionariess = coreDictionariesService.queryCoreDictionariesByExample(example);
            if(null != coreDictionariess && coreDictionariess.size() > 0){
                if(!(coreDictionariess.size() == 1 && coreDictionaries.getCoreDictId() != null && coreDictionaries.getCoreDictId() != 0l)){
                    return HttpResult.error("参数已经存在！");
                }
            }
            if(null == coreDictionaries || coreDictionaries.getCoreDictId() == 0l){
                coreDictionariesService.save(coreDictionaries);
            }else {
                coreDictionariesService.updateById(coreDictionaries);
            }
            return HttpResult.ok("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_DICT_DEL')")
    @DeleteMapping("/delete")
    public HttpResult delete(@RequestParam Long coreDictId){
        if(null == coreDictId || coreDictId == 0){
            return HttpResult.error("参数为为空！");
        }
        try {
            coreDictionariesService.delCoreDictionariesById(coreDictId);
            return HttpResult.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CORE_DICT_EDIT_VIEW')")
    @GetMapping("/find")
    public HttpResult find(@RequestParam Long coreDictId){
        if(null == coreDictId || coreDictId == 0){
            return HttpResult.ok();
        }
        try {
            CoreDictionaries coreDictionaries = coreDictionariesService.queryCoreDictionariesById(coreDictId);
            if(null != coreDictionaries && null != coreDictionaries.getCoreDictGroupId()){
                CoreDictionariesGroup group = coreDictionariesGroupService.queryCoreDictionariesGroupById(coreDictionaries.getCoreDictGroupId());
                if(null != group){
                    coreDictionaries.setDictGroupName(group.getDictGroupName());
                }
            }
            return HttpResult.ok(coreDictionaries);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("程序出现异常");
        }
    }
}
