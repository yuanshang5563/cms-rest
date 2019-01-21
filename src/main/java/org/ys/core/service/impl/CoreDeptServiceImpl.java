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
import org.ys.core.dao.CoreDeptMapper;
import org.ys.core.model.CoreDept;
import org.ys.core.model.CoreDeptExample;
import org.ys.core.model.CoreDeptExample.Criteria;
import org.ys.core.service.CoreDeptService;

import com.github.pagehelper.PageHelper;

@Service("coreDeptService")
public class CoreDeptServiceImpl implements CoreDeptService {
	@Autowired
	private CoreDeptMapper coreDeptMapper;

	@Override
	public CoreDept queryCoreDeptById(Long coreDeptId) throws Exception {
		if(null == coreDeptId) {
			return null;
		}
		return coreDeptMapper.selectByPrimaryKey(coreDeptId);
	}

	@Override
	public int save(CoreDept coreDept) throws Exception {
		if(null != coreDept) {
			return coreDeptMapper.insert(coreDept);
		}
		return 0;
	}

	@Override
	public int updateById(CoreDept coreDept) throws Exception {
		if(null != coreDept) {
			return coreDeptMapper.updateByPrimaryKey(coreDept);
		}
		return 0;
	}

	@Override
	public int updateByExaple(CoreDept coreDept, CoreDeptExample example) throws Exception {
		if(null != coreDept && null != example) {
			return coreDeptMapper.updateByExample(coreDept, example);
		}
		return 0;
	}

	@Override
	public int delCoreDeptById(Long coreDeptId) throws Exception {
		if(null != coreDeptId) {
			return coreDeptMapper.deleteByPrimaryKey(coreDeptId);
		}
		return 0;
	}

	@Override
	public List<CoreDept> queryCoreDeptsByExample(CoreDeptExample example) throws Exception {
		if(null == example) {
			return null;
		}
		return coreDeptMapper.selectByExample(example);
	}

	@Override
	public PageBean<CoreDept> pageCoreDeptsByExample(CoreDeptExample example, int pageNum, int pageSize) throws Exception {
		if(null == example) {
			return null;
		}
		PageHelper.startPage(pageNum, pageSize, true);
		List<CoreDept> deptList = coreDeptMapper.selectByExample(example);
		return new PageBean<CoreDept>(deptList);
	}
	
	@Override
	public List<CoreDept> queryCoreDeptsByParentId(Long parentId) throws Exception {
		if(null == parentId) {
			return null;
		}
		CoreDeptExample example = new CoreDeptExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentCoreDeptIdEqualTo(parentId);
		return coreDeptMapper.selectByExample(example);
	}
	
	private Set<CoreDept> queryAllSubCoreDeptsByDeptId(Long coreDeptId,Set<CoreDept> allSubDepts,List<CoreDept> allDepts) throws Exception  {
        for(CoreDept dept : allDepts){
        	if(dept.getCoreDeptId() == coreDeptId) {
        		allSubDepts.add(dept);
        	}
            //遍历出父id等于参数的id，add进子节点集合
            if(dept.getParentCoreDeptId()==coreDeptId){
                //递归遍历下一级
            	queryAllSubCoreDeptsByDeptId(dept.getCoreDeptId(),allSubDepts,allDepts);
                allSubDepts.add(dept);
            }
        }
        return allSubDepts;	
	}

	@Override
	public Set<CoreDept> queryAllSubCoreDeptsByDeptId(Long coreDeptId) throws Exception {
		if(null == coreDeptId) {
			return null;
		}
		Set<CoreDept> allSubDepts = new HashSet<>();
		//一次找出所有节点然后处理
		CoreDeptExample example = new CoreDeptExample();
		List<CoreDept> allDepts = coreDeptMapper.selectByExample(example);
		return queryAllSubCoreDeptsByDeptId(coreDeptId,allSubDepts,allDepts);
	}

}
