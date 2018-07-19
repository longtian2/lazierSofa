package com.longtian.json.deserialize;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * 根据字段类型取默认值的Json字段取值策略
 *
 * @author chenll
 * @date 2018/7/18 14:20
 * @since V1.0
 */
public class DefaultJsonFieldValueStrategy extends JsonFieldValueStrategy {
    @Override
    public Object getValue(String doc, Field field) {
        String fieldType = field.getType().getSimpleName();
        if (fieldType.equals(String.class.getSimpleName())) {
            return new String("");
        } else if (fieldType.equals(Long.class.getSimpleName())) {
            return new Long(1);
        } else if (fieldType.equals(Integer.class.getSimpleName())) {
            return new Integer(1);
        } else if (fieldType.equals(Double.class.getSimpleName())) {
            return new Double(1.1);
        } else if (fieldType.equals(Float.class.getSimpleName())) {
            return new Float(1.1);
        } else if (fieldType.equals(Short.class.getSimpleName())) {
            return new Short("1.1");
        } else if (fieldType.equals(BigDecimal.class.getSimpleName())) {
            return new BigDecimal("1.1");
        } else if (fieldType.equals(Boolean.class.getSimpleName())) {
            return Boolean.FALSE;
        }
        return new Object();
    }
}
