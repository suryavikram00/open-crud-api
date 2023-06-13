package com.api.open.crud.api.exceptionhandler;

import com.api.open.read.api.exceptionhandler.OpenReadApiExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class OpenCrudExceptionHandler extends OpenReadApiExceptionHandler {

}
