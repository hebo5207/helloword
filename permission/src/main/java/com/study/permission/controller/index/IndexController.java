package com.study.permission.controller.index;

import com.study.permission.common.model.JsonData;
import com.study.permission.service.menu.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
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
    private MenuService menuService;

    @RequestMapping("/initMenuTree")
    @ResponseBody
    public JsonData initMenuTree(){
        return JsonData.success(menuService.getAllSysMenu());
    }
}
