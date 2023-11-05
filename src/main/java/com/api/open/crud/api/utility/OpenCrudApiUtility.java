/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.utility;

import com.api.open.crud.api.entity.BaseEntity;
import com.api.open.crud.api.entity.OpenCrudMetadata;
import com.api.open.crud.api.exception.OpenCrudApiException;
import com.api.open.crud.api.exception.model.ValidExcelObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NaturalId;

/**
 *
 * @author NMSLAP570
 */
@Slf4j
public class OpenCrudApiUtility {

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

    // this method should return the fiels which is not annotated with json ignore
    public static <T> String extractFieldValues(BaseEntity<T> object) throws IllegalAccessException {
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
                fieldValues.add(fieldValue);
            } else {
                fieldValues.add(""); // Add an empty string if the value is null
            }
        }
        return String.join(",", fieldValues);
    }

    public static <T> String extractFieldValuesWithHeader(BaseEntity<T> object) throws IllegalAccessException {
        List<String> headerNameList = object.getTableHeaderNames();
        List<String> fieldValues = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("serialVersionUID")
                    || !headerNameList.contains(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null) {
                String fieldValue = String.valueOf(value).replace(",", "\\,"); // Escape pipe symbols
                fieldValues.add(fieldValue);
            } else {
                fieldValues.add(""); // Add an empty string if the value is null
            }
        }
        return String.join(",", fieldValues);
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static List<BaseEntity<?>> getDerivedInstances() throws IOException, ClassNotFoundException {
        List<BaseEntity<?>> derivedInstances = new ArrayList<>();

        // Get all classes in the classpath
        List<Class<?>> classes = OpenCrudApiUtility.find("com.netmeds.magnum");

        // Iterate over the classes and check if they extend BaseEntity
        for (Class<?> clazz : classes) {
            if (!Modifier.isAbstract(clazz.getModifiers()) && BaseEntity.class.isAssignableFrom(clazz)) {
                try {
                    BaseEntity<?> instance = (BaseEntity<?>) clazz.getDeclaredConstructor().newInstance();
                    derivedInstances.add(instance);
                } catch (Exception e) {
                    // Handle any exception during instance creation
                    e.printStackTrace();
                }
            }
        }

        return derivedInstances;
    }

    public static List<OpenCrudMetadata> getClassName() throws IOException, ClassNotFoundException {
        List<OpenCrudMetadata> classNameList = new ArrayList<>();
        List<BaseEntity<?>> derivedInstances = new ArrayList<>();

        // Get all classes in the classpath
        List<Class<?>> classes = OpenCrudApiUtility.find("com.netmeds.magnum");

        // Iterate over the classes and check if they extend BaseEntity
        for (Class<?> clazz : classes) {
            if (!Modifier.isAbstract(clazz.getModifiers()) && BaseEntity.class.isAssignableFrom(clazz)) {
                try {
                    BaseEntity<?> instance = (BaseEntity<?>) clazz.getDeclaredConstructor().newInstance();
                    derivedInstances.add(instance);
                    classNameList.add(new OpenCrudMetadata(clazz.getSimpleName()));
                } catch (Exception e) {
                    // Handle any exception during instance creation
                    e.printStackTrace();
                }
            }
        }

        return classNameList;
    }

    private static List<Class<?>> find(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        List<File> directories = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            directories.add(new File(resource.getFile()));
        }

        List<Class<?>> classes = new ArrayList<>();
        for (File directory : directories) {
            classes.addAll(findClasses(directory, packageName));
        }

        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = Class.forName(className);
                    if (BaseEntity.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                        classes.add(clazz);
                    }
                }
            }
        }

        return classes;
    }

    public static <T> Stream<List<ValidExcelObject<T>>> batchesOfValidExcelObject(List<ValidExcelObject<T>> source, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length = " + length);
        }
        if (source == null) {
            return Stream.empty();
        }
        int size = source.size();
        if (size <= 0) {
            return Stream.empty();
        }
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1).mapToObj(
                n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
    }

    public static <T> Stream<List<T>> batches(List<T> source, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length = " + length);
        }
        if (source == null) {
            return Stream.empty();
        }
        int size = source.size();
        if (size <= 0) {
            return Stream.empty();
        }
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1).mapToObj(
                n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
    }

    public static <T> T updateObject(T entityFromDb, T entity) {
        Class<?> clazz = entityFromDb.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                if (field.getName().equalsIgnoreCase("serialVersionUID")) {
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
