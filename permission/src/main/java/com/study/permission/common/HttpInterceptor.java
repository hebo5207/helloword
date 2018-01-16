package com.study.permission.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    /**
     * 请求之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("请求之前");
        return true;
    }

    /**
     * 请求成功之后执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("请求成功后");
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 不管成功还是失败都会执行
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("请求执行后");
        super.afterCompletion(request, response, handler, ex);
    }
}
