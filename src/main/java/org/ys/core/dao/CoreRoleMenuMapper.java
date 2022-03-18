package org.ys.core.dao;

import org.springframework.stereotype.Repository;
import org.ys.core.model.CoreRoleMenu;

@Repository
public interface CoreRoleMenuMapper {

	public void insertCoreRoleMenu(CoreRoleMenu coreRoleMenu);
	
	public void delCoreRoleMenuByMenuId(Long coreMenuId);
	
	public void delCoreRoleMenuByRoleId(Long coreRoleId);
}