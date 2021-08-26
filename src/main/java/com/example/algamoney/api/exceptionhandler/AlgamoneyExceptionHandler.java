package com.example.algamoney.api.exceptionhandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        String userMessage = messageSource.getMessage("invalid.message", null, LocaleContextHolder.getLocale());
        String devMessage = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
        List<Erro> errors = Arrays.asList(new Erro(userMessage, devMessage));
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Erro> erros = createErrorList(ex.getBindingResult());
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request){
        String userMessage = messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
        String devMessage = ex.toString();
        List<Erro> errors = Arrays.asList(new Erro(userMessage, devMessage));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){
        String userMessage = messageSource.getMessage("resource.operation-not-permited", null, LocaleContextHolder.getLocale());
        String devMessage = ExceptionUtils.getRootCauseMessage(ex);
        List<Erro> errors = Arrays.asList(new Erro(userMessage, devMessage));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({JpaObjectRetrievalFailureException.class})
    public ResponseEntity<Object> handleJpaObjectRetrievalFailureException(JpaObjectRetrievalFailureException ex, WebRequest request){
        String userMessage = messageSource.getMessage("resource.operation-not-permited", null, LocaleContextHolder.getLocale());
        String devMessage = ExceptionUtils.getRootCauseMessage(ex);
        List<Erro> errors = Arrays.asList(new Erro(userMessage, devMessage));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(NoSuchElementException ex, WebRequest request){
        String userMessage = messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
        String devMessage = ex.toString();
        List<Erro> errors = Arrays.asList(new Erro(userMessage, devMessage));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private List<Erro> createErrorList(BindingResult bindingResult){
        List<Erro> errors = new ArrayList<>();

        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            String userMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String devMessage = fieldError.toString();
            errors.add(new Erro(userMessage, devMessage));
        }

        return errors;
    }

    public static class Erro {

        private String userMessage;
        private String devMessage;

        public Erro(String userMessage, String devMessage) {
            this.userMessage = userMessage;
            this.devMessage = devMessage;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public String getDevMessage() {
            return devMessage;
        }

    }

}