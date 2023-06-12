/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.controller;

import com.api.open.read.api.controller.IOpenReadController;
import com.api.open.read.api.entity.BaseEntity;
import com.api.open.read.api.model.CrudApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author NMSLAP570
 * @param <T>
 */
public interface IOpenCrudController<T extends BaseEntity> extends IOpenReadController<T> {

    ResponseEntity<CrudApiResponse<T>> updateEntity(@RequestBody T t);

    ResponseEntity<CrudApiResponse<T>> createEntity(@RequestBody T t);

}
