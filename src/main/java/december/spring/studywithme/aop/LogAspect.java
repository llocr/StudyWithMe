package december.spring.studywithme.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final HttpServletRequest httpServletRequest;

    @Pointcut("execution(* december.spring.studywithme..*Controller.*(..))")
    public void controller() {
    }

    @Around("controller()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String url = httpServletRequest.getRequestURI();
        String httpMethod = httpServletRequest.getMethod();
        log.info("HTTP Method: {}, Request URL: {}", httpMethod, url);

        Object result = joinPoint.proceed();

        return result;
    }
}