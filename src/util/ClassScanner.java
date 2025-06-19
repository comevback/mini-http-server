// src/util/ClassScanner.java
package util;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ClassScanner {

    public static Set<Class<?>> getClasses(String packageName) throws Exception {
        Set<Class<?>> classes = new HashSet<>();

        // 将包名转成路径 e.g., "controller" → "controller/"
        String path = packageName.replace('.', '/');

        // 获取资源路径
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("Package not found: " + path);
        }

        File directory = new File(resource.toURI());

        // 遍历目录下所有 .class 文件
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        }

        return classes;
    }
}
