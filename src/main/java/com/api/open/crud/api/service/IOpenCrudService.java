/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.service;

import com.api.open.read.api.entity.BaseEntity;
import com.api.open.read.api.service.IOpenReadService;

/**
 *
 * @author NMSLAP570
 * @param <T>
 */
public interface IOpenCrudService<T extends BaseEntity> extends IOpenReadService<BaseEntity>{
      
    T updateEntity(T t);
    
    T createEntity(T t);
        
}
