/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.service;

import com.api.open.crud.api.repository.OpenCrudApiRepository;
import com.api.open.read.api.entity.BaseEntity;
import com.api.open.read.api.entity.SimplePage;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        //        extends OpenReadService<BaseEntity>
        implements IOpenCrudService<T> {

    @Autowired
    protected OpenCrudApiRepository<T> openCrudApiRepository;

    @Override
    public List<T> findAll() {
        return openCrudApiRepository.findAll();
    }

    @Override
    public SimplePage<T> findByValue(T t, Pageable pageable, Boolean matchingAny) {
        log.info(" In Method :: {} {} ", t);
        ExampleMatcher matcher = null;
        if (matchingAny) {
            matcher = ExampleMatcher.matchingAny();
        } else {
            matcher = ExampleMatcher.matchingAll();
        }

        final Page<T> page = openCrudApiRepository.findAll(Example.of(t, matcher), pageable);
        log.info(" Successfully fectched purchase order list of size :: {} ", page.getNumberOfElements());
        return new SimplePage<>(page.getContent(), page.getTotalElements(), pageable);

    }

    @Override
    public T findById(Long id) {
        try {
            Optional<T> optional = openCrudApiRepository.findById(id);
            return optional.isPresent() ? optional.get() : null;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public SimplePage<T> findAll(final Pageable pageable) {
        final Page<T> page = openCrudApiRepository.findAll(pageable);
        return new SimplePage<>(page.getContent(), page.getTotalElements(), pageable);
    }

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
