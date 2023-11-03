/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.service;

import com.api.open.crud.api.repository.OpenCrudApiRepository;
import com.api.open.crud.api.entity.BaseEntity;
import com.api.open.crud.api.entity.SimplePage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class OpenCrudService<T extends BaseEntity<ID>, ID>
        implements IOpenCrudService<T, ID> {

    @Autowired
    protected OpenCrudApiRepository<T, ID> openCrudApiRepository;

    @Override
    public T findById(ID id) {
        try {
            Optional<T> optional = openCrudApiRepository.findById(id);
            return optional.orElse(null);
        } catch (Exception e) {
            throw e;
        }
    }

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
//    @Transactional
    public SimplePage<T> findByValue(List<T> tList, Pageable pageable, Boolean matchingAny) {
        log.info("In Method, findByValue method, size :: {}", tList.size());

        ExampleMatcher matcher = matchingAny ? ExampleMatcher.matchingAny() : ExampleMatcher.matchingAll();

        List<Example<T>> examples = tList.stream()
                .map(exampleObject -> Example.of(exampleObject, matcher))
                .collect(Collectors.toList());

        List<T> resultList = examples.stream()
                .map(example -> openCrudApiRepository.findAll(example, pageable))
                .flatMap(page -> page.getContent().stream())
                .collect(Collectors.toList());

        long totalElements = resultList.size();

        return new SimplePage<>(resultList, totalElements, pageable);
    }

    @Override
    public SimplePage<T> findAll(final Pageable pageable) {
        final Page<T> page = openCrudApiRepository.findAll(pageable);
        return new SimplePage<>(page.getContent(), page.getTotalElements(), pageable);
    }

    @Override
//    @Transactional
    public synchronized T updateEntity(T t) {
        return openCrudApiRepository.save(t);
    }

    @Override
//    @Transactional
    public synchronized List<T> updateEntity(List<T> t) {
        return openCrudApiRepository.saveAll(t);
    }

    @Override
    @Transactional
    public T createEntity(T t) {
        return openCrudApiRepository.save(t);
    }
}

