package com.study.permission.dao;

import com.study.permission.model.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    /**
     * 获得所有的菜单
     * @return
     */
    List<SysMenu> getAllMenu();

    /**
     * 获得导航信息
     * @param id
     * @return
     */
    String getMenuStr(@Param("id") Integer id);

    /**
     * 根据菜单名称或者菜单的父级ID查询子菜单个数
     * @param name   当前菜单名称
     * @param parentId  父级菜单ID
     * @return
     */
    int checkCountByParam(@Param("name") String name,@Param("parentId") Integer parentId);

    /**
     *根据Level查询菜单信息
     * @param Level String  菜单级别
     * @return SysMenu
     */
    public SysMenu getSysDeptByLevelAndName(@Param("level") String Level,@Param("name") String name);


}