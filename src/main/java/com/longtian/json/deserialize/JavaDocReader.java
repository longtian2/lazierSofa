package com.longtian.json.deserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;

/**
 * Java文件Doc读取器
 *
 * @author chenll
 * @date 2018/7/18 9:48
 * @since V1.0
 */
public class JavaDocReader {
    /**
     * 回车换行
     */
    private static final String ENTER_SEPARATOR = "\n";

    private static RootDoc root;

    public static Map<String, List<String>> readDoc(String classPath) {
        com.sun.tools.javadoc.Main.execute(new String[] {"-doclet", CustomDoclet.class.getName(), "-encoding", "utf-8",
                "-private", classPath + ".java"});
        ClassDoc[] classes = root.classes();
        Map<String, List<String>> classDocMap = new HashMap<>();
        for (int i = 0; i < classes.length; i++) {
            String className = classes[i].name();
            List<String> list = classDocMap.get(className);
            if (Objects.isNull(list)) {
                list = new ArrayList<>();
                classDocMap.put(className, list);
            }
            for (FieldDoc fieldDoc : classes[i].fields()) {
                list.add(fieldDoc.getRawCommentText().replace(ENTER_SEPARATOR, ""));
            }
        }
        return classDocMap;
    }

    public static class CustomDoclet extends Doclet {

        public static boolean start(RootDoc rootDoc) {
            root = rootDoc;
            return true;
        }
    }
}
