/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.entity;

import com.api.open.crud.api.service.excelutils.IOpenCrudEntityAttributes;
import com.api.open.crud.api.utility.OpenCrudApiUtility;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.NaturalId;

/**
 *
 * @author NMSLAP570
 */
@MappedSuperclass
//@Entitys
public abstract class BaseEntity<T> implements Serializable, IOpenCrudEntityAttributes {

     private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private T id;

    public BaseEntity() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        List<Field> naturalIdFields = getNaturalIdFields();
        int result = 17;
        for (Field field : naturalIdFields) {
            result = 31 * result + Objects.hashCode(getFieldValue(field));
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        List<Field> naturalIdFields = getNaturalIdFields();
        for (Field field : naturalIdFields) {
            if (!Objects.equals(getFieldValue(field), other.getFieldValue(field))) {
                return false;
            }
        }
        return true;
    }

    protected List<Field> getNaturalIdFields() {
        List<Field> naturalIdFields = new ArrayList<>();
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(NaturalId.class)) {
                field.setAccessible(true);
                naturalIdFields.add(field);
            }
        }
        return naturalIdFields;
    }

    protected Object getFieldValue(Field field) {
        try {
            return field.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing field value", e);
        }
    }

    public static <T> Map<T, T> convertSetToHashMap(List<T> set) {
        Map<T, T> map = new HashMap<>();
        set.forEach(item -> {
            T itemWithAnnotatedAttributes = OpenCrudApiUtility.keepAnnotatedFields(item);
            map.put(itemWithAnnotatedAttributes, item);
        });
        return map;
    }

}
