/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.utility;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author NMSLAP570
 */
@Slf4j
public class OpenCrudUtility {

    public static <T> String getFieldNames(Class<T> clazz) {
        List<String> fieldNames = new ArrayList();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("serialVersionUID")) {
                continue;
            }
            fieldNames.add(field.getName());
        }
        return String.join(",", fieldNames);
    }

    public static <T> String extractFieldValues(T object) throws IllegalAccessException {
        List<String> fieldValues = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("serialVersionUID")) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null) {
                String fieldValue = String.valueOf(value).replace(",", "\\,"); // Escape pipe symbols
                fieldValues.add('"' + fieldValue + '"');
            } else {
                fieldValues.add(""); // Add an empty string if the value is null
            }
        }
        return String.join(",", fieldValues);
    }

//    public static Class<?> getClassName(TableNameEnum tableNameEnum) {
//        String className = tableNameEnum.getQualifiedClassName();
//        Class<?> clazz = null;
//        try {
//            clazz = Class.forName(className);
//            // Now you can use the 'clazz' object to access the class type
//            log.info("Class type: " + clazz.getName());
//        } catch (ClassNotFoundException e) {
//            // Handle the exception if the class is not found
//            throw new CrudApiException("Exception when trying to create class :: " + className);
//        }
//        return clazz;
//    }
    
    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

}
