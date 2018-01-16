package com.study.permission.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.study.permission.dao.SysDeptMapper;
import com.study.permission.dto.DeptLevelDto;
import com.study.permission.model.SysDept;
import com.study.permission.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 封装树形结构的Service
 * 何勇杰
 */
@Service
public class SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 获得部门的树形结构
     *
     * @return List<DeptLevelDto>
     */
    public List<DeptLevelDto> getDeptTree() {
        List<SysDept> sysDepts = sysDeptMapper.getAllDept();
        List<DeptLevelDto> deptLevelDtos = Lists.newArrayList();
        for (SysDept sysDept : sysDepts) {
            DeptLevelDto deptLevelDto = DeptLevelDto.adapt(sysDept);
            deptLevelDtos.add(deptLevelDto);
        }
        return deptListToTree(deptLevelDtos);
    }

    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelDtos) {
        if (CollectionUtils.isEmpty(deptLevelDtos)) {
            return Lists.newArrayList();
        }
        // Multimap值可重复 取出来的值是一个Collection
        Multimap<String, DeptLevelDto> deptLevelMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootDeptLevels = Lists.newArrayList();
        // 查找出第一级的所有部门
        for (DeptLevelDto deptLevelDto : deptLevelDtos) {
            deptLevelMap.put(deptLevelDto.getLevel(), deptLevelDto);
            if (LevelUtil.ROOT.equals(deptLevelDto)) {
                rootDeptLevels.add(deptLevelDto);
            }
        }
        // 从小到大排序
        Collections.sort(rootDeptLevels, comparator);
        transformDeptTree(rootDeptLevels,LevelUtil.ROOT,deptLevelMap);
        return rootDeptLevels;

    }

    /**
     * 递归生成树
     * @param deptLevelList  需要生成树的部门根节点集合
     * @param level          root节点的Level
     * @param levelDeptMap   Multimap<String, DeptLevelDto>  需要生成树的部门集合  Map<String,ArrayList<DeptLevelDto>>
     */
    public void transformDeptTree(List<DeptLevelDto> deptLevelList, String level, Multimap<String, DeptLevelDto> levelDeptMap) {
        // 遍历root节点
        for (int i = 0; i < deptLevelList.size(); i++) {
            DeptLevelDto deptLevelDto = deptLevelList.get(i);
            //下一级部门层级 = 当前节点层级 + 当前节点ID
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 处理下一个部门层级
            List<DeptLevelDto> nextDeptLevelDto = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(nextDeptLevelDto)) {
                // 排序
                // 从小到大排序
                Collections.sort(nextDeptLevelDto,comparator);
                deptLevelDto.setDeptList(nextDeptLevelDto);
                // 递归调用transformDeptTree 最终将树结构完成
                transformDeptTree(nextDeptLevelDto,nextLevel,levelDeptMap);
            }
        }
    }

    /**
     * 部门排序
     */
    public Comparator comparator = new Comparator<DeptLevelDto>() {
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
