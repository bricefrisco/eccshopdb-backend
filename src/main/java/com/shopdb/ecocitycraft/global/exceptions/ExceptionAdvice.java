package com.shopdb.ecocitycraft.global.exceptions;

import com.shopdb.ecocitycraft.security.models.exceptions.AuthenticationException;
import com.shopdb.ecocitycraft.security.models.exceptions.AuthorizationException;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.AlreadyExistentException;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.NotFoundException;
import com.shopdb.ecocitycraft.shopdb.models.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     * Authorization Exceptions
     */

    @ExceptionHandler({AuthenticationException.class, AuthorizationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(Exception ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(Exception ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * ShopDB Exceptions
     */

    @ExceptionHandler(BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse onBindException(BindException ex) {
        List<String> errorList = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError) ->
                errorList.add(fieldError.getObjectName() + "." + fieldError.getField() + ": " + fieldError.getDefaultMessage()));

        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                String.join(" | ", errorList));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse onConstraintViolationException(ConstraintViolationException ex) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ErrorResponse onUnauthorizedException(UnauthorizedException ex) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse onMissingPathVariableException(MissingPathVariableException ex) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse onHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorList = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError) ->
                errorList.add(fieldError.getObjectName() + "." + fieldError.getField() + ": " + fieldError.getDefaultMessage()));
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                String.join(" | ", errorList)
        );
    }

    @ExceptionHandler(AlreadyExistentException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse onAlreadyExistentException(AlreadyExistentException ex) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse onNotFoundException(NotFoundException ex) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onException(Exception ex) {
        LOGGER.error("Exception occurred:", ex);
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage());
    }

    private ErrorResponse createErrorResponse(String message, HttpStatus status) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()), status.value(), status.getReasonPhrase(), message);
    }
}
