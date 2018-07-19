package com.longtian.json.serialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Java类的编写器
 *
 * @author chenll
 * @date 2018/7/12 20:16
 * @since V1.0
 */
public class ClassBeanWriter {
    /**
     * 回车换行
     */
    private static final String ENTER_SEPARATOR = "\n";

    /**
     * tab 缩进
     */
    private static final String TAB_SEPARATOR = "\t";

    public static Map<String, String> generateClass(List<JsonClassDefinitionParser.ClassDefinition> classDefinitions) {
        Map<String, String> classContentMap = new HashMap<>();
        List<String> customClassNames = new ArrayList<String>();

        Iterator<JsonClassDefinitionParser.ClassDefinition> iterator = classDefinitions.iterator();
        while (iterator.hasNext()) {
            StringBuilder classBody = new StringBuilder();
            classBody.append(ENTER_SEPARATOR);
            JsonClassDefinitionParser.ClassDefinition classDefinition = iterator.next();
            String className = Objects.nonNull(classDefinition.getAliasName())
                    ? classDefinition.getAliasName()
                    : classDefinition.getClassName();
            className = firstUpperCase(className);
            if (customClassNames.contains(className)) {
                continue;
            }

            customClassNames.add(className);
            classBody.append(writeLine(1, String.format("public class %s { ", className)));

            StringBuilder getterAndSetter = new StringBuilder();
            for (JsonClassDefinitionParser.FiledDefinition fieldDefinition : classDefinition.getFields()) {
                addField(classBody, fieldDefinition);
                generateGetterAndSetter(getterAndSetter, fieldDefinition);
            }

            iterator.remove();
            classBody.append(getterAndSetter.toString());
            classBody.append(writeLine(1, "}"));
            classBody.append(ENTER_SEPARATOR);
            classContentMap.put(className, classBody.toString());
        }

        return classContentMap;
    }

    private static void addField(StringBuilder fieldContent,
            JsonClassDefinitionParser.FiledDefinition filedDefinition) {
        fieldContent.append(ENTER_SEPARATOR);
        String remark = filedDefinition.getRemark();
        if (Objects.nonNull(remark) && remark.length() > 0) {
            fieldContent.append(writeLine(2, "/**"));
            fieldContent.append(writeLine(2, " * " + remark.replace(getFieldType(filedDefinition), "").trim()));
            fieldContent.append(writeLine(2, " */"));
        }
        fieldContent.append(writeLine(2,
                String.format("private %s %s;", getFieldType(filedDefinition), filedDefinition.getFiledName())));
    }

    private static void generateGetterAndSetter(StringBuilder fieldGetterAndSetter,
            JsonClassDefinitionParser.FiledDefinition filedDefinition) {
        fieldGetterAndSetter.append(ENTER_SEPARATOR);
        fieldGetterAndSetter.append(writeLine(2, String.format("public %s get%s() {", getFieldType(filedDefinition),
                firstUpperCase(filedDefinition.getFiledName()))));
        fieldGetterAndSetter.append(writeLine(3, String.format("return %s;", filedDefinition.getFiledName())));
        fieldGetterAndSetter.append(writeLine(2, "}"));

        fieldGetterAndSetter.append(ENTER_SEPARATOR);
        fieldGetterAndSetter.append(
                writeLine(2, String.format("public void set%s(%s %s) {", firstUpperCase(filedDefinition.getFiledName()),
                        getFieldType(filedDefinition), filedDefinition.getFiledName())));
        fieldGetterAndSetter.append(writeLine(3,
                String.format("this.%s = %s;", filedDefinition.getFiledName(), filedDefinition.getFiledName())));
        fieldGetterAndSetter.append(writeLine(2, "}"));
    }

    private static String writeLine(int tabNum, String content) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < tabNum; i++) {
            line.append(TAB_SEPARATOR);
        }
        line.append(content);
        line.append(ENTER_SEPARATOR);
        return line.toString();
    }

    private static String getFieldType(JsonClassDefinitionParser.FiledDefinition filedDefinition) {
        String classType = Objects.nonNull(filedDefinition.getAliasName())
                ? filedDefinition.getAliasName()
                : filedDefinition.getFiledName();
        classType = firstUpperCase(classType);
        if (filedDefinition.getClassType().equals(Object.class)) {
            return classType;
        }
        if (filedDefinition.getClassType().equals(List.class)) {
            return String.format("List<%s>", classType);
        }
        return filedDefinition.getClassType().getSimpleName();
    }

    public static String firstUpperCase(String key) {
        return key.substring(0, 1).toUpperCase() + key.substring(1);
    }
}
