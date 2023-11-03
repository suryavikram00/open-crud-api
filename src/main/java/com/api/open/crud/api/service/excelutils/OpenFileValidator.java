/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.service.excelutils;


import com.api.open.crud.api.entity.BaseEntity;
import com.api.open.crud.api.entity.SimplePage;
import com.api.open.crud.api.exception.OpenFileParserException;
import com.api.open.crud.api.exception.model.ValidExcelObject;
import com.api.open.crud.api.service.IOpenCrudService;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author NMSLAP570
 * @param <T>
 */
@Slf4j
public class OpenFileValidator<T extends BaseEntity<ID>, ID> {

//    private final IFileParser fileParser;
//
//    private final IOpenCrudService<T, ID> openCrudService;
//
////    private final INcrFileProcessingService ncrFileProcessingService;
//
//    private static final String INPUT_FILE_PREFIX = "ip_";
//
//    private static final String OUTPUT_FILE_PREFIX = "op_";
//
//    private static final String REMARK_COLUMN = "remarks";
//
//    ExecutorService customThreadPool = null;
//
//    public OpenFileValidator(IFileParser fileParser,
//            IOpenCrudService<T, ID> openCrudService,
//            INcrFileProcessingService ncrFileProcessingService) {
//        this.fileParser = fileParser;
//        this.openCrudService = openCrudService;
//        this.ncrFileProcessingService = ncrFileProcessingService;
//    }
//
//    public void isValidFile(Class<T> clazz) throws OpenFileParserException {
//        this.fileParser.fileTypeValidator();
//
//        this.headerValidator(clazz);
//
//        this.emptyFileValidator();
//    }
//
//    public String processFile(Class<T> clazz) {
//
//        String fileName = clazz.getSimpleName().toLowerCase()
//                + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        String inputFileName = INPUT_FILE_PREFIX + fileName;
//        String outputFileName = OUTPUT_FILE_PREFIX + fileName;
//        String inputMultipartFileName = this.fileParser.getInputMultipartFile().getOriginalFilename();
//        String outputFilePathWithExtn = ApplicationPropertyUtility.getBulkUploadFilePath() + outputFileName + inputMultipartFileName.substring(inputMultipartFileName.lastIndexOf("."));
//        String inputFilePathWithExtn = ApplicationPropertyUtility.getBulkUploadFilePath() + inputFileName + inputMultipartFileName.substring(inputMultipartFileName.lastIndexOf("."));
//        // save multipart file to disk
//        try {
//            OpenFileUtility.saveMultipartFile(this.fileParser.getInputMultipartFile(), ApplicationPropertyUtility.getBulkUploadFilePath(), inputFileName);
//            // create a output file using fileParser.createOutputFile()
//            this.fileParser.createOutputFile(ApplicationPropertyUtility.getBulkUploadFilePath(), outputFileName, inputMultipartFileName.substring(inputMultipartFileName.lastIndexOf(".")));
//            List<String> entityHeader = new ArrayList<>();
//            entityHeader.add(0, REMARK_COLUMN);
//            entityHeader.addAll(this.getHeader(clazz));
//            this.fileParser.appendToFile(entityHeader);
//        } catch (OpenFileParserException e) {
//            // throw error instead of writing it to file as the output file might not have been created
//            log.error("Error when creating file :: " + fileName + " | Exception :: ", e);
//            throw new OpenFileParserException("Error when creating file :: " + fileName);
//        }
//
//        // read the input file
//        BufferedReader br;
//        String eachLine;
//        try {
//            InputStream is = this.fileParser.getInputMultipartFile().getInputStream();
//            br = new BufferedReader(new InputStreamReader(is));
//            int count = 0;
//            List<List<String>> recordList = new LinkedList<>();
//            while ((eachLine = br.readLine()) != null) {
//                List<String> record = new ArrayList<>(Arrays.asList(eachLine.split(",")));
//                log.info("each row fetched for file :: " + this.fileParser.getInputMultipartFile().getName() + " | " + Arrays.toString(record.toArray()));
//                if (count > ApplicationPropertyUtility.getMaxProcessableRecords()) {
//                    log.error("Error Cannot process file more than " + ApplicationPropertyUtility.getMaxProcessableRecords() + " records :: " + fileName);
//                    throw new OpenFileParserException("You cannot process a file with more than "
//                            + ApplicationPropertyUtility.getMaxProcessableRecords() + " records | Please upload the file in smaller batches");
//                }
//                // skip the first record
//                if (count++ == 0) {
//                    continue;
//                }
//
//                recordList.add(record);
//            }
//            AtomicInteger batchIndex = new AtomicInteger(0);
//            CopyOnWriteArrayList<ValidExcelObject<T>> validObjectList = new CopyOnWriteArrayList<>();
//
//            // Recreate the custom thread pool for each run
//            customThreadPool = Executors.newFixedThreadPool(ApplicationPropertyUtility.getCustomThreadPoolSize());
//            List<CompletableFuture<Void>> futures = new LinkedList<>();
//
//            OpenReadApiUtility.batches(recordList, 500)
//                    .forEach((eachRecordList) -> {
//                        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                            int currentBatchIndex = batchIndex.getAndIncrement();
//                            int startIdx = currentBatchIndex * 500;
//                            int endIdx = Math.min(startIdx + 499, startIdx + eachRecordList.size() - 1);
//                            log.info("validating records in batch [{} - {}]", startIdx, endIdx);
//
//                            eachRecordList.forEach((eachRecord) -> {
//                                // validate the number of column (if error append it to the output file)
//                                if (Boolean.FALSE.equals(isValidNumberOfColumn(clazz, eachRecord))) {
//                                    return;
//                                }
//
//                                // validate the column values (if error append it to the output file)
//                                if (Boolean.FALSE.equals(isValidRow(clazz, eachRecord))) {
//                                    return;
//                                }
//                                T object = getObject(clazz, eachRecord);
//                                if (object == null) {
//                                    return;
//                                }
//                                // TODO :: list of ValidExcelObject to map with key as object and value as record
//                                validObjectList.add(new ValidExcelObject(eachRecord, object));
//                            });
//                        }, customThreadPool);
//                        futures.add(future);
//                    });
//
//            // Join all CompletableFuture instances to wait for their completion
//            futures.forEach(CompletableFuture::join);
//
//            this.updateInBatches(validObjectList, clazz);
//
//            // update the input and output file path in the file processing table
//            NcrFileProcessingEntity fileProcessingEntity = new NcrFileProcessingEntity();
//            fileProcessingEntity.setTag(clazz.getSimpleName());
//            fileProcessingEntity.setInputFile(inputFilePathWithExtn);
//            fileProcessingEntity.setOutputFile(outputFilePathWithExtn);
//            fileProcessingEntity.setUploadedBy(JwtTokenUtils.getLoggedInUserId());
//            fileProcessingEntity.setUploadedOn(new Date());
//            this.ncrFileProcessingService.createEntity(fileProcessingEntity);
//            log.info("Sucessfully saved NcrFileProcessingEntity :: " + fileProcessingEntity.toString());
//
//        } catch (OpenFileParserException e) {
//            // throw error instead of writing it to file as the output file might not have been created
//            log.error("Error when processing file :: " + fileName, e);
//            throw new OpenFileParserException(e.getMessage());
//        } catch (Exception e) {
//            log.info("processFile exception :: " + e);
//            throw new OpenFileParserException("Failed during processing file");
//        } finally {
//            // Shutdown the custom thread pool when done
//            if (customThreadPool != null) {
//                customThreadPool.shutdown();
//            }
//        }
//        return outputFilePathWithExtn;
//    }
//
//    private void updateInBatches(List<ValidExcelObject<T>> validObjectList, Class<T> clazz) {
//        AtomicInteger batchIndex = new AtomicInteger(0);
//
//        List<CompletableFuture<Void>> futures = new LinkedList<>();
//
//        // save the valid object in batches        
//        OpenReadApiUtility.batchesOfValidExcelObject(validObjectList, 100)
//                //                .parallel()
//                .forEach((batchList) -> {
//                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//
//                        List<ValidExcelObject<T>> objectInBatches = batchList.stream().collect(Collectors.toList());
//                        try {
//                            int currentBatchIndex = batchIndex.getAndIncrement();
//                            int startIdx = currentBatchIndex * 100;
//                            int endIdx = Math.min(startIdx + 99, validObjectList.size() - 1);
//
//                            log.info("Processing update in batch [{} - {}]", startIdx, endIdx);
//                            List<T> entityListWithAnnotatedAttributes = new LinkedList<>();
//                            List<T> entityList = objectInBatches.stream()
//                                    .map(obj -> {
//                                        entityListWithAnnotatedAttributes.add(OpenReadApiUtility.keepAnnotatedFields(obj.getT()));
//                                        return obj.getT();
//                                    })
//                                    .collect(Collectors.toList());
//                            SimplePage<T> pageList = this.openCrudService.findByValue(entityListWithAnnotatedAttributes, Pageable.unpaged(), Boolean.FALSE);
//                            List<T> entityListFromDB = pageList.getContent();
//
//                            // convert the list to a map
//                            Map<T, T> map = T.convertSetToHashMap(entityListFromDB);
//
//                            // check if the zipcode is present in the db or not
//                            List<ValidExcelObject<T>> recordNotFoundObject = objectInBatches.stream()
//                                    .filter(eachValidObject
//                                            -> !map.containsKey(eachValidObject.getT()))
//                                    .map(eachValidObject -> {
//                                        eachValidObject.getRecord().add(0, "Record not found!");
//                                        this.fileParser.appendToFile(eachValidObject.getRecord());
//                                        return new ValidExcelObject<>(Collections.emptyList(), eachValidObject.getT());
//                                    })
//                                    .collect(Collectors.toList());
//
//                            objectInBatches.removeAll(recordNotFoundObject);
//
//                            List<T> updatedDbEntityWithValueFromFile = entityList.stream()
//                                    .filter(eachEntity
//                                            -> map.containsKey(eachEntity))
//                                    .map(eachEntity
//                                            -> this.updateObject(map.get(eachEntity), eachEntity))
//                                    .map(updatedEntity -> {
//                                        // update the audit fields too
//                                        if (updatedEntity.updatedByColumn() != null
//                                                && !updatedEntity.updatedByColumn().isEmpty()) {
//                                            setValueViaReflection(clazz, updatedEntity, updatedEntity.updatedByColumn(), String.valueOf(JwtTokenUtils.getLoggedInUserId()));
//                                        }
//                                        if (updatedEntity.updatedOnColumn() != null
//                                                && !updatedEntity.updatedOnColumn().isEmpty()) {
//                                            setValueViaReflection(clazz, updatedEntity, updatedEntity.updatedOnColumn(), new Date().toString());
//                                        }
//                                        return updatedEntity;
//                                    })
//                                    .collect(Collectors.toList());
//                            this.openCrudService.updateEntity(updatedDbEntityWithValueFromFile);
//                            this.appendToFile(objectInBatches, "Success");
//                        } catch (Exception e) {
//                            log.error("error when saving object :: ", e);
//                            // append error to this list
//                            this.appendToFile(objectInBatches, e.getMessage());
//                        }
//                    }, this.customThreadPool);
//                    futures.add(future);
//                });
//
//        // Join all CompletableFuture instances to wait for their completion
//        futures.forEach(CompletableFuture::join);
//
//    }
//
//    private void appendToFile(List<ValidExcelObject<T>> validExcelObjectList, String message) {
//        for (ValidExcelObject<T> eachValidExcelObject : validExcelObjectList) {
//            eachValidExcelObject.getRecord().add(0, message);
//            this.fileParser.appendToFile(eachValidExcelObject.getRecord());
//        }
//    }
//
//    private T getObject(Class<T> clazz, List<String> record) {
//        T entity = null;
//        try {
//            entity = this.convertRowToObject(clazz, record);
//        } catch (Exception e) {
//            log.error("Exception when converting row to object :: " + e);
//            record.add(0, e.getMessage());
//            this.fileParser.appendToFile(record);
//        }
//        return entity;
//    }
//
//    private Boolean isValidNumberOfColumn(Class<T> clazz, List<String> row) throws OpenFileParserException {
//        List<String> entityHeader = this.getHeader(clazz);
//        if (entityHeader.size() != row.size()) {
//            row.add(0, "Number of columns is not matched | Expected :: " + entityHeader.size()
//                    + " | Actual :: " + row.size());
//            this.fileParser.appendToFile(row);
//            return Boolean.FALSE;
//        }
//        return Boolean.TRUE;
//    }
//
//    private void headerValidator(Class<T> clazz) {
//        // get header from the file
//        List<String> entityHeader = this.getHeader(clazz);
//        List<String> headerRecord = OpenFileUtility.getHeaderRecord(this.fileParser.getInputMultipartFile());
//        // check the file header matches with the entity headers
//        for (String eachHeader : entityHeader) {
//            if (!headerRecord.contains(eachHeader)) {
//                log.info("file :: " + this.fileParser.getInputMultipartFile().getName()
//                        + " | Header not found in file :: " + eachHeader);
//                throw new OpenFileParserException("file :: " + this.fileParser.getInputMultipartFile().getName()
//                        + " | Header not found in file :: " + eachHeader);
//            }
//        }
//    }
//
//    private List<String> getHeader(Class<T> clazz) {
//        List<String> entityHeader = new ArrayList<>();
//        try {
//            entityHeader = clazz.newInstance().getTableHeaderNames();
//        } catch (IllegalAccessException | InstantiationException e) {
//            log.info(e.getMessage());
//        } catch (Exception e) {
//            log.info(e.getMessage());
//        }
//        return new ArrayList<>(entityHeader);
//    }
//
//    private void emptyFileValidator() {
//        // check if the file contains atleast one record other than header.
//        if (!OpenFileUtility.hasAtleastOneRecord(this.fileParser.getInputMultipartFile())) {
//            throw new OpenFileParserException("File :: " + this.fileParser.getInputMultipartFile().getName() + " does not contain records");
//        }
//    }
//
//    private Boolean isValidRow(Class<T> clazz, List<String> record) {
//        List<String> entityHeader = this.getHeader(clazz);
//        try {
//            T entity = this.convertRowToObject(clazz, record);
//            // validate the object
//            this.validateObject(entity);
//        } catch (Exception e) {
//            record.add(0, e.getMessage());
//            this.fileParser.appendToFile(record);
//            return Boolean.FALSE;
//        }
//        return Boolean.TRUE;
//    }
//
//    private void validateObject(T object) {
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Set<ConstraintViolation<T>> violations = validator.validate(object);
//        if (!violations.isEmpty()) {
//            StringBuilder errorMessage = new StringBuilder("Validation errors | ");
//            for (ConstraintViolation<T> violation : violations) {
//                errorMessage.append("Column Name :: ")
//                        .append(violation.getPropertyPath())
//                        .append(" :: ")
//                        .append(violation.getMessage())
//                        .append(" | ");
//                break;
//            }
//            throw new OpenFileParserException(errorMessage.toString());
//        }
//    }
//
//    private T convertRowToObject(Class<T> clazz, List<String> record) {
//        T entity = null;
//        List<String> entityHeader = this.getHeader(clazz);
//        try {
//            entity = clazz.newInstance();
//            for (int column = 0; column < record.size(); column++) {
//                this.setValueViaReflection(clazz, entity, entityHeader.get(column), record.get(column));
//            }
//            // validate the object
//            return entity;
//        } catch (OpenFileParserException ex) {
//            log.info("OpenFileParserException while convertRowToObject :: record :: " + Arrays.toString(record.toArray()) + " | " + ex);
//            throw new OpenFileParserException(ex.getMessage());
//        } catch (Exception ex) {
//            log.info("Exception while convertRowToObject :: record :: " + Arrays.toString(record.toArray()) + " | " + ex);
//            throw new OpenFileParserException("Exception when converting record to object!");
//        }
//    }
//
//    private void setValueViaReflection(Class<T> clazz, T entityObj, String excelColumnName, String cellValue) {
//        Class<?> entityClass = clazz;
//        try {
//            if (cellValue == null || cellValue.isEmpty()) {
//                return;
//            }
//            String methodName = "set" + this.capitalize(excelColumnName); //setFccode
//            Field field = entityClass.getDeclaredField(excelColumnName);
//            field.setAccessible(true);
//            Method method = entityClass.getMethod(methodName, field.getType());
//            // if field type is integer and then the value is string then throw exception            
//            if (field.getType().equals(Integer.class)) {
//                method.invoke(entityObj, Integer.valueOf(cellValue));
//            } else if (field.getType().equals(Date.class)) {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
//                Date date = dateFormat.parse(cellValue);
//                method.invoke(entityObj, date);
//            } else if (field.getType().equals(Boolean.class)) {
//                if (!cellValue.equalsIgnoreCase("true") && !cellValue.equalsIgnoreCase("false")) {
//                    throw new OpenFileParserException("The accepted value must be either true or false | for column : " + excelColumnName + " | value : " + cellValue);
//                }
//                method.invoke(entityObj, Boolean.valueOf(cellValue));
//            } else {
//                method.invoke(entityObj, cellValue);
//            }
//
//        } catch (NumberFormatException e) {
//            log.info("NumberFormatException while setting value for column : {} | value : {} | exception : {}", excelColumnName, cellValue, e.getMessage());
//            throw new OpenFileParserException("ColumnName :: " + excelColumnName
//                    + " | value :: " + cellValue + " is not a integer!");
//        } catch (OpenFileParserException e) {
//            log.info("OpenFileParserException :: {} ", e.getMessage());
//            throw new OpenFileParserException(e.getMessage());
//        } catch (Exception e) {
//            log.info("Exception while setting value for column : {} | value : {} | exception : {}", excelColumnName, cellValue, e);
//            throw new OpenFileParserException("Exception while setValueViaReflection!");
//        }
//    }
//
//    private String capitalize(String str) {
//        if (str == null || str.isEmpty()) {
//            return str;
//        }
//        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
//    }
//
//    private T updateObject(T entityFromDb, T entity) {
//        Class<?> clazz = entityFromDb.getClass();
//        Field[] fields = clazz.getDeclaredFields();
//
//        for (Field field : fields) {
//            try {
//                if (field.getName().equalsIgnoreCase("serialVersionUID")) {
//                    continue;
//                }
//                field.setAccessible(true);
//                Object fieldValue = field.get(entity);
//                if (fieldValue != null && !OpenReadApiUtility.hasNaturalIdAnnotation(field)) {
//                    field.set(entityFromDb, fieldValue);
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace(); // Handle the exception appropriately
//            }
//        }
//        return entityFromDb;
//    }

}
