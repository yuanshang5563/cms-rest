package org.ys.core.service;

import org.ys.core.model.CoreUserRole;

import java.util.List;

/**
 * 系统用户和权限操作接口
 */
public interface CoreUserRoleService {

	/**
	 * 保存关联
	 * @param coreUserRole
	 */
	public void insertCoreUserRole(CoreUserRole coreUserRole);

	/**
	 * 删除用户的角色信息
	 * @param coreUserId
	 */
	public void delCoreUserRoleByUserId(Long coreUserId);

	/**
	 * 删除用户某个的角色
	 * @param coreRoleId
	 */
	public void delCoreUserRoleByRoleId(Long coreRoleId);

	/**
	 * 查找用户的角色信息
	 * @param coreUserId
	 */
	public List<CoreUserRole> findCoreUserRoleByUserId(Long coreUserId);
}