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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 菜单服务
 */
@Service
@Slf4j
public class SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;


    public void delete( Integer id){
        if(checkNameIsExits("",id)){
            throw  new ParamException("当前菜单下有子菜单，请先删除子菜单");
        }
        sysMenuMapper.deleteByPrimaryKey(id);
    }



    /**
     * 添加菜单
     * 1.检查参数
     * 2.检查是否同名
     * 3.设置Level operator信息
     * 4.添加
     * @param sysMenuParam SysMenuParam
     */
    public void add(SysMenuParam sysMenuParam){
        BeanValidator.check(sysMenuParam);
        SysMenu sysMenu = SysMenuParam.sysMenuParamToSysMenu(sysMenuParam);
        if(sysMenu.getParentId() == 0 ){
            sysMenu.setLevel("0");
        }else{
            SysMenu sysMenuParent = sysMenuMapper.selectByPrimaryKey(sysMenu.getParentId());
            sysMenu.setLevel(LevelUtil.calculateLevel(sysMenuParent.getLevel(),sysMenuParent.getId()));
        }
        if(checkNameIsExits(sysMenu.getName(),sysMenu.getParentId())){
            throw new ParamException("当前菜单下已经有叫" + sysMenu.getName() + "名称的菜单了");
        }
        sysMenuMapper.insertSelective(sysMenu);
        updateMenuUrl(sysMenu.getLevel(),sysMenu.getName());
    }

    /**
     *更新URL   给URL后面加上？id=菜单的ID
     * 用于界面展示
     */
    public void updateMenuUrl(String level,String name){
        SysMenu sysMenu = sysMenuMapper.getSysDeptByLevelAndName(level,name);

      if(StringUtils.isNotBlank(sysMenu.getUrl())){
          sysMenu.setUrl(sysMenu.getUrl() + "?id=" + sysMenu.getId());
          sysMenuMapper.updateByPrimaryKeySelective(sysMenu);
      }

    }

    /**
     * 修改菜单信息
     * 1.检查参数
     * 2.更改菜单
     * @param sysMenuParam SysMenuParam
     */
    public void update(SysMenuParam sysMenuParam){
        BeanValidator.check(sysMenuParam);
        SysMenu oldSysMenu = sysMenuMapper.selectByPrimaryKey(sysMenuParam.getId());

        Preconditions.checkNotNull(oldSysMenu, "待更新的菜单不存在");
        SysMenu newSysMenu = SysMenuParam.sysMenuParamToSysMenu(sysMenuParam);
        newSysMenu.setId(sysMenuParam.getId());
        // 如果有URL需要增加上id=这样的值，方便界面展示导航菜单，这是跳转界面
        if(StringUtils.isNotBlank(newSysMenu.getUrl())){
            if(newSysMenu.getUrl().indexOf("?id=") == -1){
                newSysMenu.setUrl(newSysMenu.getUrl()  + "?id=" + newSysMenu.getId());
            }
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
        return sysMenuMapper.checkCountByParam(name,parentId) >0 ;
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
        // 按照seq从小到大排序
        Collections.sort(rootSysMenu,comparator);
        // 2. 根据root节点使用递归函数完成树结构的封装
        return createTree(rootSysMenu, multimap);
    }

    /**
     * 处理第二级数据
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
                    // 按照seq从小到大排序
                    Collections.sort(sysMenuDtos,comparator);
                    sysMenuDto.setChildren(sysMenuDtos);
                }
            }
        }
        return rootSysMenu;
    }

    /**
     * Menu方式
     */
    public Comparator comparator = new Comparator<SysMenuDto>() {
        public int compare(SysMenuDto o1, SysMenuDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

}
