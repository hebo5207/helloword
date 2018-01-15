package com.study.permission.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获得全局的ApplicationContext
 */
public class ApplicationContextHelper implements ApplicationContextAware{
    private  static ApplicationContext applicationContext;

    /**
     * 程序初始化的时候就创建ApplicationContext
     * @param context
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 根据Class获得对象，多用于Mapper层
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    /**
     * 获得具体的类实例 多用于servlet层
     * @param name    唯一ID
     * @param clazz
     * @param <T>
     * @return
     */
    public static<T> T getBean(String name,Class<T> clazz){
        return applicationContext.getBean(name,clazz);
    }

}

