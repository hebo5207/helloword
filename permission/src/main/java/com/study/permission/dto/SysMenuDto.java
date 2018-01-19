package com.study.permission.dto;


import com.google.common.collect.Lists;
import com.study.permission.model.SysMenu;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class SysMenuDto extends SysMenu {
    private List<SysMenuDto> sysMenuDtos = Lists.newArrayList();

    /**
     * 将SysMenu 的值copy到SysMenuDto
     * @param sysMenu  SysMenu
     * @return SysMenuDto
     */
    public static SysMenuDto adapt(SysMenu sysMenu){
        SysMenuDto sysMenuDto = new SysMenuDto();
        BeanUtils.copyProperties(sysMenu,sysMenuDto);
        return sysMenuDto;
    }


}
