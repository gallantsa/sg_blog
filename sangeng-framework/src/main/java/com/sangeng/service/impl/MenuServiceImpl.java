package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Menu;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.service.MenuService;
import com.sangeng.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-09-23 16:49:30
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 如果是管理员, 返回所有的权限
        if(SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        // 否则返回去具有的权限

        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        // 判断是否是管理员
        if(SecurityUtils.isAdmin()) {
            //如果是 返回所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            // 否则 获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        // 构建tree
        // 先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menu.getMenuName()), Menu::getMenuName, menu.getMenuName());
        wrapper.eq(StringUtils.hasText(menu.getStatus()), Menu::getStatus, menu.getStatus());
        wrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);

        List<Menu> menus = list(wrapper);
        return menus;
    }

    @Override
    public ResponseResult edit(Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        Menu menu = getById(id);

        // 判断是否有子菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, id);
        if (count(wrapper) != 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "存在子菜单不允许删除");
        }

        // 如果没有子菜单则直接删除
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}

