/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api;

import com.api.open.crud.api.entity.BaseEntity;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import com.api.open.crud.api.repository.IBaseEntityRepository;

/**
 *
 * @author NMSLAP570
 */
@Configuration
public class OpenCrudApiApplication {
    
    @Bean
    @Primary
    public  IBaseEntityRepository getDefaultOpenCrudApiRepository() {        
        return null;        
    }

}
