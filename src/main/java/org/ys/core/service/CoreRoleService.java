package org.ys.core.service;

import java.util.List;

import org.ys.common.page.PageBean;
import org.ys.core.model.CoreRole;
import org.ys.core.model.CoreRoleExample;
import org.ys.core.model.CoreRoleMenu;

/**
 * 角色操作接口
 */
public interface CoreRoleService {
	/**
	 * 根据id查询
	 * @param coreRoleId
	 * @return
	 * @throws Exception
	 */
	public CoreRole queryCoreRoleById(Long coreRoleId) throws Exception;

	/**
	 * 保存角色
	 * @param coreRole
	 * @throws Exception
	 */
	public void save(CoreRole coreRole) throws Exception;

	/**
	 * 根据id更新角色
	 * @param coreRole
	 * @throws Exception
	 */
	public void updateById(CoreRole coreRole) throws Exception;

	/**
	 * 根据指定条件更新
	 * @param coreRole
	 * @param example
	 * @throws Exception
	 */
	public void updateByExaple(CoreRole coreRole, CoreRoleExample example) throws Exception;

	/**
	 * 根据id删除
	 * @param coreRoleId
	 * @throws Exception
	 */
	public void delCoreRoleById(Long coreRoleId) throws Exception;

	/**
	 * 根据指定条件查询
	 * @param example
	 * @return
	 * @throws Exception
	 */
	public List<CoreRole>queryCoreRolesByExample(CoreRoleExample example) throws Exception;

	/**
	 * 查询全部
	 * @return
	 * @throws Exception
	 */
	public List<CoreRole>queryAll() throws Exception;

	/**
	 * 根据指定条件分页
	 * @param example
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageBean<CoreRole> pageCoreRolesByExample(CoreRoleExample example, int pageNum, int pageSize) throws Exception;

	/**
	 * 查找某个用户的角色
	 * @param coreUserId
	 * @return
	 */
	public List<CoreRole> listCoreRolesByUserId(Long coreUserId);
}