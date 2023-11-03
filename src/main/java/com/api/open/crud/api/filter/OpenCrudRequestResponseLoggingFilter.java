/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.open.crud.api.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 *
 * @author NMSLAP570
 */
@Slf4j
@Component
@Order(1)
public class OpenCrudRequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = null;
        ContentCachingResponseWrapper responseWrapper = null;
        long startTime = 0;
        long timeTaken = 0;
        try {
            requestWrapper = new ContentCachingRequestWrapper(request);
            responseWrapper = new ContentCachingResponseWrapper(response);
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            log.info("error in doFilterInternal :: ", e);
        }

        filterChain.doFilter(requestWrapper, responseWrapper);

        try {

            timeTaken = System.currentTimeMillis() - startTime;
            String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
                    request.getCharacterEncoding());
            String responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
                    response.getCharacterEncoding());
            log.info(
                    "FINISHED PROCESSING : METHOD={}; REQUESTURI={}; REQUEST PAYLOAD={}; RESPONSE CODE={}; RESPONSE={}; TIM TAKEN={}",
                    request.getMethod(), request.getRequestURI(), requestBody, response.getStatus(), responseBody,
                    timeTaken);
            responseWrapper.copyBodyToResponse();
        } catch (IOException e) {
            log.info("IOException in doFilterInternal :: ", e);
        } catch (Exception e) {
            log.info("Exception in doFilterInternal :: ", e);
        }
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            log.info("error in getStringValue :: ", e);
        }
        return "";
    }
}
