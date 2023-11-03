/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.entity;

import javax.persistence.Transient;

/**
 *
 * @author NMSLAP570
 */
public class OpenCrudMetadata {
    
    @Transient
    protected String tableApiName;
//    @Transient
//    protected Boolean serverPaginationEnabled;

    public OpenCrudMetadata(String tableApiName) {
        this.tableApiName = tableApiName;
//        this.serverPaginationEnabled = true;
    }

    public String getTableApiName() {
        return tableApiName;
    }

//    public Boolean getServerPaginationEnabled() {
//        return serverPaginationEnabled;
//    }
    
    
    
}
