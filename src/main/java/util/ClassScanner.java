package util;

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

        // 将包名转成路径 e.g., "controller" → "controller/"
        String path = packageName.replace('.', '/');

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            if ("file".equals(url.getProtocol())) {
                File directory = new File(url.toURI());
                if (directory.exists()) {
                    for (File file : directory.listFiles()) {
                        if (file.getName().endsWith(".class")) {
                            String className = packageName + "." + file.getName().replace(".class", "");
                            classes.add(Class.forName(className));
                        }
                    }
                }
            } else if ("jar".equals(url.getProtocol())) {
                JarURLConnection jarConn = (JarURLConnection) url.openConnection();
                try (JarFile jarFile = jarConn.getJarFile()) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (!entry.isDirectory() && name.startsWith(path) && name.endsWith(".class")) {
                            // e.g., controller/BasicController.class -> controller.BasicController
                            String className = name.replace('/', '.').replace(".class", "");
                            classes.add(Class.forName(className));
                        }
                    }
                }
            }
        }

//        // 获取资源路径
//        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
//        if (resource == null) {
//            throw new IllegalArgumentException("Package not found: " + path);
//        }
//
//        File directory = new File(resource.toURI());
//
//        // 遍历目录下所有 .class 文件
//        for (File file : directory.listFiles()) {
//            if (file.getName().endsWith(".class")) {
//                String className = packageName + "." + file.getName().replace(".class", "");
//                Class<?> clazz = Class.forName(className);
//                classes.add(clazz);
//            }
//        }

        return classes;
    }
}
