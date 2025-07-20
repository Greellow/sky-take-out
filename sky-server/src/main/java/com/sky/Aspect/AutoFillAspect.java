package com.sky.Aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始公共字段填充");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法上的注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        //获取操作类型
        OperationType opeartionType = autoFill.value();

        //获取被拦截的方法参数
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object arg = args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        if(opeartionType == OperationType.INSERT){
            try {
                Method setCreateTime = arg.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setUpdateTime = arg.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setCreateUser = arg.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateUser = arg.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                setCreateTime.invoke(arg, now);
                setUpdateTime.invoke(arg, now);
                setCreateUser.invoke(arg, BaseContext.getCurrentId());
                setUpdateUser.invoke(arg, BaseContext.getCurrentId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if (opeartionType == OperationType.UPDATE){
            try {
               // Method setCreateTime = arg.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setUpdateTime = arg.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                //Method setCreateUser = arg.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateUser = arg.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                //setCreateTime.invoke(arg, now);
                setUpdateTime.invoke(arg, now);
                //setCreateUser.invoke(arg, BaseContext.getCurrentId());
                setUpdateUser.invoke(arg, BaseContext.getCurrentId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
