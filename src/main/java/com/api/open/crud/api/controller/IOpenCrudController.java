/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.controller;

import com.api.open.crud.api.entity.BaseEntity;
import com.api.open.crud.api.exception.model.CrudApiResponse;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author NMSLAP570
 * @param <T>
 */
public interface IOpenCrudController<T extends BaseEntity<ID>, ID> {
    
   public ResponseEntity<CrudApiResponse<T>> updateEntity(@RequestBody T t);

    public ResponseEntity<CrudApiResponse<T>> createEntity(@RequestBody T t);

    public ResponseEntity<CrudApiResponse<T>> findAll();

    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<CrudApiResponse<T>> findById(@PathVariable ID id);

    public ResponseEntity<CrudApiResponse<T>> findAllByPageable(
            Boolean isPaged,
            @RequestParam(name = "sortColumn", required = false) String sortColumn,
            @RequestParam(name = "sortDirection", required = false) String sortDirection,
            @PageableDefault(size = 20) final Pageable pageable);

    public ResponseEntity<CrudApiResponse<T>> findByFilter(T t,
            Boolean isPaged,
            @SortDefault(sort = "id") @PageableDefault(size = 10) Pageable pageable,
            Boolean matchingAny);

    public void exportData(List<T> list, HttpServletResponse response);

    public void downloadFileFormat(HttpServletResponse response);

//    public ResponseEntity<?> bulkUpload(@RequestParam("file") MultipartFile file);

    public ResponseEntity<?> readFile(@RequestParam String path);

    public void exportDataByFilter(T t, HttpServletResponse response);
  

}
