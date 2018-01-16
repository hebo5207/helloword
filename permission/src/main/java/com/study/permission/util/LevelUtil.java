package com.study.permission.util;


import org.apache.commons.lang3.StringUtils;

/**
 * 树形级别工具类
 */
public class LevelUtil {

    /**
     * 每一个级别之间的分隔符
     */
    public final static String SEPARATOR = ".";

    /**
     * 如果没有上级菜单那么级别就是0
     */
    public final static String ROOT = "0";

    // 0
    // 0.1
    // 0.1.2
    // 0.1.3
    // 0.4
    // 如果没有父节点那么就是默认的root  根节点
    // 如果有父节点的话，那么当前部门层级就是父节点的层级加上父节点的ID
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
