package org.ys.core.service;

import org.ys.core.model.CoreRoleMenu;

import java.util.List;

/**
 * 角色和菜单或权限的关联接口
 */
public interface CoreRoleMenuService {
	/**
	 * 保存关联
	 * @param coreRoleMenu
	 */
	public void insertCoreRoleMenu(CoreRoleMenu coreRoleMenu);

	/**
	 * 保存角色及其权限
	 * @param coreRoleMenuList
	 * @throws Exception
	 */
	public void saveRoleMenus(List<CoreRoleMenu> coreRoleMenuList) throws Exception;

	/**
	 * 清除某个菜单或权限的与角色的关联
	 * @param coreMenuId
	 */
	public void delCoreRoleMenuByMenuId(Long coreMenuId);

	/**
	 * 清除某个角色的权限和角色的关联
	 * @param coreRoleId
	 */
	public void delCoreRoleMenuByRoleId(Long coreRoleId);
}