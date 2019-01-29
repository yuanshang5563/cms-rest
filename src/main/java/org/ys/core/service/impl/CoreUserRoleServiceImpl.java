package org.ys.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.core.dao.CoreUserRoleMapper;
import org.ys.core.model.CoreUserRole;
import org.ys.core.service.CoreUserRoleService;

import java.util.List;

@Service("coreUserRoleService")
public class CoreUserRoleServiceImpl implements CoreUserRoleService {
	@Autowired
	private CoreUserRoleMapper coreUserRoleMapper;

	@Override
	public void insertCoreUserRole(CoreUserRole coreUserRole) {
		if(null != coreUserRole) {
			coreUserRoleMapper.insertCoreUserRole(coreUserRole);
		}
	}

	@Override
	public void delCoreUserRoleByUserId(Long coreUserId) {
		if(null != coreUserId) {
			coreUserRoleMapper.delCoreUserRoleByUserId(coreUserId);
		}
	}

	@Override
	public void delCoreUserRoleByRoleId(Long coreRoleId) {
		if(null != coreRoleId) {
			coreUserRoleMapper.delCoreUserRoleByRoleId(coreRoleId);
		}
	}

	@Override
	public List<CoreUserRole> findCoreUserRoleByUserId(Long coreUserId){
		if(null == coreUserId) {
			return null;
		}
		return coreUserRoleMapper.findCoreUserRoleByUserId(coreUserId);
	}
}
