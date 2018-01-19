package com.study.permission.service.menu;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.study.permission.dao.SysMenuMapper;
import com.study.permission.dto.SysMenuDto;
import com.study.permission.model.SysMenu;
import com.study.permission.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单服务
 */
@Service
@Slf4j
public class MenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    /**
     * 返回树结构
     *
     * @return
     */
    public List<SysMenuDto> getAllSysMenu() {
        List<SysMenu> sysMenus = sysMenuMapper.getAllMenu();
        List<SysMenuDto> sysMenuDtos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(sysMenus)) {
            for (SysMenu sysMenu : sysMenus) {
                SysMenuDto sysMenuDto = SysMenuDto.adapt(sysMenu);
                sysMenuDtos.add(sysMenuDto);
            }
        }
        return sysMenuDaoToTree(sysMenuDtos);
    }


    /**
     * 将List<SysMenuDto> 转换为树结构
     * 1. 找到root节点
     * 2. 根据root节点使用递归函数完成树结构的封装
     *
     * @param sysMenuDtos List<SysMenuDto>
     * @return 树结构的List<SysMenuDto>
     */
    private List<SysMenuDto> sysMenuDaoToTree(List<SysMenuDto> sysMenuDtos) {
        if (CollectionUtils.isEmpty(sysMenuDtos)) {
            log.warn("没有查询到菜单！");
            return Lists.newArrayList();
        }
        //1. 找到root节点
        Multimap<String,SysMenuDto> multimap = ArrayListMultimap.create();
        List<SysMenuDto> rootSysMenu = Lists.newArrayList();
        for (SysMenuDto sysMenuDto : sysMenuDtos) {
            multimap.put(sysMenuDto.getLevel(), sysMenuDto);
            if (LevelUtil.ROOT.equals(sysMenuDto.getLevel())) {
                rootSysMenu.add(sysMenuDto);
            }
        }
        // 2. 根据root节点使用递归函数完成树结构的封装
        return createTree(rootSysMenu, multimap);
    }

    /**
     * 递归生成树
     * @param rootSysMenu   第一级菜单
     * @param multimap     Multimap 所有菜单
     * @return
     */
    private List<SysMenuDto> createTree(List<SysMenuDto> rootSysMenu, Multimap multimap) {
        if (CollectionUtils.isNotEmpty(rootSysMenu)) {
            for (SysMenuDto sysMenuDto : rootSysMenu) {
                String nextLevel = LevelUtil.calculateLevel(sysMenuDto.getLevel(), sysMenuDto.getId());
                List<SysMenuDto> sysMenuDtos = (List<SysMenuDto>)multimap.get(nextLevel);
                if(CollectionUtils.isNotEmpty(sysMenuDtos)){
                    sysMenuDto.setSysMenuDtos(sysMenuDtos);
                }
            }
        }
        return rootSysMenu;
    }
}
