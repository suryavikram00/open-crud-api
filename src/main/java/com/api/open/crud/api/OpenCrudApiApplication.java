/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
