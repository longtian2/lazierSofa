package com.longtian.json.deserialize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * 自定义的Java文件加载器
 *
 * @author chenll
 * @date 2018/7/18 10:16
 * @since V1.0
 */
public class CustomJavaFileLoader extends ClassLoader {
    private static final char POINT = '.';
    private static final String JDK_CLASS = "java.lang.";
    private String sourcePath;

    public CustomJavaFileLoader(String sourcePath) {
        super();
        this.sourcePath = sourcePath;
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        try {
            if (!className.startsWith(JDK_CLASS)) {
                compiler(className);
            }
            return super.loadClass(className);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            String classPath =
                    sourcePath + File.separatorChar + className.replace(POINT, File.separatorChar) + ".class";
            File file = new File(classPath);
            if (file.exists()) {
                file.delete();
            }
        }
        return null;
    }

    private void compiler(String className) throws IOException {
        String classPath = sourcePath + File.separatorChar + className.replace(POINT, File.separatorChar) + ".java";
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        System.out.println("compiler is null ?  " + Objects.isNull(compiler));
        compiler.run(null, null, null, "-encoding", "UTF-8", classPath);
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        byte[] classData = loadClassData(className);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(className, classData, 0, classData.length);
        }
    }

    private byte[] loadClassData(String className) {
        String fileName = sourcePath + File.separatorChar + className.replace(POINT, File.separatorChar) + ".class";
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
