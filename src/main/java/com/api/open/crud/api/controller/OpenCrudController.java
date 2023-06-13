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
import com.api.open.read.api.entity.SimplePage;
import com.api.open.read.api.utility.OpenReadApiUtility;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
//        extends OpenReadController<T>
        implements IOpenCrudController<T> {

    @Autowired
    private IOpenCrudService<T> openCrudService;
    

    @Override
    @GetMapping
    public ResponseEntity<CrudApiResponse<T>> findAll() {
        List<T> list = openCrudService.findAll();
        CrudApiResponse<T> crudApiResponse = new CrudApiResponse<>(StatusEnum.SUCCESS);
        crudApiResponse.setObjectList(list);
        return new ResponseEntity(crudApiResponse, HttpStatus.OK);
    }

    @Override
    @GetMapping("/paginate")
    public ResponseEntity<CrudApiResponse<T>> findAllByPageable(
            Boolean isPaged,
            @SortDefault(sort = "id")
            @PageableDefault(size = 10) Pageable pageable) {
        if (isPaged == null || Boolean.FALSE.equals(isPaged)) {
            pageable = Pageable.unpaged();
        }
        SimplePage<T> page = openCrudService.findAll(pageable);
        CrudApiResponse<T> crudApiResponse = new CrudApiResponse<>(StatusEnum.SUCCESS);
        crudApiResponse.setPageData(page);
        return new ResponseEntity(crudApiResponse, HttpStatus.OK);
    }

    @Override
    @PostMapping("/export")
    public void exportData(
            @RequestBody List<T> list,
            HttpServletResponse response) {
        try {
            Class<T> genericType = ((Class) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0]);
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + genericType.getSimpleName() + ".csv\"");
            PrintWriter writer = response.getWriter();
            writer.println(OpenReadApiUtility.getFieldNames(genericType));
            for (T eachObject : list) {
                writer.println(OpenReadApiUtility.extractFieldValues(eachObject));
            }
        } catch (Exception e) {
            log.error("Exception in findAll method :: ", e);
        }
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<CrudApiResponse<T>> findById(@PathVariable Long id) {
        try {
            CrudApiResponse<T> crudApiResponse = new CrudApiResponse<>(StatusEnum.SUCCESS);
            crudApiResponse.setObject(openCrudService.findById(id));
            return new ResponseEntity(crudApiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception in findById method :: ", e);
            return new ResponseEntity(new CrudApiResponse<T>(StatusEnum.FAILURE), HttpStatus.OK);
        }
    }

    @Override
    @GetMapping(value = "/search")
    public ResponseEntity<CrudApiResponse<T>> findByFilter(T t,
            Boolean isPaged,
            @SortDefault(sort = "id")
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = true, defaultValue = "true") Boolean matchingAny) {
        try {
            if (isPaged == null || Boolean.FALSE.equals(isPaged)) {
                pageable = Pageable.unpaged();
            }
            SimplePage<T> page = openCrudService.findByValue(t, pageable, matchingAny);
            CrudApiResponse<T> crudApiResponse = new CrudApiResponse<>(StatusEnum.SUCCESS);
            crudApiResponse.setPageData(page);
            return new ResponseEntity(crudApiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception in findByFilter method :: ", e);
            return new ResponseEntity(new CrudApiResponse<T>(StatusEnum.FAILURE), HttpStatus.OK);
        }
    }

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
