package org.ys.core.dao;

import org.springframework.stereotype.Repository;
import org.ys.core.model.CoreUserRole;

import java.util.List;

@Repository
public interface CoreUserRoleMapper {

	public void insertCoreUserRole(CoreUserRole coreUserRole);
	
	public void delCoreUserRoleByUserId(Long coreUserId);
	
	public void delCoreUserRoleByRoleId(Long coreRoleId);

	public List<CoreUserRole> findCoreUserRoleByUserId(Long coreUserId);
}