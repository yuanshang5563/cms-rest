package org.ys.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.core.dao.CoreDictionariesGroupMapper;
import org.ys.core.model.CoreDictionariesGroup;
import org.ys.core.model.CoreDictionariesGroupExample;
import org.ys.core.model.CoreDictionariesGroupExample.Criteria;
import org.ys.core.service.CoreDictionariesGroupService;

import com.github.pagehelper.PageHelper;

@Service("coreDictionariesGroupService")
public class CoreDictionariesGroupServiceImpl implements CoreDictionariesGroupService {
	
	@Autowired
	private CoreDictionariesGroupMapper coreDictionariesGroupMapper;

	@Override
	public CoreDictionariesGroup queryCoreDictionariesGroupById(Long coreDictGroupId) throws Exception {
		if(null == coreDictGroupId) {
			return null;
		}
		return coreDictionariesGroupMapper.selectByPrimaryKey(coreDictGroupId);
	}

	@Override
	public void save(CoreDictionariesGroup coreDictionariesGroup) throws Exception {
		if(null != coreDictionariesGroup) {
			coreDictionariesGroupMapper.insert(coreDictionariesGroup);
		}
	}

	@Override
	public void updateById(CoreDictionariesGroup coreDictionariesGroup) throws Exception {
		if(null != coreDictionariesGroup && null != coreDictionariesGroup.getCoreDictGroupId()) {
			coreDictionariesGroupMapper.updateByPrimaryKey(coreDictionariesGroup);
		}
	}

	@Override
	public void updateByExaple(CoreDictionariesGroup coreDictionariesGroup, CoreDictionariesGroupExample example) throws Exception {
		if(null != coreDictionariesGroup && null != example) {
			coreDictionariesGroupMapper.updateByExample(coreDictionariesGroup, example);
		}
	}

	@Override
	public void delCoreDictionariesGroupById(Long coreDictGroupId) throws Exception {
		if(null != coreDictGroupId) {
			coreDictionariesGroupMapper.deleteByPrimaryKey(coreDictGroupId);
		}
	}

	@Override
	public List<CoreDictionariesGroup> queryCoreDictionariesGroupsByExample(CoreDictionariesGroupExample example) throws Exception {
		if(null == example) {
			return null;
		}
		return coreDictionariesGroupMapper.selectByExample(example);
	}

	@Override
	public PageBean<CoreDictionariesGroup> pageCoreDictionariesGroupsByExample(CoreDictionariesGroupExample example, int pageNum, int pageSize) throws Exception {
		if(null == example) {
			return null;
		}
		PageHelper.startPage(pageNum, pageSize, true);
		List<CoreDictionariesGroup> groupList = coreDictionariesGroupMapper.selectByExample(example);
		return new PageBean<CoreDictionariesGroup>(groupList);
	}

	@Override
	public List<CoreDictionariesGroup> queryCoreDictionariesGroupsByParentId(Long parentId) throws Exception {
		if(null == parentId) {
			return null;
		}
		CoreDictionariesGroupExample example = new CoreDictionariesGroupExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentCoreDictGroupIdEqualTo(parentId);
		return coreDictionariesGroupMapper.selectByExample(example);
	}
	
	private Set<CoreDictionariesGroup> queryAllSubCoreDeptsByDeptId(Long coreDictGroupId,Set<CoreDictionariesGroup> allSubGroups,
	List<CoreDictionariesGroup> allGroups) throws Exception  {
        for(CoreDictionariesGroup group : allGroups){
        	if(group.getCoreDictGroupId() == coreDictGroupId) {
        		allSubGroups.add(group);
        	}
            //遍历出父id等于参数的id，add进子节点集合
            if(group.getParentCoreDictGroupId()==coreDictGroupId){
                //递归遍历下一级
            	queryAllSubCoreDeptsByDeptId(group.getCoreDictGroupId(),allSubGroups,allGroups);
            	allSubGroups.add(group);
            }
        }
        return allSubGroups;	
	}

	@Override
	public Set<CoreDictionariesGroup> queryAllSubCoreDictionariesGroupsByDictGroupId(Long coreDictGroupId) throws Exception {
		if(null == coreDictGroupId) {
			return null;
		}
		Set<CoreDictionariesGroup> allSubGroups = new HashSet<>();
		//一次找出所有节点然后处理
		CoreDictionariesGroupExample example = new CoreDictionariesGroupExample();
		List<CoreDictionariesGroup> allGroups = coreDictionariesGroupMapper.selectByExample(example);
		return queryAllSubCoreDeptsByDeptId(coreDictGroupId,allSubGroups,allGroups);
	}
}
