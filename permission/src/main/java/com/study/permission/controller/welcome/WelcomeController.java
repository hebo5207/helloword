package com.study.permission.controller.welcome;

import com.study.permission.common.model.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/welcome")
public class WelcomeController {
    @RequestMapping("/toWelcomePage.page")
    public ModelAndView toWelcomePage(){
        return new ModelAndView("welcome");
    }

}
