package com.api.open.crud.api.exceptionhandler;

import com.api.open.crud.api.exception.OpenCrudApiException;
import com.api.open.crud.api.exception.enums.StatusEnum;
import com.api.open.crud.api.exception.model.CrudApiResponse;
import com.api.open.crud.api.exception.model.CrudApiResponseValidationError;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class OpenCrudExceptionHandler {

    @ExceptionHandler(value = OpenCrudApiException.class)
    public ResponseEntity<CrudApiResponse<String>> openReadApiExceptionHandler(OpenCrudApiException exception, WebRequest request) {
        String message = "Crud Runtime Exception :: " + exception.getLocalizedMessage();
        return buildResponseEntity(new CrudApiResponse<String>(StatusEnum.FAILURE).addMessage(message).addDebugMessage(exception));
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CrudApiResponse<String>> constraintViolationExceptionHandler(
            SQLIntegrityConstraintViolationException ex, WebRequest request) {
        String message = "Data Integrity Violation";
        CrudApiResponse<String> crudResponse = new CrudApiResponse<>(StatusEnum.FAILURE);
        crudResponse.setMessage("Data is not valid");
        crudResponse.setDebugMessage(ex.getLocalizedMessage());

        return buildResponseEntity(new CrudApiResponse<String>(StatusEnum.FAILURE)
                .addMessage(ex.getMessage() != null ? ex.getMessage() : message).addDebugMessage(ex));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<CrudApiResponse<String>> constraintViolationExceptionHandler(ConstraintViolationException ex,
            WebRequest request) {
        StringBuffer message = new StringBuffer("Data is not valid");
        CrudApiResponse<String> crudApiResponse = new CrudApiResponse<>(StatusEnum.FAILURE);

        List<CrudApiResponseValidationError> validationErrorList = ex.getConstraintViolations().stream().map(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getMessage();
            message.append(" || " + fieldName + ":" + errorMessage);
            return new CrudApiResponseValidationError(fieldName, errorMessage);
        }).collect(Collectors.toList());

        crudApiResponse.setMessage(message.toString());
        crudApiResponse.setDebugMessage(message.toString());

        crudApiResponse.setErrorList(validationErrorList);

        return buildResponseEntity(new CrudApiResponse<String>(StatusEnum.FAILURE)
                .addMessage(ex.getMessage() != null ? ex.getMessage() : message.toString()).addDebugMessage(ex));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<CrudApiResponse<String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {

        CrudApiResponse<String> crudApiResponse = new CrudApiResponse<>(StatusEnum.FAILURE);

        StringBuffer message = new StringBuffer("Data is not valid");

        crudApiResponse.setErrorList(ex.getBindingResult().getAllErrors().stream().map((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            message.append(" || " + fieldName + ":" + errorMessage);
            return new CrudApiResponseValidationError(fieldName, errorMessage);
        }).collect(Collectors.toList()));

        crudApiResponse.setMessage(message.toString());
        crudApiResponse.setDebugMessage(message.toString());

        return buildResponseEntity(crudApiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CrudApiResponse<String>> genericExceptionHandler(Exception exception, WebRequest request) {
        log.info("Logging The exception {} ", exception);
        String message = "Something went wrong, Please contact the support team | " 
                            + (exception.getLocalizedMessage() != null ? exception.getMessage() : "");
        return buildResponseEntity(new CrudApiResponse<String>(StatusEnum.FAILURE).addMessage(message).addDebugMessage(exception));
    }

    private ResponseEntity<CrudApiResponse<String>> buildResponseEntity(CrudApiResponse<String> crudApiResponse) {
        return new ResponseEntity<>(crudApiResponse, HttpStatus.ACCEPTED);
    }

}
