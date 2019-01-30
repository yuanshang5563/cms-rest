package org.ys.core.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.core.dao.CoreRoleMapper;
import org.ys.core.dao.CoreRoleMenuMapper;
import org.ys.core.model.CoreRole;
import org.ys.core.model.CoreRoleExample;
import org.ys.core.model.CoreRoleMenu;
import org.ys.core.service.CoreRoleService;

import com.github.pagehelper.PageHelper;

@Service("coreRoleService")
public class CoreRoleServiceImpl implements CoreRoleService {
	@Autowired
	private CoreRoleMapper coreRoleMapper;
	
	@Autowired
	private CoreRoleMenuMapper coreRoleMenuMapper;

	@Override
	public List<CoreRole> queryAll() throws Exception {
		CoreRoleExample example = new CoreRoleExample();
		return coreRoleMapper.selectByExample(example);
	}

	@Override
	public CoreRole queryCoreRoleById(Long coreRoleId) throws Exception {
		if(null == coreRoleId) {
			return null;
		}
		return coreRoleMapper.selectByPrimaryKey(coreRoleId);
	}

	@Override
	public void save(CoreRole coreRole) throws Exception {
		if(null != coreRole) {
			coreRoleMapper.insert(coreRole);
		}
	}

	@Override
	public void updateById(CoreRole coreRole) throws Exception {
		if(null != coreRole) {
			coreRoleMapper.updateByPrimaryKey(coreRole);
		}
	}

	@Override
	public void updateByExaple(CoreRole coreRole, CoreRoleExample example) throws Exception {
		if(null != coreRole && null != example) {
			coreRoleMapper.updateByExample(coreRole, example);
		}
	}

	@Override
	public void delCoreRoleById(Long coreRoleId) throws Exception {
		if(null != coreRoleId) {
			coreRoleMapper.deleteByPrimaryKey(coreRoleId);
		}
	}

	@Override
	public List<CoreRole> queryCoreRolesByExample(CoreRoleExample example) throws Exception {
		if(null == example) {
			return null;
		}
		return coreRoleMapper.selectByExample(example);
	}

	@Override
	public PageBean<CoreRole> pageCoreRolesByExample(CoreRoleExample example, int pageNum, int pageSize) throws Exception {
		if(null == example) {
			return null;
		}
		PageHelper.startPage(pageNum, pageSize, true);
		List<CoreRole> roles = coreRoleMapper.selectByExample(example);
		return new PageBean<CoreRole>(roles);
	}

	@Override
	public List<CoreRole> listCoreRolesByUserId(Long coreUserId) {
		if(null == coreUserId) {
			return null;
		}
		return coreRoleMapper.listCoreRolesByUserId(coreUserId);
	}
}
