 	package org.community.filter;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.community.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class InitFilter implements Filter 
	{
	@Autowired	
	private Admin admin;
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException 
	{
		String uri = ((HttpServletRequest)req).getRequestURI();
		
		if(admin.isVirgin() && uri.matches(".*html") && !uri.equals("/init.html"))
			{		
			req.getRequestDispatcher("/init.html").forward(req, res);
			return;
			}
		chain.doFilter(req, res);
		
	}

	public void init(FilterConfig filterConfig) {}

	public void destroy() {}
	
}