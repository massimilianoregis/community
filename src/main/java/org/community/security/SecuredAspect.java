package org.community.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.community.SessionData;
import org.community.exceptions.PermissionDenied;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class SecuredAspect {
 
	@Before(value="@annotation(org.community.security.Secured)&& @annotation(sc)", argNames = "sc")
	public void secure(JoinPoint joinPoint,Secured sc) {		
		if(SessionData.getUser()==null) throw new PermissionDenied();
		
		String company = sc.company().isEmpty()?null:sc.company();
		System.out.println("Secure only for:"+sc.value()+" "+SessionData.getUser());		
		if(!SessionData.getUser().canAccess(sc.value(),company)) throw new PermissionDenied();
		
	}
 
}