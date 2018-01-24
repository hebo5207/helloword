package com.study.permission.service.sys;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.study.permission.common.exception.ParamException;
import com.study.permission.dao.SysMenuMapper;
import com.study.permission.dto.SysMenuDto;
import com.study.permission.model.SysMenu;
import com.study.permission.param.SysMenuParam;
import com.study.permission.util.BeanValidator;
import com.study.permission.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单服务
 */
@Service
@Slf4j
public class SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    /**
     * 修改菜单信息,如果下面有子部门不能移动
     * 1.检查参数
     * 2.检查同一级是否重名
     * 3.更改菜单
     * @param sysMenuParam SysMenuParam
     */
    public void update(SysMenuParam sysMenuParam){
        BeanValidator.check(sysMenuParam);
        SysMenu oldSysMenu = sysMenuMapper.selectByPrimaryKey(sysMenuParam.getId());

        Preconditions.checkNotNull(oldSysMenu, "待更新的菜单不存在");
        SysMenu newSysMenu = SysMenuParam.sysMenuParamToSysMenu(sysMenuParam);
        if(checkNameIsExits(newSysMenu.getName(),newSysMenu.getParentId())){
            throw new ParamException("当前菜单下已经有叫" + newSysMenu.getName() + "名称的菜单了");
        }
        sysMenuMapper.updateByPrimaryKeySelective(newSysMenu);
    }

    /**
     * 检查同一级下名称是否存在
     * @param name   改过后的名称
     *  @param parentId  父菜单ID
     * @return
     */
    public boolean checkNameIsExits(String name,Integer parentId){
        return sysMenuMapper.checkNameIsExits(name,parentId) >0 ;
    }

    /**
     * 根据ID查询菜单信息
     * @param id
     * @return  SysMenu
     */
    public SysMenuParam getSysMenuByKey(Integer id){
        SysMenu sysMenu = sysMenuMapper.selectByPrimaryKey(id);
        SysMenuParam sysMenuParam = SysMenuParam.sysMenuToSysMenuParam(sysMenu);

        SysMenu sysMenuParent = sysMenuMapper.selectByPrimaryKey(sysMenu.getParentId());
        sysMenuParam.setParentStr("0");
        if(sysMenuParent != null){
            sysMenuParam.setParentStr(sysMenuParent.getName());
        }
        return sysMenuParam;
    }

    /**
     * 根据ID获得当前节点名称以及父节点名称中间用@隔开
     *
     * @param id Integer 需要查询的ID
     * @return
     */
    public String getMenuStr(@Param("id") Integer id) {
        return sysMenuMapper.getMenuStr(id) == null ? "" : sysMenuMapper.getMenuStr(id);
    }

    /**
     * 返回树结构   用于index界面初始化
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
        List<SysMenuDto> sysMenuDtoList = sysMenuDtos;
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
        Multimap<String, SysMenuDto> multimap = ArrayListMultimap.create();
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
     *
     * @param rootSysMenu 第一级菜单
     * @param multimap    Multimap 所有菜单
     * @return
     */
    private List<SysMenuDto> createTree(List<SysMenuDto> rootSysMenu, Multimap multimap) {
        if (CollectionUtils.isNotEmpty(rootSysMenu)) {
            for (SysMenuDto sysMenuDto : rootSysMenu) {
                String nextLevel = LevelUtil.calculateLevel(sysMenuDto.getLevel(), sysMenuDto.getId());
                List<SysMenuDto> sysMenuDtos = (List<SysMenuDto>) multimap.get(nextLevel);
                if (CollectionUtils.isNotEmpty(sysMenuDtos)) {
                    sysMenuDto.setChildren(sysMenuDtos);
                }
            }
        }
        return rootSysMenu;
    }


}
