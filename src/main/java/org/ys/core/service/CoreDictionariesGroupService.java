package org.ys.core.service;

import org.ys.common.page.PageBean;
import org.ys.core.controller.vo.CoreDictGroupCondition;
import org.ys.core.model.CoreDictionariesGroup;
import org.ys.core.model.CoreDictionariesGroupExample;

import java.util.List;
import java.util.Set;

/**
 * 字典组操作接口
 */
public interface CoreDictionariesGroupService {
	/**
	 * 根据id查找
	 * @param coreDictGroupId
	 * @return
	 * @throws Exception
	 */
	public CoreDictionariesGroup queryCoreDictionariesGroupById(Long coreDictGroupId) throws Exception;

	/**
	 * 保存
	 * @param coreDictionariesGroup
	 * @throws Exception
	 */
	public void save(CoreDictionariesGroup coreDictionariesGroup) throws Exception;

	/**
	 * 根据Id更新
	 * @param coreDictionariesGroup
	 * @throws Exception
	 */
	public void updateById(CoreDictionariesGroup coreDictionariesGroup) throws Exception;

	/**
	 * 根据指定条件更新
	 * @param coreDictionariesGroup
	 * @param example
	 * @throws Exception
	 */
	public void updateByExample(CoreDictionariesGroup coreDictionariesGroup, CoreDictionariesGroupExample example) throws Exception;

	/**
	 * 根据id删除
	 * @param coreDictGroupId
	 * @throws Exception
	 */
	public void delCoreDictionariesGroupById(Long coreDictGroupId) throws Exception;

	/**
	 * 根据指定条件查询
	 * @param example
	 * @return
	 * @throws Exception
	 */
	public List<CoreDictionariesGroup> queryCoreDictionariesGroupsByExample(CoreDictionariesGroupExample example) throws Exception;

	/**
	 * 查询全部
	 * @return
	 * @throws Exception
	 */
	public List<CoreDictionariesGroup> queryAll() throws Exception;

	/**
	 * 根据指定条件分页
	 * @param example
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageBean<CoreDictionariesGroup> pageCoreDictionariesGroupsByExample(CoreDictionariesGroupExample example, int pageNum, int pageSize) throws Exception;

	/**
	 * 查找某个节点的子节点
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<CoreDictionariesGroup> queryCoreDictionariesGroupsByParentId(Long parentId) throws Exception;

	/**
	 * 查找某个节点下的所有子节点
	 * @param coreDictGroupId
	 * @return
	 * @throws Exception
	 */
	public Set<CoreDictionariesGroup> queryAllSubCoreDictionariesGroupsByDictGroupId(Long coreDictGroupId) throws Exception;

	/**
	 * 查询数据字典组的树
	 * @param coreDictGroupCondition
	 * @return
	 */
	public List<CoreDictionariesGroup> findTree(CoreDictGroupCondition coreDictGroupCondition);
	
}