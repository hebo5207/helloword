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
     *获得所有部门信息
     * @return  List<SysDept>
     */
    List<SysDept> getAllDept();

    /**
     *获得所有的自部门
     * @param level  需要查询的子部门属于杀个级别
     * @return    List<SysDept>
     */
    List<SysDept> getChildDeptList(@Param("level") String level);

    /**
     *批量更新子部门
     * @param sysDepts  List<SysDept> 需要更新的子部门
     */
    void batchUpdateChild(@Param("sysDepts") List<SysDept> sysDepts);

    /**
     *查询更新后的父级菜单下是否有相同名称的部门
     * @param parentId  父级菜单ID
     * @param deptName  更新后的部门名称
     * @return
     */
    public int checkExist(@Param("parentId") Integer parentId,@Param("deptName") String deptName);

    /**
     *根据id查询级别信息
     * @param id   要查询的ID
     * @return String  级别信息
     */
    public String getParentLevel(@Param("id") Integer id);


}