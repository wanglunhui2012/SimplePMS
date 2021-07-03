package indi.simple.pms.handler;

import indi.simple.pms.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:22
 * @Description: 全局返回和异常处理，都使用了ResponseEntity，就不需要再封装了
 */
@RestControllerAdvice
@Slf4j
public class GlobalReturnAndErrorHandler /*extends ResponseEntityExceptionHandler*//*implements ResponseBodyAdvice<Object> */{
    /*@Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return body;
    }*/

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity handleAuthorizationException(BadRequestException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity handlerNoFoundException(NoHandlerFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError=(FieldError)e.getBindingResult().getAllErrors().get(0);

        String field=fieldError.getField();
        String message=fieldError.getDefaultMessage();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).body(field+":"+message);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity handlerConstraintViolationException(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).body(e.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity handleMissingServletRequestParameterException(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).body(e.getMessage());
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity globalExceptionHandle(HttpServletRequest request, HttpServletResponse response, Throwable e) throws Exception {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(e.getMessage());
    }
}