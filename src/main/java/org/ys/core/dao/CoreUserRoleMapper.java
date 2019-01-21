package org.ys.core.dao;

import org.ys.core.model.CoreUserRole;

public interface CoreUserRoleMapper {

	public void insertCoreUserRole(CoreUserRole coreUserRole);
	
	public void delCoreUserRoleByUserId(Long coreUserId);
	
	public void delCoreUserRoleByRoleId(Long coreRoleId);
}