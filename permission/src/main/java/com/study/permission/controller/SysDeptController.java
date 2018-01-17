package com.study.permission.controller;

import com.study.permission.common.model.JsonData;
import com.study.permission.param.SysDeptParam;
import com.study.permission.service.SysDeptService;
import com.study.permission.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("/sys/dept")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/toDeptPage.page")
    public ModelAndView toDeptPage(){
        return new ModelAndView("dept");
    }

    /**
     * 添加部门信息
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(SysDeptParam sysDeptParam){
        sysDeptService.save(sysDeptParam);
        return JsonData.success();
    }

    /**
     * 修改部门信息
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(SysDeptParam sysDeptParam){
        sysDeptService.save(sysDeptParam);
        return JsonData.success();
    }

    /**
     * 展示树结构
     * @return
     */
    @RequestMapping("/findTree.json")
    @ResponseBody
    public JsonData findTree(){
       return JsonData.success(sysTreeService.getDeptTree()) ;
    }


}
