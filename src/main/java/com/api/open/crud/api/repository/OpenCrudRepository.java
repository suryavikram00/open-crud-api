/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.repository;

/**
 *
 * @author NMSLAP570
 */
import com.api.open.read.api.entity.BaseEntity;
import com.api.open.read.api.repository.CrudApiRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OpenCrudRepository<T extends BaseEntity> extends CrudApiRepository<T> {

}
