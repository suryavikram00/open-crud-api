/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.open.crud.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 *
 * @author NMSLAP570
 */
public interface IOpenCrudEntityAttributes {
    
    @JsonIgnore
    public List<String> getTableHeaderNames();
    @JsonIgnore
    public String updatedByColumn();
    @JsonIgnore
    public String updatedOnColumn();    
    @JsonIgnore
    public String createdByColumn();    
    @JsonIgnore
    public String createdOnColumn();
    
}
