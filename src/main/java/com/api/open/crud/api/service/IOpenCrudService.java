/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.service;

import com.api.open.crud.api.entity.BaseEntity;

import com.api.open.crud.api.entity.SimplePage;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 *
 * @author NMSLAP570
 * @param <T>
 */
@Component
public interface IOpenCrudService<T extends BaseEntity<ID>, ID> {

    public T findById(ID id);

    public T updateEntity(T t);

    public T createEntity(T t);

    public List<T> findAll();

    public SimplePage<T> findByValue(T t, final Pageable pageable, Boolean matchingAny);

    public SimplePage<T> findByValue(List<T> tList, Pageable pageable, Boolean matchingAny);

    public SimplePage<T> findAll(final Pageable pageable);

    public List<T> updateEntity(List<T> t);

}
