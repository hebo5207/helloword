package com.study.permission.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/login")
public class LoginController {
    @RequestMapping("/toIndexPage.json")
    public ModelAndView toIndexPage(){
        return new ModelAndView("index");
    }
}
