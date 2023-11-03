/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.exception.model;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author NMSLAP570
 */
@AllArgsConstructor
public class ValidExcelObject<T> {

    @Getter
    private List<String> record;
    @Getter
    private T t;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.t);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValidExcelObject<?> other = (ValidExcelObject<?>) obj;
        if (!Objects.equals(this.t, other.t)) {
            return false;
        }
        return true;
    }
    
    

}
