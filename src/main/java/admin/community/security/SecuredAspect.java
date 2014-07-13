package admin.community.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import admin.community.SessionData;
import admin.community.exceptions.PermissionDenied;

@Aspect
@Component
public class SecuredAspect {
 
	@Before(value="@annotation(admin.community.security.Secured)&& @annotation(sc)", argNames = "sc")
	public void secure(JoinPoint joinPoint,Secured sc) {		
		if(SessionData.getUser()==null) throw new PermissionDenied();
		System.out.println("Secure only for:"+sc.value()+" "+SessionData.getUser());
		System.out.println(SessionData.getUser().canAccess(sc.value()));
		if(!SessionData.getUser().canAccess(sc.value())) throw new PermissionDenied();
		
	}
 
}