package com.alexcorp.example.itextsandbox.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class DataMapperService {

    public Map<String, String> mapFields(Object object, boolean useSuper) throws IllegalAccessException {
        return mapFields(object, object.getClass(), useSuper);
    }

    private Map<String, String> mapFields(Object object, Class<?> aClass, boolean useSuper) throws IllegalAccessException {
        Map<String, String> values = new HashMap<>();
        if (useSuper) {
            if (aClass != null && !aClass.getSuperclass().equals(Object.class)) {
                values.putAll(mapFields(object, aClass.getSuperclass(), true));
            }
        }

        if (aClass != null) {
            for (Field field : aClass.getDeclaredFields()) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    field.setAccessible(true);
                }
                values.put(field.getName(), field.get(object).toString());
            }
        }
        return values;
    }
}
