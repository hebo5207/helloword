package com.study.permission.dao;

import com.study.permission.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    /**
     * 获得所有的部门信息
     * @return List<SysDept> 所有的部门信息
     */
    List<SysDept> getAllDept();

    /**
     * 获得当前级别的所有子节点
     * @param level  需要查询的级别
     * @return List<SysDept> 当前级别下面的所有子部门
     */
    List<SysDept> getChildDept(@Param("level") String level);


    /**
     * 批量更新子部门
     * @param sysDepts
     */
    void updateBatchChildDept(List<SysDept> sysDepts);

    /**
     * 查找部门信息的条数
     * @param parentId  父ID
     * @param name      部门名称
     * @param deptId    部门ID
     * @return
     */
    int selectCountByOldDept(@Param("parentId") Integer parentId,@Param("name") String name,@Param("deptId") Integer deptId);

}