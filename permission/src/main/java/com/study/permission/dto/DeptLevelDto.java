package com.study.permission.dto;

import com.google.common.collect.Lists;
import com.study.permission.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept{

    private List<DeptLevelDto> deptList = Lists.newArrayList();

    /**
     * 将Dept对象复制到DeptLevelDto中
     * @param dept    部门对象
     * @return  DeptLevelDto
     */
    public static DeptLevelDto adapt(SysDept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
