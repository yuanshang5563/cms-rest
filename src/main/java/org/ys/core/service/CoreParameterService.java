package org.ys.core.service;

import org.ys.common.page.PageBean;
import org.ys.core.model.CoreDictionaries;
import org.ys.core.model.CoreParameter;
import org.ys.core.model.CoreParameterExample;

import java.util.List;
import java.util.Map;

/**
 * 参数操作接口
 */
public interface CoreParameterService {
	/**
	 * 根据Id查询
	 * @param coreParamId
	 * @return
	 * @throws Exception
	 */
	public CoreParameter queryCoreParameterById(Long coreParamId) throws Exception;

	/**
	 * 保存
	 * @param coreParameter
	 * @throws Exception
	 */
	public void save(CoreParameter coreParameter) throws Exception;

	/**
	 * 根据id更新
	 * @param coreParameter
	 * @throws Exception
	 */
	public void updateById(CoreParameter coreParameter) throws Exception;

	/**
	 * 根据指定条件更新
	 * @param coreParameter
	 * @param example
	 * @throws Exception
	 */
	public void updateByExaple(CoreParameter coreParameter, CoreParameterExample example) throws Exception;

	/**
	 * 根据id删除
	 * @param coreParamId
	 * @throws Exception
	 */
	public void delCoreParameterById(Long coreParamId) throws Exception;

	/**
	 *  根据指定条件查询
	 * @param example
	 * @return
	 * @throws Exception
	 */
	public List<CoreParameter>queryCoreParametersByExample(CoreParameterExample example) throws Exception;

	/**
	 *  查询全部
	 * @return
	 * @throws Exception
	 */
	public List<CoreParameter>queryAll() throws Exception;

	/**
	 * 根据指定条件分页
	 * @param example
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageBean<CoreParameter> pageCoreParametersByExample(CoreParameterExample example, int pageNum, int pageSize) throws Exception;

	/**
	 * 获取字典Map
	 * @return
	 */
	public Map<String,List<CoreDictionaries>> initDictionaries();

	/**
	 * 替换参数中的数据字典码为值
	 * @param coreParameters
	 */
	public void convertCoreParametersDictionaries(List<CoreParameter> coreParameters);
}