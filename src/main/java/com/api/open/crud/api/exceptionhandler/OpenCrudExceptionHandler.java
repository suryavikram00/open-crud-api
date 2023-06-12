package com.api.open.crud.api.exceptionhandler;

import com.api.open.read.api.enums.StatusEnum;
import com.api.open.read.api.exception.OpenReadApiException;
import com.api.open.read.api.exceptionhandler.OpenReadApiExceptionHandler;
import com.api.open.read.api.model.CrudApiResponse;
import com.api.open.read.api.model.CrudApiResponseValidationError;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class OpenCrudExceptionHandler extends OpenReadApiExceptionHandler {

}
