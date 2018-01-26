package com.study.permission.service.sys;

/**
 * 树结构封装类
 */
public class SysZtreeService<T>{

    private Class clazz ;

    public SysZtreeService(T t){
        this.clazz = t.getClass();
    }


    /**
     * 根据查询出来的所有对象封装成属性结构
     */
    public static <T> void getKey(){

    }




    public static void main(String[] args) {
        SysZtreeService.getKey();
    }



}
