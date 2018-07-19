package com.longtian.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.longtian.json.deserialize.ClassJsonDefinitionParser;
import com.longtian.json.deserialize.DefaultJsonFieldValueStrategy;
import com.longtian.json.deserialize.JsonFieldValueStrategy;
import com.longtian.json.deserialize.RemarkJsonFieldValueStrategy;
import com.longtian.json.serialize.ClassBeanWriter;
import com.longtian.json.serialize.DefaultFieldTypeStrategy;
import com.longtian.json.serialize.FieldTypeStrategy;
import com.longtian.json.serialize.JsonClassDefinitionParser;
import com.longtian.json.serialize.RemarkFieldTypeStrategy;

/**
 * Json与 Class的转换器
 *
 * @author chenll
 * @date 2018/7/13 18:09
 * @since V1.0
 */
public class JsonAndClassConverter {
    private static final String DEFAULT_CHARSET = "utf-8";

    public static String readFile(File file) {
        Long size = file.length();
        byte[] content = new byte[size.intValue()];
        FileInputStream in = null;

        try {
            in = new FileInputStream(file);
            in.read(content);
            return new String(content, DEFAULT_CHARSET);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (Objects.nonNull(in)) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void writeFile(File file, String content) {
        BufferedWriter writer = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), DEFAULT_CHARSET));
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (Objects.nonNull(writer)) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("参数错误");
            return;
        }
        String operateType = args[0];
        if (OperateTypeEnum.CONVERT_CLASS.getCode().equals(operateType)) {
            convertClass(args);
        } else if (OperateTypeEnum.CONVERT_JSON.getCode().equals(operateType)) {
            convertJson(args);
        }
    }

    private static void convertJson(String[] array) {
        // 源文件
        String sourcePath = array[1];
        // 目标路径
        String targetPath = array[2];
        // 文件名
        String className = array[3];
        // 策略
        String strategy = null;
        if (array.length == 5) {
            strategy = array[4];
        }

        ClassJsonDefinitionParser classJsonDefinitionParser = new ClassJsonDefinitionParser();
        if (ConvertJsonStrategyEnum.REMARK.getCode().equals(strategy)) {
            classJsonDefinitionParser.setJsonFieldValueStrategy(ConvertJsonStrategyEnum.REMARK.getStrategy());
        } else {
            classJsonDefinitionParser.setJsonFieldValueStrategy(ConvertJsonStrategyEnum.DEFAULT.getStrategy());
        }
        writeFile(new File(targetPath + File.separatorChar + className + ".json"),
                classJsonDefinitionParser.parserJsonString(sourcePath, className));
    }

    public static void convertClass(String[] array) {
        // 源文件
        String sourceFile = array[1];
        // 目标路径
        String targetPath = array[2];
        // 文件名
        String fileName = array[3];
        // 策略
        String strategy = null;
        // 别名
        String aliasJson = null;

        if (array.length == 5) {
            strategy = array[4];
        } else if (array.length == 6) {
            strategy = array[4];
            aliasJson = array[5];
        }

        String jsonContent = readFile(new File(sourceFile));

        JsonClassDefinitionParser classDefinitionParser = new JsonClassDefinitionParser();
        classDefinitionParser.setFieldAliasNameMap(JSON.parseObject(aliasJson, Map.class));
        if (ConvertClassStrategyEnum.REMARK.getCode().equals(strategy)) {
            classDefinitionParser.setFieldTypeStrategy(ConvertClassStrategyEnum.REMARK.getStrategy());
        } else {
            classDefinitionParser.setFieldTypeStrategy(ConvertClassStrategyEnum.DEFAULT.getStrategy());
        }

        classDefinitionParser.parseClassDefinitions(jsonContent, fileName);
        Map<String, String> classContentMap =
                ClassBeanWriter.generateClass(classDefinitionParser.getClassDefinitions());
        if (Objects.nonNull(classContentMap)) {
            for (Map.Entry<String, String> classContent : classContentMap.entrySet()) {
                writeFile(new File(targetPath + File.separator + classContent.getKey() + ".java"),
                        classContent.getValue());
            }
        }
    }

    public enum OperateTypeEnum {
        CONVERT_CLASS("class", "Json 转换 Class"), CONVERT_JSON("json", "Class 转换 Json");
        private String code;
        private String desc;

        OperateTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum ConvertClassStrategyEnum {
        DEFAULT("d", new DefaultFieldTypeStrategy()), REMARK("r", new RemarkFieldTypeStrategy());
        private String code;
        private FieldTypeStrategy strategy;

        ConvertClassStrategyEnum(String code, FieldTypeStrategy strategy) {
            this.code = code;
            this.strategy = strategy;
        }

        public String getCode() {
            return code;
        }

        public FieldTypeStrategy getStrategy() {
            return strategy;
        }
    }

    public enum ConvertJsonStrategyEnum {
        DEFAULT("d", new DefaultJsonFieldValueStrategy()), REMARK("r", new RemarkJsonFieldValueStrategy());
        private String code;
        private JsonFieldValueStrategy strategy;

        ConvertJsonStrategyEnum(String code, JsonFieldValueStrategy strategy) {
            this.code = code;
            this.strategy = strategy;
        }

        public String getCode() {
            return code;
        }

        public JsonFieldValueStrategy getStrategy() {
            return strategy;
        }
    }
}
