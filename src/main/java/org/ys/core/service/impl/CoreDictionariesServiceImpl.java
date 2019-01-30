package org.ys.core.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.ys.common.constant.DictionariesGroupContant;
import org.ys.common.constant.RedisKeyContant;
import org.ys.common.page.PageBean;
import org.ys.core.dao.CoreDictionariesMapper;
import org.ys.core.model.CoreDictionaries;
import org.ys.core.model.CoreDictionariesExample;
import org.ys.core.service.CoreDictionariesService;

import com.github.pagehelper.PageHelper;

@Service("coreDictionariesService")
public class CoreDictionariesServiceImpl implements CoreDictionariesService {
	
	@Autowired
	private CoreDictionariesMapper coreDictionariesMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public CoreDictionaries queryCoreDictionariesById(Long coreDictId) throws Exception {
		if(null == coreDictId) {
			return null;
		}
		return coreDictionariesMapper.selectByPrimaryKey(coreDictId);
	}

	@Override
	public void save(CoreDictionaries coreDictionaries) throws Exception {
		if(null != coreDictionaries) {
			coreDictionariesMapper.insert(coreDictionaries);
		}
	}

	@Override
	public void updateById(CoreDictionaries coreDictionaries) throws Exception {
		if(null != coreDictionaries && null != coreDictionaries.getCoreDictId()) {
			coreDictionariesMapper.updateByPrimaryKey(coreDictionaries);
		}
	}

	@Override
	public void updateByExaple(CoreDictionaries coreDictionaries, CoreDictionariesExample example) throws Exception {
		if(null != coreDictionaries && null != example) {
			coreDictionariesMapper.updateByExample(coreDictionaries, example);
		}
	}

	@Override
	public void delCoreDictionariesById(Long coreDictId) throws Exception {
		if(null != coreDictId) {
			coreDictionariesMapper.deleteByPrimaryKey(coreDictId);
		}
	}

	@Override
	public List<CoreDictionaries> queryCoreDictionariesByExample(CoreDictionariesExample example) throws Exception {
		if(null == example) {
			return null;
		}
		return coreDictionariesMapper.selectByExample(example);
	}

	@Override
	public List<CoreDictionaries> queryAll() throws Exception {
		CoreDictionariesExample example =  new CoreDictionariesExample();
		return coreDictionariesMapper.selectByExample(example);
	}

	@Override
	public PageBean<CoreDictionaries> pageCoreDictionariesByExample(CoreDictionariesExample example, int pageNum, int pageSize) throws Exception {
		if(null == example) {
			return null;
		}
		PageHelper.startPage(pageNum, pageSize, true);
		List<CoreDictionaries> dictList = coreDictionariesMapper.selectByExample(example);
		return new PageBean<CoreDictionaries>(dictList);
	}

	@Override
	public List<CoreDictionaries> listCoreDictionariesByDictGroupId(Long coreDictGroupId) {
		if(null == coreDictGroupId) {
			return null;
		}
		return coreDictionariesMapper.listCoreDictionariesByDictGroupId(coreDictGroupId);
	}

	@Override
	public List<CoreDictionaries> listCoreDictionariesByDictGroupCode(String dictGroupCode) {
		if(StringUtils.isEmpty(dictGroupCode)) {
			return null;
		}
		//先从缓存中找再找数据库
		List<CoreDictionaries> list = (List<CoreDictionaries>) redisTemplate.opsForList().leftPop(RedisKeyContant.CORE_DICTIONARIES_GROUP+ dictGroupCode.trim()+":");
		if (null == list || list.size() <= 0){
			list = coreDictionariesMapper.listCoreDictionariesByDictGroupCode(dictGroupCode);
		}
		return list;
	}

	@Override
	public String getDictionariesValueByCode(String dictCode) {
		if(StringUtils.isEmpty(dictCode)) {
			return null;
		}
		//先从缓存中找再找数据库
		CoreDictionaries dictionaries = (CoreDictionaries) redisTemplate.opsForValue().get(RedisKeyContant.CORE_DICTIONARIES+dictCode.trim()+":");
		if(null == dictionaries){
			CoreDictionariesExample example = new CoreDictionariesExample();
			example.createCriteria().andDictCodeEqualTo(dictCode.trim());
			List<CoreDictionaries> coreDictionariesList = coreDictionariesMapper.selectByExample(example);
			if(null != coreDictionariesList && coreDictionariesList.size() > 0){
				dictionaries = coreDictionariesList.get(0);
			}
		}
		if(null == dictionaries){
			return null;
		}
		return dictionaries.getDictValue();
	}

}
