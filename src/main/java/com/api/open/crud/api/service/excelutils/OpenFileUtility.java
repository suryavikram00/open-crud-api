/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.service.excelutils;


import com.api.open.crud.api.exception.OpenFileParserException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author NMSLAP570
 */
@Slf4j
public class OpenFileUtility {

    public static List<String> getHeaderRecord(MultipartFile file) throws OpenFileParserException {
        BufferedReader br;
        String eachLine;
        try {
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((eachLine = br.readLine()) != null) {
                List<String> headerRecord = Arrays.asList(eachLine.split(","));
                log.info("First row fetched for file :: " + file.getName() + " | " + Arrays.toString(headerRecord.toArray()));
                return headerRecord;
            }
        } catch (Exception e) {
            log.info("getHeaderRecord exception :: " + e.toString());
            throw new OpenFileParserException("Failed during seggregation");
        }
        return Collections.EMPTY_LIST;
    }

    public static Boolean hasAtleastOneRecord(MultipartFile file) {
        Boolean hasAtleastOneRecord = Boolean.FALSE;
        BufferedReader br;
        String eachLine;
        int rowCount = 0;
        try {
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((eachLine = br.readLine()) != null) {
                rowCount++;
                if (rowCount == 2) {
                    hasAtleastOneRecord = Boolean.TRUE;
                    break;
                }
            }
        } catch (Exception e) {
            log.info("hasAtleastOneRecord exception :: " + e.toString());
        }
        log.info("File :: " + file.getName() + " has atleast one record :: " + hasAtleastOneRecord);
        return hasAtleastOneRecord;
    }

    public static void saveMultipartFile(MultipartFile multipartFile, String targetDirectory, String targetFileName)
            throws OpenFileParserException {

        try {
            String originalFileName = multipartFile.getOriginalFilename();

            // Extract the file extension from the original file name
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            InputStream inputStream = multipartFile.getInputStream();
            Path targetPath = Paths.get(targetDirectory, targetFileName + fileExtension);

            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

            inputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OpenFileParserException(e.getMessage());
        }

    }
    

    public static File createFile(String targetDirectory, String targetFileName, String fileExtension) 
    throws OpenFileParserException{

        try {
            // Create the target directory if it doesn't exist
            File directory = new File(targetDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create the new File object
            File newFile = new File(directory, targetFileName + fileExtension);

            // Create the file if it doesn't exist
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            return newFile;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OpenFileParserException(e.getMessage());
        }
    }

}
