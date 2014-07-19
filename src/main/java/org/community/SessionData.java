package org.community;

import javax.servlet.http.HttpSession;

public class SessionData {
	
static private ThreadLocal<HttpSession> sess = new ThreadLocal<HttpSession>();
static public void setSession(HttpSession session)
	{
	sess.set(session);
	}
static public HttpSession getSession()
	{
	return sess.get();
	}
static public User getUser()
	{	
	return (User)getSession().getAttribute("user");
	}
static public void setUser(User user)
	{
	getSession().setAttribute("user", user);
	}
}
