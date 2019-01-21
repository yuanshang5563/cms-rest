package org.ys.core.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.page.PageBean;
import org.ys.core.dao.CoreMenuMapper;
import org.ys.core.model.CoreMenu;
import org.ys.core.model.CoreMenuExample;
import org.ys.core.model.CoreMenuExample.Criteria;
import org.ys.core.service.CoreMenuService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("coreMenuService")
public class CoreMenuServiceImpl implements CoreMenuService{
	@Autowired
	private CoreMenuMapper coreMenuMapper;

	@Override
	public CoreMenu queryCoreMenuById(Long coreMenuId) throws Exception {
		if(null == coreMenuId){
			return null;
		}
		return coreMenuMapper.selectByPrimaryKey(coreMenuId);
	}

	@Override
	public void save(CoreMenu coreMenu) throws Exception {
		if(null != coreMenu) {
			coreMenuMapper.insert(coreMenu);
		}
	}

	@Override
	public void updateById(CoreMenu coreMenu) throws Exception {
		if(null != coreMenu) {
			coreMenuMapper.updateByPrimaryKey(coreMenu);
		}
	}

	@Override
	public void updateByExaple(CoreMenu corePermission, CoreMenuExample example) throws Exception {
		if(null != corePermission && null != example) {
			coreMenuMapper.updateByExample(corePermission, example);
		}
		
	}

	@Override
	public void delCoreMenuById(Long coreMenuId) throws Exception {
		if(null != coreMenuId) {
			coreMenuMapper.deleteByPrimaryKey(coreMenuId);
		}
	}

	@Override
	public List<CoreMenu> queryCoreMenusByExample(CoreMenuExample example) throws Exception {
		if(null == example) {
			return null;
		}
		return coreMenuMapper.selectByExample(example);
	}

	@Override
	public List<CoreMenu> queryCoreMenusByParentId(Long parentId) throws Exception {
		if(null == parentId) {
			return null;
		}
		CoreMenuExample example = new CoreMenuExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentCoreMenuIdEqualTo(parentId);
		return coreMenuMapper.selectByExample(example);
	}
	
	private Set<CoreMenu> queryAllSubCoreMenusByMenuId(Long coreMenuId,Set<CoreMenu> allSubMenus,List<CoreMenu> allMenus) throws Exception  {
        for(CoreMenu menu : allMenus){
        	if(menu.getCoreMenuId() == coreMenuId) {
        		allSubMenus.add(menu);
        	}
            //遍历出父id等于参数的id，add进子节点集合
            if(menu.getParentCoreMenuId() == coreMenuId){
                //递归遍历下一级
            	queryAllSubCoreMenusByMenuId(menu.getCoreMenuId(),allSubMenus,allMenus);
            	allSubMenus.add(menu);
            }
        }
        return allSubMenus;	
	}

	@Override
	public Set<CoreMenu> queryAllSubCoreMenusByMenuId(Long coreMenuId) throws Exception {
		if(null == coreMenuId) {
			return null;
		}
		Set<CoreMenu> allSubMenus = new HashSet<>();
		//一次找出所有节点然后处理
		CoreMenuExample example = new CoreMenuExample();
		List<CoreMenu> allMenus = coreMenuMapper.selectByExample(example);
		return queryAllSubCoreMenusByMenuId(coreMenuId,allSubMenus,allMenus);
	}

	@Override
	public PageBean<CoreMenu> pageCoreMenusByExample(CoreMenuExample example, int pageNum, int pageSize) throws Exception {
		if(null == example) {
			return null;
		}
		PageHelper.startPage(pageNum, pageSize, true);
		List<CoreMenu> menuList = coreMenuMapper.selectByExample(example);
		return new PageBean<CoreMenu>(menuList);
	}

	@Override
	public List<CoreMenu> listCoreMenusByUserId(Long coreUserId) {
		if(null == coreUserId) {
			return null;
		}
		return coreMenuMapper.listCoreMenusByUserId(coreUserId);
	}

}
