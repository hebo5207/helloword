package com.study.permission.param;

import com.study.permission.model.SysMenu;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SysMenuParam {

    private Integer id;

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 父级菜单，为0的话就是一级菜单
     */
   @NotNull(message = "父节点不能为空，可以为0，表示第一级菜单")
   @Min(value = 0)
    private Integer parentId;

    /**
     * 父级菜单名称，用于界面展示
     */
    private String parentStr;

    /**
     * 菜单还是方法
     */
    private String type;

    /**
     * 菜单URL
     */

    @Size(max = 200,message = "URL不能超过200个字符")
    private String url;

    /**
     * 排序信息
     */
    @Min(value = 0,message = "seq只能是数字")
    private Integer seq;

    /**
     * 图标
     */
    @Size(max=10,message="图标不能超过10个字符")
    private String icon;

    /**
     * 备注
     */
    @Size(max = 200,message = "备注不能超过200个字")
    private String remark;

    /**
     * 将SysMenu COPY 成SysMenuParam对象
     * 注：不会copy的属性，需要自己设置
     * parentStr
     * typeStr
     *
     * @param sysMenu 将SysMenu
     * @return SysMenuParam
     */
    public static SysMenuParam sysMenuToSysMenuParam(SysMenu sysMenu) {

        SysMenuParam sysMenuParam =SysMenuParam.builder().icon(sysMenu.getIcon())
                .type(sysMenu.getType())
                .name(sysMenu.getName()).remark(sysMenu.getRemark()).url(sysMenu.getUrl())
                .seq(sysMenu.getSeq()).parentId(sysMenu.getParentId()).build();
        return sysMenuParam;
    }

    /**
     * 将 成SysMenuParam COPY 成SysMenu对象
     * @param sysMenuParam  SysMenuParam
     * @return SysMenu
     */
    public static SysMenu sysMenuParamToSysMenu(SysMenuParam sysMenuParam){
        SysMenu sysMenu = SysMenu.builder().name(sysMenuParam.getName())
                .parentId(sysMenuParam.getParentId())
                .url(sysMenuParam.getUrl())
                .seq(sysMenuParam.getSeq())
                .icon(sysMenuParam.getIcon())
                .remark(sysMenuParam.getRemark()).build();
        return sysMenu;
    }


}
