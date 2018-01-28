package com.study.permission.service.sys;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.study.permission.dao.SysDeptMapper;
import com.study.permission.dto.SysDeptDto;
import com.study.permission.model.SysDept;
import com.study.permission.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 树结构封装类
 *1.部门树形封装
 *
 */
@Service
@Slf4j
public class SysZtreeService{

    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     *获得部门树
     * @return    List<SysDeptDto>
     */
    public List<SysDeptDto> getDeptTree(){
        List<SysDept> sysDeptList = sysDeptMapper.getAllDept();
        List<SysDeptDto> sysDeptDtoList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(sysDeptList)){
            for(SysDept sysDept : sysDeptList){
                SysDeptDto sysDeptDto = SysDeptDto.sysDeptToSysDeptDto(sysDept);
                sysDeptDtoList.add(sysDeptDto);
            }
        }
        return initTree(sysDeptDtoList);

    }

    /**
     * 根据传进来的部门树进行数据分离
     * 会主动处理一级部门
     * @param sysDeptDtos   List<SysDeptDto>
     * @return  处理过后的 List<SysDeptDto>
     */
    public List<SysDeptDto> initTree(List<SysDeptDto> sysDeptDtos){
        if(CollectionUtils.isEmpty(sysDeptDtos)){
            log.warn("没有查询到部门信息");
            return Lists.newArrayList();
        }

        List<SysDeptDto> rootList = Lists.newArrayList();
        Multimap multimap = ArrayListMultimap.create();
        for(SysDeptDto sysDeptDto : sysDeptDtos){
            multimap.put(sysDeptDto.getLevel(),sysDeptDto);
            if(LevelUtil.ROOT.equals(sysDeptDto.getLevel())){
                rootList.add(sysDeptDto);
            }
        }

        // 一级菜单排序
        Collections.sort(rootList,comparator);
        List<SysDeptDto> ts = whileHandleDeptData(rootList,multimap);
        return ts;
    }

    /**
     * 递归生成部门数据
     * @param rootList  第一级部门信息
     * @param multimap  分类信息
     * @return  List<SysDeptDto>处理后的数据
     */
    public List<SysDeptDto> whileHandleDeptData( List<SysDeptDto> rootList , Multimap multimap){
        if(CollectionUtils.isNotEmpty(rootList)){
            for(SysDeptDto sysDeptDto : rootList){
                String nextLevel = LevelUtil.calculateLevel(sysDeptDto.getLevel(),sysDeptDto.getId());
                List<SysDeptDto> sysDeptDtoList =   (List<SysDeptDto>)multimap.get(nextLevel);
                if(CollectionUtils.isNotEmpty(sysDeptDtoList)){
                    Collections.sort(sysDeptDtoList,comparator);
                    sysDeptDto.setChildren(sysDeptDtoList);
                    whileHandleDeptData(sysDeptDtoList,multimap);
                }
            }
        }
        return rootList;
    }


    /**
     *根据Seq排序
     */
    private Comparator comparator = new Comparator<SysDeptDto>() {
        public int compare(SysDeptDto o1, SysDeptDto o2) {
            return  o1.getSeq() - o2.getSeq();
        }
    };


}
