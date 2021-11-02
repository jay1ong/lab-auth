package com.im.auth.controller;

import com.im.auth.exception.ApiMessage;
import com.im.auth.exception.BizException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

@org.springframework.web.bind.annotation.ControllerAdvice
public class RestControllerAdvice implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<?> bizException(BizException ex, WebRequest request) {
        ApiMessage message = new ApiMessage();
        message.setCode(ex.getCode());
        message.setTimestamp(LocalDateTime.now());
        message.setMessage(ex.getMessage());
        message.setDescription(request.getDescription(false));
        return ResponseEntity.ok(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ApiMessage message = new ApiMessage();
        message.setCode("11000");
        message.setTimestamp(LocalDateTime.now());
        message.setMessage(ex.getMessage());
        message.setDescription(request.getDescription(false));
        return ResponseEntity.ok(message);
    }

    @Override
    public boolean supports(@Nonnull MethodParameter returnType,
                            @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @Nonnull MethodParameter returnType,
                                  @Nonnull MediaType selectedContentType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nonnull ServerHttpRequest request,
                                  @Nonnull ServerHttpResponse response) {
        if (body instanceof ApiMessage) {
            return body;
        }
        ApiMessage message = new ApiMessage();
        message.setCode("200");
        message.setTimestamp(LocalDateTime.now());
        message.setData(body);
        return message;
    }
}
