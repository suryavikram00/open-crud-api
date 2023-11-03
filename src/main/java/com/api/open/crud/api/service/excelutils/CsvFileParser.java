/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.service.excelutils;


import com.api.open.crud.api.exception.OpenFileParserException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author NMSLAP570
 */
@Slf4j
public class CsvFileParser extends IFileParser {

    public CsvFileParser(MultipartFile file) {
        super(file);
    }

    @Override
    public void fileTypeValidator() throws OpenFileParserException {
        if (super.getInputMultipartFile() == null || super.getInputMultipartFile().isEmpty()) {
            log.info("Empty file, File uploaded is invalid :: {}", super.getInputMultipartFile().getName());
            throw new OpenFileParserException("File uploaded is invalid :: " + super.getInputMultipartFile().getName());
        }
        String extension = FilenameUtils.getExtension(super.getInputMultipartFile().getOriginalFilename());
        if (!"csv".equalsIgnoreCase(extension)) {
            log.info("File uploaded is invalid, only csv file format would be accepted :: {}", super.getInputMultipartFile().getName());
            throw new OpenFileParserException("File uploaded is invalid, only csv file format would be accepted :: " + super.getInputMultipartFile().getName());
        }
    }

    @Override
    public void createOutputFile(String targetDirectory, String targetFileName, String fileExtension) {
        // create file
        this.outputFile = OpenFileUtility.createFile(targetDirectory, targetFileName, fileExtension);
    }

    public synchronized void appendToFile(List<String> record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFile, true))) {
            for (String value : record) {
                writer.write(value != null ? value : "");
                writer.write(','); // Add a comma between values
            }
            writer.newLine(); // Add a new line after the record
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
