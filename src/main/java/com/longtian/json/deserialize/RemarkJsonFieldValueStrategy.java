package com.longtian.json.deserialize;

import java.lang.reflect.Field;

/**
 * 根据备注、字段类型的Json字段取值策略
 *
 * @author chenll
 * @date 2018/7/18 14:20
 * @since V1.0
 */
public class RemarkJsonFieldValueStrategy extends JsonFieldValueStrategy {
    @Override
    public Object getValue(String doc, Field field) {
        return doc + String.format(" 【%s】", field.getType().getSimpleName());
    }
}
