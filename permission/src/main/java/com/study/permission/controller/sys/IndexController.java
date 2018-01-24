package com.study.permission.controller.sys;

import com.study.permission.common.model.JsonData;
import com.study.permission.service.sys.SysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 首页初始化Controller
 * 何勇杰
 */
@Controller
@RequestMapping("/sys/index")
public class IndexController {

    @Resource
    private SysMenuService menuService;

    @RequestMapping("/initMenuTree")
    @ResponseBody
    public JsonData initMenuTree(){
        return JsonData.success(menuService.getAllSysMenu());
    }
}
