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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Resource
    SysMenuService sysMenuService;

    @RequestMapping("/toMenuPage.page")
    public ModelAndView toMenuPage(Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id",id);
        modelAndView.setViewName("sys/menu/menu");
        return modelAndView;
    }

    @RequestMapping("/toMenuAddPage.page")
    public ModelAndView toMenuAddPage(@Param("type") String type,@Param("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("sys/menu/menu-add");
        modelAndView.addObject("menuId",id);
        modelAndView.addObject("type",type);
        return modelAndView;
    }

    @RequestMapping("/select.json")
    @ResponseBody
    public JsonData select(@Param("id") Integer id){
        return JsonData.success(sysMenuService.getSysMenuByKey(id));
    }

    @RequestMapping("/navStr.json")
    @ResponseBody
    public JsonData  navStr(@Param("id") Integer id){
        String str = sysMenuService.getMenuStr(id);
        String strs [] =str.split("@");
        return JsonData.success(strs);
    }

    @RequestMapping("/toIconPage.page")
    public ModelAndView toIconPage(){
        return new ModelAndView("sys/menu/menu-icon");
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(SysMenuParam sysMenuParam){
        sysMenuService.update(sysMenuParam);
        return JsonData.success();
    }

    @RequestMapping("/add.json")
    @ResponseBody
    public JsonData add(SysMenuParam sysMenuParam){
        sysMenuService.add(sysMenuParam);
        return JsonData.success();
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@Param("id") Integer id){
        sysMenuService.delete(id);
        return JsonData.success();
    }


}
