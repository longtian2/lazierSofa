package com.longtian.json.serialize;

import com.google.gson.JsonElement;

/**
 * 字段类型策略
 *
 * @author chenll
 * @date 2018/7/12 19:40
 * @since V1.0
 */
public abstract class FieldTypeStrategy {
    /**
     * 字段类型
     *
     * @param element
     * @return
     */
    public abstract Class<?> getPrimitiveType(JsonElement element);
}
