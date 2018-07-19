package com.longtian.json.serialize;

import java.math.BigDecimal;
import java.util.Objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * 根据Json备注确定字段类型的策略
 *
 * @author chenll
 * @date 2018/7/17 15:13
 * @since V1.0
 */
public class RemarkFieldTypeStrategy extends FieldTypeStrategy {

    @Override
    public Class<?> getPrimitiveType(JsonElement element) {
        Class<?> clazz = String.class;
        JsonPrimitive jp = element.getAsJsonPrimitive();
        String remark = jp.getAsString();
        if (Objects.nonNull(remark)) {
            if (remark.contains(String.class.getSimpleName())) {
                clazz = String.class;
            } else if (remark.contains(Boolean.class.getSimpleName())) {
                clazz = Boolean.class;
            } else if (remark.contains(BigDecimal.class.getSimpleName())) {
                clazz = BigDecimal.class;
            } else if (remark.contains(Integer.class.getSimpleName())) {
                clazz = Integer.class;
            } else if (remark.contains(Long.class.getSimpleName())) {
                clazz = Long.class;
            } else if (remark.contains(Double.class.getSimpleName())) {
                clazz = Double.class;
            } else if (remark.contains(Float.class.getSimpleName())) {
                clazz = Float.class;
            } else if (remark.contains(Short.class.getSimpleName())) {
                clazz = Short.class;
            }
        }
        return clazz;
    }
}
