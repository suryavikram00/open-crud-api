/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.controller;

import static com.api.open.crud.api.constants.OpenCrudEndPoints.ENDPOINT_OPEN_CRUD_PREFIX;
import com.api.open.read.api.entity.BaseEntity;
import com.api.open.read.api.enums.StatusEnum;
import com.api.open.read.api.model.CrudApiResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.api.open.crud.api.service.IOpenCrudService;
import com.api.open.read.api.controller.OpenReadController;

/**
 *
 * @author NMSLAP570
 * @param <T>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@ResponseBody
@Slf4j
@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping(path = ENDPOINT_OPEN_CRUD_PREFIX)
public class OpenCrudController<T extends BaseEntity>
        extends OpenReadController<T>
        implements IOpenCrudController<T> {

    @Autowired
    private IOpenCrudService<T> openCrudService;

    @Override
    @PutMapping
    public ResponseEntity<CrudApiResponse<T>> updateEntity(@Valid @RequestBody T t) {
        CrudApiResponse<T> crudApiResponse = new CrudApiResponse<T>(StatusEnum.SUCCESS).addMessage("Data updated successfully");
        crudApiResponse.setObject(openCrudService.updateEntity(t));

        return new ResponseEntity(crudApiResponse, HttpStatus.OK);
    }

    @Override
    @PostMapping
    public ResponseEntity<CrudApiResponse<T>> createEntity(@Valid @RequestBody T t) {
        CrudApiResponse<T> crudApiResponse = new CrudApiResponse<T>(StatusEnum.SUCCESS).addMessage("Data created successfully");
        crudApiResponse.setObject(openCrudService.createEntity(t));
        return new ResponseEntity(crudApiResponse, HttpStatus.OK);
    }

}
