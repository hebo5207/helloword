package com.study.permission.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.study.permission.common.exception.ParamException;
import com.sun.org.apache.xpath.internal.operations.String;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 实体类校验工具
 * 何勇杰
 */
@Slf4j
public class BeanValidator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 检验字段是否为空
     * @param t  需要检验的类
     * @param groups    需要检验的类对应的class  非必填项
     * @param <T>
     * @return   Map<String,String> 出错字段的出错信息，如果没有返回一个空的Map
     */
    public static <T> Map<String,String> validator(T t, Class... groups){
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> validatorCheckResult = validator.validate(t,groups);
        if(validatorCheckResult.isEmpty()){
            return Collections.emptyMap();
        }else{
            LinkedHashMap result = Maps.newLinkedHashMap();
            Iterator<ConstraintViolation<T>> iterator = validatorCheckResult.iterator();
            while(iterator.hasNext()){
                ConstraintViolation<T> cv = iterator.next();
                result.put(cv.getPropertyPath().toString(),cv.getMessage());
            }
            return result;
        }
    }

    /**
     * 检验集合
     * @param collection   需要检验的集合
     * @return  Map<String,String> 返回出错信息
     */
    public static  Map<String,String> validatorList(Collection<?> collection){
        // 检验是否为空
        try{
            Preconditions.checkNotNull(collection);
        }catch (NullPointerException e){
            e.printStackTrace();
            return Collections.emptyMap();
        }
        Map<String,String> result;
        Iterator iterator = collection.iterator();
        do{
            if(!iterator.hasNext()){
                return Collections.emptyMap();
            }else{
                Object obj = iterator.hasNext();
                result =  validator(obj,new Class[0]);
            }
        }while (result.isEmpty());
        return result;
    }

    /**
     * 检验一个对象 可以传入很多个Object
     * @param obj  必传项，需要检验的Object
     * @param params   可变参数，可传可不传
     * @return  返回检验结果
     */
    public static Map<String,String> validatorObject(Object obj,Object... params){
        if(params != null && params.length > 0){
            return validatorList(Lists.asList(obj,params));
        }else{
            return validator(obj,new Class[0]);
        }
    }

    /**
     * 检验参数，检验失败后抛出ParamException
     * @param obj  需要检验的Object
     */
    public static  void check(Object obj,Object... param) {
        Map<String,String> results = validatorObject(obj);
        if(MapUtils.isNotEmpty(results)){
            for(Map.Entry<String,String> entry:results.entrySet()){
                log.info(obj.getClass() + "-->{}-->{}",entry.getKey(),entry.getValue());
            }
            throw new ParamException("参数校验失败");
        }
    }
}
