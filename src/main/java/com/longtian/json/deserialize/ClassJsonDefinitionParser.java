package com.longtian.json.deserialize;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Class转换成Json的解析器
 *
 * @author chenll
 * @date 2018/7/18 17:40
 * @since V1.0
 */
public class ClassJsonDefinitionParser {
    private JsonFieldValueStrategy jsonFieldValueStrategy = new DefaultJsonFieldValueStrategy();

    public String parserJsonString(String sourcePath, String className) {
        Map<String, List<String>> docMap = null;
        if (jsonFieldValueStrategy instanceof RemarkJsonFieldValueStrategy) {
            docMap = JavaDocReader.readDoc(sourcePath + File.separatorChar + className);
        }
        try {
            CustomJavaFileLoader loader = new CustomJavaFileLoader(sourcePath);
            Class<?> javaBean = loader.loadClass(className);
            List<String> docs = Objects.nonNull(docMap) ? docMap.get(javaBean.getSimpleName()) : null;
            Field[] fields = javaBean.getDeclaredFields();
            int i = 0;
            JSONObject jsonObject = new JSONObject();
            for (Field f : fields) {
                jsonObject.put(f.getName(),
                        jsonFieldValueStrategy.getValue(Objects.nonNull(docs) ? docs.get(i) : null, f));
                i++;
            }
            return JSON.toJSONString(jsonObject, true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setJsonFieldValueStrategy(JsonFieldValueStrategy jsonFieldValueStrategy) {
        this.jsonFieldValueStrategy = jsonFieldValueStrategy;
    }
}
