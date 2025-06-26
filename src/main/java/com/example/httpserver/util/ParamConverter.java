package com.example.httpserver.util;

public class ParamConverter {

    public static Object convert(String value, Class<?> type) {
        if (value == null) return getDefaultValue(type);

        if (type == String.class) return value;
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == long.class || type == Long.class) return Long.parseLong(value);
        if (type == double.class || type == Double.class) return Double.parseDouble(value);
        if (type == float.class || type == Float.class) return Float.parseFloat(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);

        // 可以拓展更多类型
        throw new IllegalArgumentException("Unsupported param type: " + type.getName());
    }

    private static Object getDefaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == double.class) return 0.0;
            if (type == float.class) return 0.0f;
            if (type == boolean.class) return false;
            // 可以添加更多
        }
        return null;
    }
}
