/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.service;

import com.api.open.read.api.entity.BaseEntity;
import com.api.open.read.api.entity.SimplePage;
import com.api.open.read.api.service.IOpenReadService;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author NMSLAP570
 * @param <T>
 */
public interface IOpenCrudService<T extends BaseEntity>
//        extends IOpenReadService<BaseEntity> 
{

    T updateEntity(T t);

    T createEntity(T t);
    
      List<T> findAll();

    public SimplePage<T>  findByValue(T t, final Pageable pageable, Boolean matchingAny);

    public SimplePage<T> findAll(final Pageable pageable);

    T findById(Long id);

}
