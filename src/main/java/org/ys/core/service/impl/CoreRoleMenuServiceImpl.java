package org.ys.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.core.dao.CoreRoleMenuMapper;
import org.ys.core.model.CoreRoleMenu;
import org.ys.core.service.CoreRoleMenuService;

@Service("coreRoleMenuService")
public class CoreRoleMenuServiceImpl implements CoreRoleMenuService {
	@Autowired
	private CoreRoleMenuMapper coreRoleMenuMapper;

	@Override
	public void insertCoreRoleMenu(CoreRoleMenu coreRoleMenu) {
		if(null != coreRoleMenu) {
			coreRoleMenuMapper.insertCoreRoleMenu(coreRoleMenu);
		}
	}

	@Override
	public void delCoreRoleMenuByMenuId(Long coreMenuId) {
		if(null != coreMenuId) {
			coreRoleMenuMapper.delCoreRoleMenuByMenuId(coreMenuId);
		}
	}

	@Override
	public void delCoreRoleMenuByRoleId(Long coreRoleId) {
		if(null != coreRoleId) {
			coreRoleMenuMapper.delCoreRoleMenuByRoleId(coreRoleId);
		}
	}
}
