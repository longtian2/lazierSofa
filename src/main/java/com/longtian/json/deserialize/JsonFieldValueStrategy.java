package com.longtian.json.deserialize;

import java.lang.reflect.Field;

/**
 * Json字段取值策略
 *
 * @author chenll
 * @date 2018/7/18 14:14
 * @since V1.0
 */
public abstract class JsonFieldValueStrategy {
    /**
     * 获取 Json字段的值
     * 
     * @param doc 字段注释
     * @param field 字段
     * @return
     */
    public abstract Object getValue(String doc, Field field);
}
