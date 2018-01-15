package com.study.permission.controller;


import com.study.permission.common.exception.ParamException;
import com.study.permission.common.model.JsonData;
import com.study.permission.param.TestVo;
import com.study.permission.util.BeanValidator;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
        throw new RuntimeException("ada");
//        return new JsonData(true);
    }

    @RequestMapping("/validator.json")
    @ResponseBody
    public JsonData validator(TestVo testvo) throws ParamException{
        BeanValidator.check(testvo);
        return JsonData.success("test success");
    }
}