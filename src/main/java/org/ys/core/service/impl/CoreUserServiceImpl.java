package org.ys.core.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.common.util.PasswordUtils;
import org.ys.core.dao.CoreUserMapper;
import org.ys.core.dao.CoreUserRoleMapper;
import org.ys.core.model.CoreUser;
import org.ys.core.model.CoreUserExample;
import org.ys.core.model.CoreUserRole;
import org.ys.core.service.CoreDictionariesService;
import org.ys.core.service.CoreUserService;

import com.github.pagehelper.PageHelper;

@Service("coreUserService")
public class CoreUserServiceImpl implements CoreUserService {
	@Autowired
	private CoreUserMapper coreUserMapper;
	
	@Autowired
	private CoreUserRoleMapper coreUserRoleMapper;

	@Autowired
	private CoreDictionariesService coreDictionariesService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public CoreUser queryCoreUserById(Long coreUserId) throws Exception {
		if(null == coreUserId) {
			return null;
		}
		return coreUserMapper.selectByPrimaryKey(coreUserId);
	}

	@Override
	public CoreUser queryCoreUserByUserName(String userName) throws Exception {
		if(StringUtils.isEmpty(userName)){
			return  null;
		}
		CoreUserExample example = new CoreUserExample();
		example.createCriteria().andUserNameEqualTo(userName.trim());
		List<CoreUser> coreUsers = coreUserMapper.selectByExample(example);
		if(null != coreUsers && coreUsers.size() > 0){
			return coreUsers.get(0);
		}else {
			return null;
		}
	}

	@Override
	public int save(CoreUser coreUser) throws Exception {
		if(null != coreUser) {
			return coreUserMapper.insert(coreUser);
		}
		return 0;
	}

	@Override
	public int updateById(CoreUser coreUser) throws Exception {
		if(null != coreUser) {
			return coreUserMapper.updateByPrimaryKey(coreUser);
		}
		return 0;
	}

	@Override
	public int updateByExample(CoreUser coreUser, CoreUserExample example) throws Exception {
		if(null != coreUser && null != example) {
			return coreUserMapper.updateByExample(coreUser, example);
		}
		return 0;
	}

	@Override
	public int delCoreUserById(Long coreUserId) throws Exception {
		if(null != coreUserId) {
			return coreUserMapper.deleteByPrimaryKey(coreUserId);
		}
		return 0;
	}

	@Override
	public List<CoreUser> queryCoreUsersByExample(CoreUserExample example) throws Exception {
		if(null == example) {
			return null;
		}
		return coreUserMapper.selectByExample(example);
	}

	@Override
	public PageBean<CoreUser> pageCoreUsersByExample(CoreUserExample example, int pageNum, int pageSize) throws Exception {
		if(null == example) {
			return null;
		}
		PageHelper.startPage(pageNum, pageSize, true);
		List<CoreUser> userList = coreUserMapper.selectByExample(example);
		return new PageBean<CoreUser>(userList);
	}

	@Override
	public void updateCoreUserAndRoles(CoreUser coreUser) throws Exception {
		if(null != coreUser) {
			if(StringUtils.isNotEmpty(coreUser.getPassword())){
				coreUser.setPassword(PasswordUtils.encode(coreUser.getPassword()));
			}
			if(null == coreUser.getCoreUserId() || coreUser.getCoreUserId() == 0l) {
                coreUser.setCreatedTime(new Date());
				coreUserMapper.insert(coreUser);
			}else {
				coreUser.setModifiedTime(new Date());
				coreUserMapper.updateByPrimaryKey(coreUser);
			}
			//更新角色映射
			List<CoreUserRole> userRoles = coreUser.getUserRoles();
			if(null != userRoles && userRoles.size() > 0) {
				coreUserRoleMapper.delCoreUserRoleByUserId(coreUser.getCoreUserId());
				for (CoreUserRole coreUserRole : userRoles) {
                    coreUserRole.setCoreUserId(coreUser.getCoreUserId());
					coreUserRoleMapper.insertCoreUserRole(coreUserRole);
				}				
			}
		}
	}

//	@Override
//	public Map<String,List<CoreDictionaries>> initDictionaries(){
//		Map<String,List<CoreDictionaries>> dictMap = new HashMap<>();
//		List<CoreDictionaries> dictList = (List<CoreDictionaries>) redisTemplate.opsForList().leftPop(RedisKeyConstant.CORE_DICTIONARIES_GROUP+ DictionariesGroupConstant.GROUP_SEX+":");
//		//如果缓存中没有就去数据库中找
//		if(null == dictList || dictList.size() > 0){
//			dictList = coreDictionariesService.listCoreDictionariesByDictGroupCode(DictionariesGroupConstant.GROUP_SEX);
//		}
//		dictMap.put(DictionariesGroupConstant.GROUP_SEX,dictList);
//		return dictMap;
//	}
//
//	@Override
//	public void convertCoreUsersDictionaries(List<CoreUser> coreUsers) {
//		if(null != coreUsers && coreUsers.size() > 0){
//			for (CoreUser coreUser : coreUsers) {
//				if(StringUtils.isNotEmpty(coreUser.getSex())){
//					coreUser.setSex(coreDictionariesService.getDictionariesValueByCode(coreUser.getSex()));
//				}
//			}
//		}
//	}
}