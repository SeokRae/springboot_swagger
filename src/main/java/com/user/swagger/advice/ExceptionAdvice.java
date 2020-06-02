package com.user.swagger.advice;

import com.user.swagger.advice.exception.CPostsNotFoundException;
import com.user.swagger.advice.exception.CUserNotFoundException;
import com.user.swagger.config.response.CommonResult;
import com.user.swagger.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult();
    }

    @ExceptionHandler(CPostsNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult postsNotFoundException(HttpServletRequest request, CPostsNotFoundException e) {
        return responseService.getFailResult();
    }
}