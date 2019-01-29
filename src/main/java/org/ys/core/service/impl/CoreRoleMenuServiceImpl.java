package org.ys.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.core.dao.CoreRoleMenuMapper;
import org.ys.core.model.CoreRoleMenu;
import org.ys.core.service.CoreRoleMenuService;

import java.util.List;

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
	public void saveRoleMenus(List<CoreRoleMenu> coreRoleMenuList) throws Exception {
		if(null != coreRoleMenuList && coreRoleMenuList.size() > 0) {
			//更新角色菜单映射
			coreRoleMenuMapper.delCoreRoleMenuByRoleId(coreRoleMenuList.get(0).getCoreRoleId());
			for (CoreRoleMenu coreRoleMenu : coreRoleMenuList) {
				if(null != coreRoleMenu.getCoreRoleId() && null != coreRoleMenu.getCoreMenuId()) {
					coreRoleMenuMapper.insertCoreRoleMenu(coreRoleMenu);
				}
			}
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
