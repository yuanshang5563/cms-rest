package org.ys.core.service;

import java.util.List;
import java.util.Map;

import org.ys.common.page.PageBean;
import org.ys.core.model.CoreDictionaries;
import org.ys.core.model.CoreUser;
import org.ys.core.model.CoreUserExample;

/**
 * 系统用户操作接口
 */
public interface CoreUserService {
	/**
	 * 根据id查询
	 * @param coreUserId
	 * @return
	 * @throws Exception
	 */
	public CoreUser queryCoreUserById(Long coreUserId) throws Exception;

	/**
	 * 根据用户名查询
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public CoreUser queryCoreUserByUserName(String userName) throws Exception;

	/**
	 * 保存
	 * @param coreUser
	 * @return
	 * @throws Exception
	 */
	public int save(CoreUser coreUser) throws Exception;

	/**
	 * 根据id更新
	 * @param coreUser
	 * @return
	 * @throws Exception
	 */
	public int updateById(CoreUser coreUser) throws Exception;

	/**
	 * 根据条件更新
	 * @param coreUser
	 * @param example
	 * @return
	 * @throws Exception
	 */
	public int updateByExaple(CoreUser coreUser, CoreUserExample example) throws Exception;

	/**
	 * 根据id删除
	 * @param coreUserId
	 * @return
	 * @throws Exception
	 */
	public int delCoreUserById(Long coreUserId) throws Exception;

	/**
	 * 根据指定条件查询
	 * @param example
	 * @return
	 * @throws Exception
	 */
	public List<CoreUser>queryCoreUsersByExample(CoreUserExample example) throws Exception;

	/**
	 * 根据指定条件分布
	 * @param example
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageBean<CoreUser> pageCoreUsersByExample(CoreUserExample example, int pageNum, int pageSize) throws Exception;

	/**
	 * 更新用户及其权限
	 * @param coreUser
	 * @throws Exception
	 */
	public void updateCoreUserAndRoles(CoreUser coreUser) throws Exception;

//	/**
//	 * 获取字典Map
//	 * @return
//	 */
//	public Map<String,List<CoreDictionaries>> initDictionaries();
//
//    /**
//     * 替换用户中的数据字典码为值
//     * @param coreUsers
//     */
//    public void convertCoreUsersDictionaries(List<CoreUser> coreUsers);
}