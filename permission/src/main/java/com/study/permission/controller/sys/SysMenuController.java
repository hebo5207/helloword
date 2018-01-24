package com.study.permission.controller.sys;

import com.study.permission.common.model.JsonData;
import com.study.permission.param.SysMenuParam;
import com.study.permission.service.sys.SysMenuService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Resource
    SysMenuService sysMenuService;

    @RequestMapping("/toMenuPage")
    public ModelAndView toMenuPage(Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id",id);
        modelAndView.setViewName("sys/menu/menu");
        return modelAndView;
    }

    @RequestMapping("/toMenuAddPage")
    public ModelAndView toMenuAddPage(@Param("type") String type,@Param("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("sys/menu/menu-add");
        modelAndView.addObject("menuId",id);
        modelAndView.addObject("type",type);
        return modelAndView;
    }

    @RequestMapping("/select")
    @ResponseBody
    public JsonData select(@Param("id") Integer id){
        return JsonData.success(sysMenuService.getSysMenuByKey(id));
    }

    @RequestMapping("/navStr")
    @ResponseBody
    public JsonData  navStr(@Param("id") Integer id){
        String str = sysMenuService.getMenuStr(id);
        String strs [] =str.split("@");
        return JsonData.success(strs);
    }

    @RequestMapping("/toIconPage")
    public ModelAndView toIconPage(){
        return new ModelAndView("sys/menu/menu-icon");
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonData update(SysMenuParam sysMenuParam){
        sysMenuService.update(sysMenuParam);
        return JsonData.success();
    }

}
