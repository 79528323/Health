package com.gzhealthy.health.tool;


import java.lang.reflect.Field;
import java.util.Map;

public class ObjectUtil {

    public static Map transformBeanToMap(Object object, Map<String, Object> map)
            throws IllegalArgumentException, IllegalAccessException {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value = field.get(object);
            map.put(field.getName(), value);
        }
        return map;
    }
}