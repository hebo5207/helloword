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

    String getMenuStr(@Param("id") Integer id);

    int checkNameIsExits(@Param("name") String name,@Param("parentId") Integer parentId);
}