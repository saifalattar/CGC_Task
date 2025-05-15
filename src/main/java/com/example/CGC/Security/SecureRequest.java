package com.example.CGC.Security;

import com.example.CGC.CGCExceptions.CGCExceptions;
import com.example.CGC.Schemas.LogData;
import com.example.CGC.Shared.Properties;
import com.example.CGC.Shared.SharedFunctions;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.*;

@Component
public class SecureRequest extends OncePerRequestFilter {
    final private HandlerExceptionResolver handlerExceptionResolver;
    final private Properties properties;
    @Autowired
    private CGCExceptions CGCExceptions;
    private Logger log;

    SecureRequest(HandlerExceptionResolver handlerExceptionResolver, @Qualifier("Exceptions") CGCExceptions CGCExceptions, Properties properties){
        this.CGCExceptions = CGCExceptions;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        log = LoggerFactory.getLogger(wrappedRequest.getServletPath());


        ArrayList<String> disallowedHeaders = new ArrayList<>();
        LinkedHashMap allowedHeaders = (LinkedHashMap)properties.getProperty("allowedHeaders");
        HashMap<String, String> requestHeaders = new HashMap<String, String>();

        wrappedRequest.getHeaderNames().asIterator().forEachRemaining(x->{
            requestHeaders.put(x, wrappedRequest.getHeader(x));
            if(!allowedHeaders.containsValue(x)){
                disallowedHeaders.add(x);
            }
        });

        try{
            // Check if the requested endpoint was NOT from the OAuth2 request and the decoded token was INVALID.
            //to know that if you don't requesting OAuth token and if you were and the token was invalid, an exception to unauthorized will be thrown.
            if(!wrappedRequest.getServletPath().equals("/api/v1.0.0/oauth/generate") && !SharedFunctions.isOAuthTokenValid(requestHeaders.get("authorization").substring(7))){
                try {
                    throw CGCExceptions.setErrorKey("UnAuthorized");
                } catch (CGCExceptions e) {
                    handlerExceptionResolver.resolveException(wrappedRequest, responseWrapper, null, e);
                }
            }else{
                // if he was requesting any other endpoint or the token of OAuth was valid will check on disallowed headers.
                if(!disallowedHeaders.isEmpty()){
                    try {
                        log.error("Disallowed headers :  {}" ,disallowedHeaders);
                        throw CGCExceptions.setErrorKey("UnAuthorized");
                    } catch (CGCExceptions e) {
                        handlerExceptionResolver.resolveException(wrappedRequest, responseWrapper, null, e);
                    }
                }else{
                    filterChain.doFilter(wrappedRequest, responseWrapper);
                }
            }
        }catch (Exception e){
            try {
                throw CGCExceptions.setErrorKey("UnAuthorized");
            } catch (CGCExceptions ee) {
                handlerExceptionResolver.resolveException(wrappedRequest, responseWrapper, null, ee);
            }
        }


        log.info("REQUEST BODY:  " +  wrappedRequest.getContentAsString());
        log.info("REQUEST HEADERS :  "+requestHeaders);
        log.info("REQUEST QUERY PARAMETERS :  "+ wrappedRequest.getQueryString());

        for (LogData data : SharedFunctions.getLogs()){
            switch (data.typeOfLog()){
                case "INFO":
                    log.info(data.msg());
                    break;
                case "DEBUG":
                    log.debug(data.msg());
                    break;
                case "ERROR":
                    log.error(data.msg());
                    break;
                default:
                    log.trace(data.msg());
                    break;
            }
        }
        log.info("FINAL RESPONSE :  " + new String(responseWrapper.getContentAsByteArray()));
        responseWrapper.copyBodyToResponse();

    }
}
