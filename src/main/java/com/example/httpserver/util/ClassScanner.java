package com.example.httpserver.util;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    public static Set<Class<?>> getClasses(String packageName) throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        /**
         * 这行代码的作用是获取当前线程的上下文类加载器（ContextClassLoader）。
         * 在 Java 中，类加载器负责加载类文件。通过 Thread.currentThread().getContextClassLoader()，
         * 可以获得当前线程关联的类加载器，通常用于动态加载类或资源，尤其是在框架或容器环境下（如 Web 服务器、应用服务器），
         * 这样可以确保加载到的是应用自己的类，而不是 JDK 或其他库的类。
         */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path); // 获取指定包路径下的所有资源

        // 遍历所有资源，根据资源的协议（file 或 jar）来分别处理
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            // 如果资源是文件协议，则直接查找文件系统中的类
            if (resource.getProtocol().equals("file")) {
                // 将 URL 转换为文件对象，并递归查找该目录下的所有类
                classes.addAll(findClasses(new File(resource.toURI()), packageName));
            // 如果资源是 jar 协议，则需要处理 jar 文件中的类
            } else if (resource.getProtocol().equals("jar")) {
                JarURLConnection jarConn = (JarURLConnection) resource.openConnection();
                try (JarFile jarFile = jarConn.getJarFile()) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(path) && name.endsWith(".class") && !entry.isDirectory()) {
                            String className = name.replace('/', '.').substring(0, name.length() - 6);
                            classes.add(Class.forName(className)); // 用反射机制加载类，添加到集合中
                        }
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 递归查找指定目录下的所有类文件，并将其转换为 Class 对象
     * @param directory 指定的目录
     * @param packageName 包名
     * @return 包含所有找到的类的集合
     * @throws ClassNotFoundException 如果类加载失败
     */
    private static Set<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                // 去掉 .class 后缀，构造完整的类名
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                // 使用 Class.forName() 方法反射机制加载类
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }
}
