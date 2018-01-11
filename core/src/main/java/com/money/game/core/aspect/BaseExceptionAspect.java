package com.money.game.core.aspect;

import com.money.game.core.constant.ResponseData;
import com.money.game.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * conan
 * 2017/10/12 11:10
 **/
@Slf4j
public class BaseExceptionAspect {

    /**
     * 打印异常日志并return ResponseEntity对象,controller层切入
     */
    public static Object printExcLogAndReturn(JoinPoint joinPoint) {
        try {
            return ((ProceedingJoinPoint) joinPoint).proceed();
        } catch (Throwable e) {
            return ResponseData.failure(execLog(e));
        }
    }

    /**
     * 打印异常日志直接抛出异常,业务层切入
     */
    public static Object printExcLogAndThrow(JoinPoint joinPoint) {
        try {
            return ((ProceedingJoinPoint) joinPoint).proceed();
        } catch (Throwable e) {
            throw execLog(e);
        }
    }

    private static BaseException execLog(Throwable e) {
        BaseException bizEx;
        if (e instanceof BaseException) {
            bizEx = new BaseException(((BaseException) e).getCode(), e.getMessage());
            log.warn("业务异常,errorCode={},errorMessge={},", bizEx.getCode(), bizEx.getMessage(), e);
        } else {
            bizEx = new BaseException("-1", "系统异常");
            log.error("系统异常", e);
        }
        return bizEx;
    }
}
