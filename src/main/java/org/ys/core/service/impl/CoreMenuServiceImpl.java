package org.ys.core.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.common.constant.CoreMenuContant;
import org.ys.common.page.PageBean;
import org.ys.core.dao.CoreMenuMapper;
import org.ys.core.model.CoreMenu;
import org.ys.core.model.CoreMenuExample;
import org.ys.core.model.CoreMenuExample.Criteria;
import org.ys.core.service.CoreMenuService;

import java.util.ArrayList;
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
	public List<CoreMenu> listCoreMenusByUserId(Long coreUserId){
		if(null == coreUserId){
			return null;
		}
		return coreMenuMapper.listCoreMenusByUserId(coreUserId);
	}

	@Override
	public List<CoreMenu> findTree(Long coreUserId, String menuType) {
		List<CoreMenu> coreMenus = new ArrayList<>();
		List<CoreMenu> menus = findByCoreUserId(coreUserId);
		for (CoreMenu menu : menus) {
			if (menu.getParentCoreMenuId() == null || menu.getParentCoreMenuId() == 0) {
				menu.setLevel(0);
				if(!exists(coreMenus, menu)) {
					coreMenus.add(menu);
				}
			}
		}
		coreMenus.sort((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum()));
		findChildren(coreMenus, menus, menuType);
		return null;
	}

	private List<CoreMenu> findByCoreUserId(Long coreUserId) {
//		CoreMenuExample example = new CoreMenuExample();
//		if(StringUtils.isEmpty(userName) || "superAdmin".equals(userName)) {
//			return coreMenuMapper.selectByExample(example);
//		}
		return coreMenuMapper.listCoreMenusByUserId(coreUserId);
	}

	private void findChildren(List<CoreMenu> coreMenus, List<CoreMenu> menus, String menuType) {
		for (CoreMenu coreMenu : coreMenus) {
			List<CoreMenu> children = new ArrayList<>();
			for (CoreMenu menu : menus) {
				if(menuType == CoreMenuContant.MENU_TYPE_MENU && menu.getMenuType() == CoreMenuContant.MENU_TYPE_BUTTON) {
					// 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
					continue ;
				}
				if (coreMenu.getCoreMenuId() != null && coreMenu.getCoreMenuId().equals(menu.getParentCoreMenuId())) {
					menu.setParentMenuName(coreMenu.getMenuName());
					menu.setLevel(coreMenu.getLevel() + 1);
					if(!exists(children, menu)) {
						children.add(menu);
					}
				}
			}
			coreMenu.setChildren(children);
			children.sort((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum()));
			findChildren(children, menus, menuType);
		}
	}

	private boolean exists(List<CoreMenu> sysMenus, CoreMenu sysMenu) {
		boolean exist = false;
		for(CoreMenu menu:sysMenus) {
			if(menu.getCoreMenuId().equals(sysMenu.getCoreMenuId())) {
				exist = true;
			}
		}
		return exist;
	}
}
