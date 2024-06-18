package december.spring.studywithme.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Request Logging")
@Aspect
@Component
@RequiredArgsConstructor
public class ControllerAOP {
	private final HttpServletRequest request;
	
	@Pointcut("execution(* december.spring.studywithme.controller.*.*(..))")
	private void controller() {}
	
	@Around("controller()")
	public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
		String method = request.getMethod();
		String requestURI = request.getRequestURI();
		log.info("Request URL: {}, HTTP Method: {}", requestURI, method);
		
		return joinPoint.proceed();
	}
}
