package com.longtian.json.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * 根据Json的值的类型确定字段类型的策略
 *
 * @author chenll
 * @date 2018/7/12 20:13
 * @since V1.0
 */
public class DefaultFieldTypeStrategy extends FieldTypeStrategy {
    private static final String POINT = ".";

    @Override
    public Class<?> getPrimitiveType(JsonElement element) {
        Class<?> clazz = Object.class;
        JsonPrimitive jp = element.getAsJsonPrimitive();
        if (jp.isNumber()) {
            String num = jp.getAsString();
            if (num.contains(POINT)) {
                try {
                    Float.parseFloat(num);
                    clazz = Float.class;
                } catch (NumberFormatException e) {
                    clazz = Double.class;
                }
            } else {
                try {
                    Integer.parseInt(num);
                    clazz = Integer.class;
                } catch (NumberFormatException e) {
                    clazz = Long.class;
                }
            }
        } else if (jp.isBoolean()) {
            clazz = Boolean.class;
        } else if (jp.isString()) {
            clazz = String.class;
        }
        return clazz;
    }
}
