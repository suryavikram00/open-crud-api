/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.service;

import com.api.open.crud.api.repository.OpenCrudApiRepository;
import com.api.open.read.api.entity.BaseEntity;
import com.api.open.read.api.service.OpenReadService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author NMSLAP570
 * @param <T>
 */
@Service
@Slf4j
public class OpenCrudService<T extends BaseEntity>
        extends OpenReadService<BaseEntity>
        implements IOpenCrudService<T> {

    @Autowired
    protected OpenCrudApiRepository<T> openCrudApiRepository;

    @Override
    @Transactional
    public T updateEntity(T t) {
        // if accredition is not enabled then, save

        // if enabled create a request
        return openCrudApiRepository.save(t);
    }

    @Override
    @Transactional
    public T createEntity(T t) {
        return openCrudApiRepository.save(t);
    }

}
