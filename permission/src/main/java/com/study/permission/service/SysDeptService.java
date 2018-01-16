package com.study.permission.service;

import com.study.permission.common.exception.ParamException;
import com.study.permission.dao.SysDeptMapper;
import com.study.permission.model.SysDept;
import com.study.permission.param.SysDeptParam;
import com.study.permission.util.BeanValidator;
import com.study.permission.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
/**
 * 部门接口服务
 * 何勇杰
 */
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 添加部门信息
     * 1. 检查参数是否合理
     * 2. 检查当前父节点下有没有同名的部门
     * 3. 添加部门信息
     * @param sysDeptParam
     */
    public void save(SysDeptParam sysDeptParam) {
        // 检查参数是否合理
        BeanValidator.check(sysDeptParam);
        // 检查当前部门的名称在当前父节点下是否存在
        if (checkExits(sysDeptParam.getParentId(), sysDeptParam.getName(), sysDeptParam.getId())) {
            throw new ParamException("当前部门名称在当前父节点下已经存在了");
        }
        SysDept sysDept = SysDept.builder()
                .name(sysDeptParam.getName())
                .remark(sysDeptParam.getRemark())
                .seq(sysDeptParam.getSeq())
                .parentId(sysDeptParam.getParentId()).build();
        sysDept.setOperateIp("127.0.0.1");
        sysDept.setOperator("admin");
        sysDept.setOperateTime(new Date());
        sysDept.setLevel(LevelUtil.calculateLevel(this.getLevel(sysDeptParam.getParentId()), sysDeptParam.getParentId()));

        sysDeptMapper.insertSelective(sysDept);
    }

    /**
     * 修改部门信息
     * 1. 检查参数是否合理
     * 2. 检查修改后的信息和以前的信息是否相同，如果相同就不更新
     * 3. 更新子部门
     * @param sysDeptParam 修改后的部门信息
     */
    public void update(SysDeptParam sysDeptParam) {
        //1. 检查参数是否合理
        BeanValidator.check(sysDeptParam);
        //2. 检查修改后的信息和以前的信息是否相同，如果相同就不更新
        if (checkExits(sysDeptParam.getParentId(), sysDeptParam.getName(), sysDeptParam.getId())) {
            throw new ParamException("当前部门名称在当前父节点下已经存在了");
        }
        SysDept oldSysDept = sysDeptMapper.selectByPrimaryKey(sysDeptParam.getId());
        SysDept newSysDept = SysDept.builder()
                .id(sysDeptParam.getId())
                .name(sysDeptParam.getName())
                .remark(sysDeptParam.getRemark())
                .seq(sysDeptParam.getSeq())
                .parentId(sysDeptParam.getParentId()).build();
        // 3.更新子部门
        updateChilds(newSysDept,oldSysDept);
        // 4.更新部门信息
        sysDeptMapper.updateByPrimaryKeySelective(newSysDept);
    }

    /**
     * 更新子部门
     * 1. 需要检查是否需要更新子部门
     *    更新后的部门Level和更新后的Level不同就需要更新
     *
     * @param newSysDept  更新后的部门
     * @param oldSysDept  更新前的部门
     */
    private void updateChilds(SysDept newSysDept,SysDept oldSysDept){
        String newLevel = newSysDept.getLevel();
        String oldLevel = oldSysDept.getLevel();
        // 1.需要检查是否需要更新子部门
        if(!newLevel.equals(oldLevel)){
            List<SysDept> newChildSysDept = sysDeptMapper.getChildDept(oldSysDept.getLevel());
            if(CollectionUtils.isNotEmpty(newChildSysDept)){
                for(SysDept sysDept : newChildSysDept){
                    String oldChildLevel = sysDept.getLevel();
                    String newChildLevel =newLevel +  oldChildLevel.substring(oldLevel.length());
                    log.info("旧的子部门Level===>>>" + oldChildLevel);
                    log.info("新的子部门Level===>>>" + newChildLevel);
                    sysDept.setLevel(newChildLevel);
                }
                // 批量更新
                sysDeptMapper.updateBatchChildDept(newChildSysDept);
            }
        }
    }

    /**
     * 检查当前部门名称在当前节点下是否已经存在
     *
     * @param parentId 父节点
     * @param name     要保存的部门名称
     * @param deptId   需要保存的部门ID
     * @return boolean   当前部门在父节点下是否存在
     */
    public boolean checkExits(Integer parentId, String name, Integer deptId) {
        return sysDeptMapper.selectCountByOldDept(parentId,name,deptId) > 0 ;
    }

    /**
     * 根据ID获得当前部门的级别
     *
     * @param deptId
     * @return
     */
    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();
    }


}
