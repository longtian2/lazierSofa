package com.longtian.json.serialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Json转换成Java定义的解析器
 *
 * @author chenll
 * @date 2018/7/12 19:29
 * @since V1.0
 */
public class JsonClassDefinitionParser {
    private final List<ClassDefinition> classDefinitions = new ArrayList<>();
    private FieldTypeStrategy fieldTypeStrategy = new DefaultFieldTypeStrategy();
    private Map<String, String> fieldAliasNameMap = new HashMap<>();

    public void parseClassDefinitions(String json, String className) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        parseObject(element, className);
        parseList(element, className);

    }

    private Class<?> getClassType(JsonElement element) {
        Class<?> clazz = null;

        if (element.isJsonNull()) {
            clazz = String.class;
        } else if (element.isJsonPrimitive()) {
            clazz = fieldTypeStrategy.getPrimitiveType(element);
        } else if (element.isJsonObject()) {
            clazz = Object.class;
        } else if (element.isJsonArray()) {
            clazz = List.class;
        }
        return clazz;
    }

    private void parseObject(JsonElement element, String className) {
        if (!element.isJsonObject()) {
            return;
        }
        convertToClassDefinition(element.getAsJsonObject(), className);
    }

    private void parseList(JsonElement element, String className) {
        if (!element.isJsonArray()) {
            return;
        }
        JsonArray jsonArray = element.getAsJsonArray();
        if (jsonArray.size() > 0) {
            convertToClassDefinition(jsonArray.get(0).getAsJsonObject(), className);
        }
    }

    private void convertToClassDefinition(JsonObject jsonBean, String className) {
        if (jsonBean == null) {
            return;
        }

        ClassDefinition classElement = new ClassDefinition();
        classElement.setClassName(className);
        if (Objects.nonNull(fieldAliasNameMap) && Objects.nonNull(fieldAliasNameMap.get(className))) {
            classElement.setAliasName(fieldAliasNameMap.get(className));
        }
        List<FiledDefinition> fields = new LinkedList<>();
        classElement.setFields(fields);
        classDefinitions.add(classElement);

        for (Map.Entry<String, JsonElement> entry : jsonBean.entrySet()) {
            String fieldName = entry.getKey();
            JsonElement fieldType = entry.getValue();

            FiledDefinition filedDefinition = new FiledDefinition();
            Class<?> type = getClassType(fieldType);
            filedDefinition.setClassType(type);
            filedDefinition.setFiledName(fieldName);
            if (fieldType.isJsonPrimitive()) {
                filedDefinition.setRemark(fieldType.getAsJsonPrimitive().getAsString());
            }
            if (Objects.nonNull(fieldAliasNameMap) && Objects.nonNull(fieldAliasNameMap.get(fieldName))) {
                filedDefinition.setAliasName(fieldAliasNameMap.get(fieldName));
            }
            fields.add(filedDefinition);

            parseObject(fieldType, fieldName);
            parseList(fieldType, fieldName);
        }

    }

    public void setFieldTypeStrategy(FieldTypeStrategy fieldTypeStrategy) {
        this.fieldTypeStrategy = fieldTypeStrategy;
    }

    public void setFieldAliasNameMap(Map<String, String> fieldAliasNameMap) {
        this.fieldAliasNameMap = fieldAliasNameMap;
    }

    public List<ClassDefinition> getClassDefinitions() {
        return classDefinitions;
    }

    public static class ClassDefinition {
        /**
         * 类名
         */
        private String className;

        /**
         * 别名
         */
        private String aliasName;

        /**
         * 字段集合
         */
        private List<FiledDefinition> fields;


        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getAliasName() {
            return aliasName;
        }

        public void setAliasName(String aliasName) {
            this.aliasName = aliasName;
        }

        public List<FiledDefinition> getFields() {
            return fields;
        }

        public void setFields(List<FiledDefinition> fields) {
            this.fields = fields;
        }
    }

    public static class FiledDefinition {
        private String filedName;

        private String aliasName;

        private Class classType;

        private String remark;

        public String getFiledName() {
            return filedName;
        }

        public void setFiledName(String filedName) {
            this.filedName = filedName;
        }

        public String getAliasName() {
            return aliasName;
        }

        public void setAliasName(String aliasName) {
            this.aliasName = aliasName;
        }

        public Class getClassType() {
            return classType;
        }

        public void setClassType(Class classType) {
            this.classType = classType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
