/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.utility;

import com.api.open.crud.api.exception.OpenCrudApiException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NaturalId;

/**
 *
 * @author NMSLAP570
 */
@Slf4j
public class OpenCrudApiUtility {

    public static <T> T updateObject(T entityFromDb, T entity) {
        Class<?> clazz = entityFromDb.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                if (field.getName().equalsIgnoreCase("serialVersionUID") 
                    || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                Object fieldValue = field.get(entity);
                if (fieldValue != null && !hasNaturalIdAnnotation(field)) {
                    field.set(entityFromDb, fieldValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
        return entityFromDb;
    }

    public static <T> T keepAnnotatedFields(T originalEntity) {
        try {
            Class<?> entityClass = originalEntity.getClass();
            T newEntity = (T) entityClass.getDeclaredConstructor().newInstance(); // Create a new instance

            Field[] fields = entityClass.getDeclaredFields();

            for (Field field : fields) {
                if (field.getName().equalsIgnoreCase("serialVersionUID")) {
                    continue;
                }
                if (hasNaturalIdAnnotation(field)) {
                    try {
                        field.setAccessible(true);
                        field.set(newEntity, field.get(originalEntity)); // Copy the value from original entity
                    } catch (IllegalAccessException e) {
                        e.printStackTrace(); // Handle the exception appropriately
                    }
                }
            }
            return newEntity;
        } catch (Exception e) {
            log.info("Exception while keepAnnotatedFields :: entity :: " + originalEntity.toString());
            throw new OpenCrudApiException("Exception when converting record to object!");
        }
    }

    public static boolean hasNaturalIdAnnotation(Field field) {
        NaturalId naturalIdAnnotation = field.getAnnotation(NaturalId.class);
        return naturalIdAnnotation != null;
    }

    public static <T> void setValueViaReflection(Class<T> clazz,
            T entityObj, String excelColumnName, String cellValue) {
        Class<T> entityClass = clazz;
        try {
            if (cellValue == null || cellValue.isEmpty()) {
                return;
            }
            String methodName = "set" + capitalize(excelColumnName); //setFccode
            Field field = entityClass.getDeclaredField(excelColumnName);
            field.setAccessible(true);
            Method method = entityClass.getMethod(methodName, field.getType());
            // if field type is integer and then the value is string then throw exception            
            if (field.getType().equals(Integer.class)) {
                method.invoke(entityObj, Integer.valueOf(cellValue));
            } else if (field.getType().equals(Long.class)) {
                method.invoke(entityObj, Long.valueOf(cellValue));
            } else if (field.getType().equals(Date.class)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                Date date = dateFormat.parse(cellValue);
                method.invoke(entityObj, date);
            } else if (field.getType().equals(Boolean.class)) {
                if (!cellValue.equalsIgnoreCase("true") && !cellValue.equalsIgnoreCase("false")) {
                    throw new OpenCrudApiException("The accepted value must be either true or false | for column : " + excelColumnName + " | value : " + cellValue);
                }
                method.invoke(entityObj, Boolean.valueOf(cellValue));
            } else {
                method.invoke(entityObj, cellValue);
            }

        } catch (NumberFormatException e) {
            log.info("NumberFormatException while setting value for column : {} | value : {} | exception : {}", excelColumnName, cellValue, e.getMessage());
            throw new OpenCrudApiException("ColumnName :: " + excelColumnName
                    + " | value :: " + cellValue + " is not a integer!");
        } catch (OpenCrudApiException e) {
            log.info("OpenFileParserException :: {} ", e.getMessage());
            throw new OpenCrudApiException(e.getMessage());
        } catch (Exception e) {
            log.info("Exception while setting value for column : {} | value : {} | exception : {}", excelColumnName, cellValue, e);
            throw new OpenCrudApiException("Exception while setValueViaReflection!");
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

}
