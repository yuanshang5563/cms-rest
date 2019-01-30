package org.ys.core.service;

import java.util.List;

import org.ys.common.page.PageBean;
import org.ys.core.model.CoreDictionaries;
import org.ys.core.model.CoreDictionariesExample;

/**
 * 数据字典操作接口
 */
public interface CoreDictionariesService {
	/**
	 * 根据id查询
	 * @param coreDictId
	 * @return
	 * @throws Exception
	 */
	public CoreDictionaries queryCoreDictionariesById(Long coreDictId) throws Exception;

	/**
	 * 保存
	 * @param coreDictionaries
	 * @throws Exception
	 */
	public void save(CoreDictionaries coreDictionaries) throws Exception;

	/**
	 * 根据id更新
	 * @param coreDictionaries
	 * @throws Exception
	 */
	public void updateById(CoreDictionaries coreDictionaries) throws Exception;

	/**
	 * 根据指定条件更新
	 * @param coreDictionaries
	 * @param example
	 * @throws Exception
	 */
	public void updateByExaple(CoreDictionaries coreDictionaries, CoreDictionariesExample example) throws Exception;

	/**
	 * 根据id删除
	 * @param coreDictId
	 * @throws Exception
	 */
	public void delCoreDictionariesById(Long coreDictId) throws Exception;

	/**
	 * 根据指定条件查询
	 * @param example
	 * @return
	 * @throws Exception
	 */
	public List<CoreDictionaries> queryCoreDictionariesByExample(CoreDictionariesExample example) throws Exception;

	/**
	 * 查询全部
	 * @return
	 * @throws Exception
	 */
	public List<CoreDictionaries> queryAll() throws Exception;

	/**
	 * 根据指定条件分页
	 * @param example
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageBean<CoreDictionaries> pageCoreDictionariesByExample(CoreDictionariesExample example, int pageNum, int pageSize) throws Exception;

	/**
	 * 根据字典组id查询
	 * @param coreDictGroupId
	 * @return
	 */
	public List<CoreDictionaries> listCoreDictionariesByDictGroupId(Long coreDictGroupId);

	/**
	 * 根据字典组查询
	 * @param dictGroupCode
	 * @return
	 */
	public List<CoreDictionaries> listCoreDictionariesByDictGroupCode(String dictGroupCode);

	/**
	 * 根据id获取数据字典
	 * @param dictCode
	 * @return
	 */
	public String getDictionariesValueByCode(String dictCode);
}