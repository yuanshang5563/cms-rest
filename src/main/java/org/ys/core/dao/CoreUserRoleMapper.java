package org.ys.core.dao;

import org.ys.core.model.CoreUserRole;

import java.util.List;

public interface CoreUserRoleMapper {

	public void insertCoreUserRole(CoreUserRole coreUserRole);
	
	public void delCoreUserRoleByUserId(Long coreUserId);
	
	public void delCoreUserRoleByRoleId(Long coreRoleId);

	public List<CoreUserRole> findCoreUserRoleByUserId(Long coreUserId);
}