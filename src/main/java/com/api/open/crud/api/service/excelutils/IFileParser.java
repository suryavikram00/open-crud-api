/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.open.crud.api.service.excelutils;


import com.api.open.crud.api.exception.OpenFileParserException;
import java.io.File;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author NMSLAP570
 */
public abstract class IFileParser {

    @Getter
    protected MultipartFile inputMultipartFile;
    
    @Getter
    protected File outputFile;
            
    public IFileParser(MultipartFile file) {
        this.inputMultipartFile = file;
    }
    
    public abstract void fileTypeValidator() throws OpenFileParserException;
    
    public abstract  void appendToFile(List<String> record);
    
    public abstract void createOutputFile( String targetDirectory, String targetFileName, String fileExtension);
    
}
