package com.example.httpserver.util;

import java.util.*;

/**
 * 用哈希表存储接口与实现类的映射关系
 */
public class InterfaceMapping {

    public static Map<Class<?>, List<Class<?>>> getInterfaceToImplMap(Set<Class<?>> classes) throws Exception {
        // Set<Class<?>> classes = ClassScanner.getClasses(basePackage); // 获取指定包下的所有类
        Map<Class<?>, List<Class<?>>> interfaceToImplMap = new HashMap<>();

        for (Class<?> clazz : classes) {
            if (clazz.isInterface()) {
                // 如果是接口，跳过
                continue;
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> iface : interfaces) {
                // 如果接口不存在于映射中，则创建一个新的列表
                if (!interfaceToImplMap.containsKey(iface)) {
                    interfaceToImplMap.put(iface, new ArrayList<>());
                }
                // 将实现类添加到接口的列表中
                interfaceToImplMap.get(iface).add(clazz);
            }
        }
        return interfaceToImplMap;
    }

}
