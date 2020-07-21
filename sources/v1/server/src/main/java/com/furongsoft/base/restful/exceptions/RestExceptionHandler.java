package com.furongsoft.base.restful.exceptions;

import com.furongsoft.base.misc.Constants;
import com.furongsoft.base.misc.MessageUtil;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Restful异常拦截器
 *
 * @author Alex
 */
@RestControllerAdvice
public class RestExceptionHandler {
    /**
     * SHIRO异常
     *
     * @param request 请求内容
     * @param e       异常内容
     * @return 响应内容
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public RestResponse handle401(HttpServletRequest request, ShiroException e) {
        if (e instanceof IncorrectCredentialsException || e instanceof UnknownAccountException) {
            return new RestResponse(HttpStatus.UNAUTHORIZED.value(), MessageUtil.message(Constants.USERNAME_OR_PASSWORD_ERROR), null);
        }
        return new RestResponse(HttpStatus.UNAUTHORIZED.value(), MessageUtil.message(Constants.UNAUTHORIZED), null);
    }

    /**
     * 未授权异常
     *
     * @return 响应内容
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public RestResponse handle401() {
        Tracker.error("Unauthorized");
        return new RestResponse(HttpStatus.UNAUTHORIZED.value(), MessageUtil.message(Constants.UNAUTHORIZED), null);
    }

    /**
     * 通用异常
     *
     * @param request 请求内容
     * @param ex      异常内容
     * @return 响应内容
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public RestResponse globalException(HttpServletRequest request, Throwable ex) {
        Tracker.error(ex);
        return new RestResponse(getStatus(request).value(), MessageUtil.message(ex.getMessage()), null);
    }

    /**
     * 参数validation错误
     *
     * @param ex      异常
     * @param request 请求信息
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {RepositoryConstraintViolationException.class})
    public RestResponse handleRepositoryConstraintViolationException(Exception ex, HttpServletRequest request) {
        RepositoryConstraintViolationException repositoryConstraintViolationException = (RepositoryConstraintViolationException) ex;
        List<ObjectError> list = repositoryConstraintViolationException.getErrors().getAllErrors();
        if (list.size() > 0) {
            return new RestResponse(getStatus(request).value(), MessageUtil.message(list.get(0).getDefaultMessage()), null);
        }
        return new RestResponse(HttpStatus.OK.value(), MessageUtil.message(Constants.ERROR), null);
    }

    /**
     * 参数validation错误
     *
     * @param ex      异常
     * @param request 请求信息
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {BindException.class})
    public RestResponse handleBindException(Exception ex, HttpServletRequest request) {
        BindingResult errorResult = (BindingResult) ex;
        if (errorResult.hasErrors()) {
            List<ObjectError> list = errorResult.getAllErrors();
            if (list.size() > 0) {
                return new RestResponse(getStatus(request).value(), MessageUtil.message(list.get(0).getDefaultMessage()), null);
            }
        }
        return new RestResponse(HttpStatus.OK.value(), MessageUtil.message(Constants.ERROR), null);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public RestResponse handleMethodArgumentNotValidException(Exception ex, HttpServletRequest request) {
        MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
        BindingResult errorResult = methodArgumentNotValidException.getBindingResult();
        if (errorResult.hasErrors()) {
            List<ObjectError> list = errorResult.getAllErrors();
            if (list.size() > 0) {
                return new RestResponse(getStatus(request).value(), MessageUtil.message(list.get(0).getDefaultMessage()), null);
            }
        }
        return new RestResponse(HttpStatus.OK.value(), MessageUtil.message(Constants.ERROR), null);
    }

    /**
     * 获取HTTP状态码
     *
     * @param request HTTP请求
     * @return HTTP状态码
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        return (statusCode == null) ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.valueOf(statusCode);
    }
}
