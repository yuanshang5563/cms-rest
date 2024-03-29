package org.ys.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.ys.common.constant.CrawlerConstant;
import org.ys.common.constant.DictionariesGroupConstant;
import org.ys.common.constant.RedisKeyConstant;
import org.ys.common.page.PageBean;
import org.ys.core.dao.CoreParameterMapper;
import org.ys.core.model.CoreDictionaries;
import org.ys.core.model.CoreParameter;
import org.ys.core.model.CoreParameterExample;
import org.ys.core.service.CoreDictionariesService;
import org.ys.core.service.CoreParameterService;

import com.github.pagehelper.PageHelper;

@Service("coreParameterService")
public class CoreParameterServiceImpl implements CoreParameterService {
	
	@Autowired
	private CoreParameterMapper coreParameterMapper;

	@Autowired
	private CoreDictionariesService coreDictionariesService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public CoreParameter queryCoreParameterById(Long coreParamId) throws Exception {
		if(null == coreParamId) {
			return null;
		}
		return coreParameterMapper.selectByPrimaryKey(coreParamId);
	}

	@Override
	public void save(CoreParameter coreParameter) throws Exception {
		if(null != coreParameter) {
			coreParameterMapper.insert(coreParameter);
		}
	}

	@Override
	public void updateById(CoreParameter coreParameter) throws Exception {
		if(null != coreParameter && null != coreParameter.getCoreParamId()) {
			coreParameterMapper.updateByPrimaryKey(coreParameter);
		}
	}

	@Override
	public void updateByExample(CoreParameter coreParameter, CoreParameterExample example) throws Exception {
		if(null != coreParameter && null != example) {
			coreParameterMapper.updateByExample(coreParameter, example);
		}
	}

	@Override
	public void delCoreParameterById(Long coreParamId) throws Exception {
		if(null != coreParamId) {
			coreParameterMapper.deleteByPrimaryKey(coreParamId);
		}
	}

	@Override
	public List<CoreParameter> queryCoreParametersByExample(CoreParameterExample example) throws Exception {
		if(null == example) {
			return null;
		}
		return coreParameterMapper.selectByExample(example);
	}

	@Override
	public List<CoreParameter> queryAll() throws Exception {
		CoreParameterExample example = new CoreParameterExample();
		return coreParameterMapper.selectByExample(example);
	}

	@Override
	public PageBean<CoreParameter> pageCoreParametersByExample(CoreParameterExample example, int pageNum, int pageSize) throws Exception {
		if(null == example) {
			return null;
		}
		PageHelper.startPage(pageNum, pageSize, true);
		List<CoreParameter> paramList = coreParameterMapper.selectByExample(example);
		return new PageBean<CoreParameter>(paramList);
	}

	@Override
	public Map<String, List<CoreDictionaries>> initDictionaries() {
		Map<String,List<CoreDictionaries>> dictMap = new HashMap<>();
		List<CoreDictionaries> dictList = (List<CoreDictionaries>) redisTemplate.opsForList().leftPop(RedisKeyConstant.CORE_DICTIONARIES_GROUP+ DictionariesGroupConstant.GROUP_PARAM_TYPE+":");
		//如果缓存中没有就去数据库中找
		if(null == dictList || dictList.size() > 0){
			dictList = coreDictionariesService.listCoreDictionariesByDictGroupCode(DictionariesGroupConstant.GROUP_PARAM_TYPE);
		}
		dictMap.put(DictionariesGroupConstant.GROUP_PARAM_TYPE,dictList);
		return dictMap;
	}

	@Override
	public void convertCoreParametersDictionaries(List<CoreParameter> coreParameters) {
		if(null != coreParameters && coreParameters.size() > 0){
			for (CoreParameter coreParameter : coreParameters) {
				if(StringUtils.isNotEmpty(coreParameter.getParamType())){
					coreParameter.setParamType(coreDictionariesService.getDictionariesValueByCode(coreParameter.getParamType()));
				}
			}
		}
	}

	@Override
	public CoreParameter queryCoreParameterByParamCode(String paramCode) throws Exception {
		if(StringUtils.isEmpty(paramCode)){
			return null;
		}
		//先从缓存中找再找数据库
		CoreParameter parameter = (CoreParameter) redisTemplate.opsForValue().get(RedisKeyConstant.CORE_PARAMETER+paramCode+":");
		if(null == parameter){
			CoreParameterExample example = new CoreParameterExample();
			example.createCriteria().andParamCodeEqualTo(StringUtils.trim(paramCode));
			List<CoreParameter> parameters = coreParameterMapper.selectByExample(example);
			if(null != parameters && parameters.size() > 0){
				parameter = parameters.get(0);
			}
		}
		return parameter;
	}

}
