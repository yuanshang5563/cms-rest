package org.ys.core.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.ys.common.constant.CoreMenuContant;
import org.ys.common.constant.RedisKeyContant;
import org.ys.core.model.*;
import org.ys.core.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("redisService")
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CoreMenuService coreMenuService;
    @Autowired
    private CoreParameterService coreParameterService;
    @Autowired
    private CoreDictionariesGroupService coreDictionariesGroupService;
    @Autowired
    private CoreDictionariesService coreDictionariesService;

    @Override
    public void initSystemCach() throws Exception{
        CoreMenuExample example = new CoreMenuExample();
        List<CoreMenu> allMenuList = coreMenuService.queryCoreMenusByExample(example);
        redisTemplate.opsForList().leftPush(RedisKeyContant.CORE_MENU_ALL_MENUS, allMenuList);
        example.clear();

        example.createCriteria().andMenuTypeEqualTo(CoreMenuContant.MENU_TYPE_PERMISSION);
        List<CoreMenu> allPermissionList = coreMenuService.queryCoreMenusByExample(example);
        redisTemplate.opsForList().leftPush(RedisKeyContant.CORE_MENU_ALL_PERMISSION, allPermissionList);
        CoreParameterExample paramExample = new CoreParameterExample();
        List<CoreParameter> paramList = coreParameterService.queryCoreParametersByExample(paramExample);
        if(null != paramList && paramList.size() > 0) {
            for (CoreParameter coreParameter : paramList) {
                if(StringUtils.isNotEmpty(coreParameter.getParamCode())) {
                    redisTemplate.opsForValue().set(RedisKeyContant.CORE_PARAMETER+coreParameter.getParamCode()+":", coreParameter);
                }
            }
        }
        CoreDictionariesGroupExample groupExample = new CoreDictionariesGroupExample();
        List<CoreDictionariesGroup> coreDictionariesGroups = coreDictionariesGroupService.queryCoreDictionariesGroupsByExample(groupExample);
        if (null != coreDictionariesGroups && coreDictionariesGroups.size() > 0){
            //先找出所有字典项并按字典组id组成Map
            CoreDictionariesExample dictExample = new CoreDictionariesExample();
            List<CoreDictionaries> coreDictionaries = coreDictionariesService.queryCoreDictionariesByExample(dictExample);
            Map<Long,List<CoreDictionaries>> dictMap = new HashMap<>();
            if (null != coreDictionaries && coreDictionaries.size() > 0) {
                for (CoreDictionaries coreDictionary : coreDictionaries) {
                    if (null != coreDictionary.getCoreDictGroupId()){
                        if (dictMap.containsKey(coreDictionary.getCoreDictGroupId())){
                            dictMap.get(coreDictionary.getCoreDictGroupId()).add(coreDictionary);
                        }else {
                            List<CoreDictionaries> coreDictionariesTempList = new ArrayList<>();
                            coreDictionariesTempList.add(coreDictionary);
                            dictMap.put(coreDictionary.getCoreDictGroupId(),coreDictionariesTempList);
                        }
                    }
                    //把字典也放入缓存中
                    redisTemplate.opsForValue().set(RedisKeyContant.CORE_DICTIONARIES+coreDictionary.getDictCode()+":", coreDictionary);
                }
            }
            //将字典组和字典组成Key-value放入redis中
            for (CoreDictionariesGroup coreDictionariesGroup : coreDictionariesGroups) {
                if(dictMap.containsKey(coreDictionariesGroup.getCoreDictGroupId())){
                    redisTemplate.opsForList().leftPush(RedisKeyContant.CORE_DICTIONARIES_GROUP+coreDictionariesGroup.getDictGroupCode()+":",dictMap.get(coreDictionariesGroup.getCoreDictGroupId()));
                }
            }
        }
    }

    @Override
    public void refreshSystemCach() throws Exception{
        initSystemCach();
    }
}
