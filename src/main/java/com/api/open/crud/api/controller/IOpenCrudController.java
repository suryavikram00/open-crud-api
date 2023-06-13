/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.controller;

import com.api.open.read.api.controller.IOpenReadController;
import com.api.open.read.api.entity.BaseEntity;
import com.api.open.read.api.model.CrudApiResponse;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author NMSLAP570
 * @param <T>
 */
public interface IOpenCrudController<T extends BaseEntity>
        extends IOpenReadController<T> 
{

    public ResponseEntity<CrudApiResponse<T>> updateEntity(@RequestBody T t);

    public ResponseEntity<CrudApiResponse<T>> createEntity(@RequestBody T t);
    
    
    public ResponseEntity<CrudApiResponse<T>> findAll();

    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<CrudApiResponse<T>> findById(@PathVariable Long id);

    public ResponseEntity<CrudApiResponse<T>> findAllByPageable(
            Boolean isPaged,
            @SortDefault(sort = "priRole")
            @PageableDefault(size = 20) final Pageable pageable);

    public ResponseEntity<CrudApiResponse<T>> findByFilter(T t,
            Boolean isPaged,
            @SortDefault(sort = "id") @PageableDefault(size = 10) Pageable pageable,
            Boolean matchingAny);

    public void exportData(
            List<T> list,
            HttpServletResponse response);

}
