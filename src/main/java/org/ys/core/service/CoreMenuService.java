package org.ys.core.service;

import org.ys.common.page.PageBean;
import org.ys.core.controller.vo.CoreMenuCondition;
import org.ys.core.model.CoreMenu;
import org.ys.core.model.CoreMenuExample;

import java.util.List;
import java.util.Set;

/**
 * 菜单操作接口
 */
public interface CoreMenuService {
	/**
	 * 根据id查找
	 * @param coreMenuId
	 * @return
	 * @throws Exception
	 */
	public CoreMenu queryCoreMenuById(Long coreMenuId) throws Exception;

	/**
	 * 保存
	 * @param coreMenu
	 * @throws Exception
	 */
	public void save(CoreMenu coreMenu) throws Exception;

	/**
	 * 根据id更新
	 * @param coreMenu
	 * @throws Exception
	 */
	public void updateById(CoreMenu coreMenu) throws Exception;

	/**
	 * 根据指定条件更新
	 * @param corePermission
	 * @param example
	 * @throws Exception
	 */
	public void updateByExample(CoreMenu corePermission, CoreMenuExample example) throws Exception;

	/**
	 * 根据id删除
	 * @param coreMenuId
	 * @throws Exception
	 */
	public void delCoreMenuById(Long coreMenuId) throws Exception;

	/**
	 * 根据指定条件查找
	 * @param example
	 * @return
	 * @throws Exception
	 */
	public List<CoreMenu>queryCoreMenusByExample(CoreMenuExample example) throws Exception;

	/**
	 * 查找全部
	 * @return
	 * @throws Exception
	 */
	public List<CoreMenu>queryAll() throws Exception;

	/**
	 * 查找子节点
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<CoreMenu> queryCoreMenusByParentId(Long parentId) throws Exception;

	/**
	 * 查找某个节点下所有节点
	 * @param coreMenuId
	 * @return
	 * @throws Exception
	 */
	public Set<CoreMenu> queryAllSubCoreMenusByMenuId(Long coreMenuId) throws Exception;

	/**
	 * 根据指定条件分页
	 * @param example
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageBean<CoreMenu> pageCoreMenusByExample(CoreMenuExample example, int pageNum, int pageSize) throws Exception;

	/**
	 * 查找某个用户的菜单或权限集合
	 * @param coreUserId
	 * @return
	 */
	public List<CoreMenu> listCoreMenusByUserId(Long coreUserId);

	/**
	 * 查询菜单树,用户ID和用户名为空则查询全部
	 * @param coreMenuCondition
	 * @param menuType 获取菜单类型，0：获取所有菜单，包含按钮，1：获取所有菜单，不包含按钮
	 * @return
	 */
	public List<CoreMenu> findTree(CoreMenuCondition coreMenuCondition, String menuType);

	/**
	 * 查找某个用户的菜单或权限集合
	 * @param coreRoleId
	 * @return
	 */
	public List<CoreMenu> listCoreMenusByRoleId(Long coreRoleId);
}