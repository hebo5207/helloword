package com.study.permission.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class JsonData {

    /**
     * 请求成功还是失败
     */
    private boolean ret;

    /**
     * 成功或者失败消息
     */
    private String msg;

    /**
     * 请求成功后的响应数据
     */
    private Object data;

    public JsonData(boolean ret){
        this.ret = ret;
    }

    /**
     * 数据请求成功
     * @param obj   处理后的数据
     * @param message   消息
     * @return   com.study.permission.common.model.JsonData
     */
    public static JsonData success(Object obj,String message){
        JsonData jsonData = new JsonData(true);
        jsonData.setData(obj);
        jsonData.setMsg(message);
        return jsonData;
    }

    /**
     * 数据请求成功  多用于查询
     * @param obj   请求的结果
     * @return  com.study.permission.common.model.JsonData
     */
    public static JsonData success(Object obj){
        JsonData jsonData = new JsonData(true);
        jsonData.setData(obj);
        return jsonData;
    }

    /**
     * 请求成功，多用于添加和修改，删除
     * @return
     */
    public static JsonData success() {
        return new JsonData(true);
    }

    /**
     * 请求失败
     * @param message  失败信息
     * @return  com.study.permission.common.model.JsonData
     */
    public static JsonData fail(String message){
        JsonData jsonData = new JsonData(false);
        jsonData.setMsg(message);
        return jsonData;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("ret",ret);
        map.put("msg",msg);
        map.put("data",data);
        return map;
    }
}
