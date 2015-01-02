package org.opencommunity.objs;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.opencommunity.exception.UserJustPresent;
import org.opencommunity.exception.UserNotFound;
import org.opencommunity.persistence.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

//@Entity(name="Communities")
public class Community 
	{	
	//@Id			
	private String name;
	private String root;	
	//@OneToOne(cascade=CascadeType.ALL) 
	private Envelope welcome;
	private String secretKey;
	
	@Autowired
	@Transient
	@JsonIgnore
	private SessionData session;
	
	static private Community instance;
	static public Community getInstance()
		{
		return instance;
		}
	public Community()
		{
		this.instance=this;
		}
	public Community(String name,String root, Envelope welcome,String admin)
		{
		this(name,root,welcome,admin,null);
		}
	public Community(String name,String root, Envelope welcome,String admin,String psw)
		{
		Community.instance=this;
		this.root=root;
		this.welcome=welcome;
		this.name=name;
		this.secretKey=UUID.randomUUID().toString();
			addRole(Role.ADMIN);
			addRole(Role.USER);
		try{
			User user = addUser(admin);						 
			 	 user.addRole(getAdminRole());
			 	 user.addRole(getUserRole());
			 	 user.setPassword(psw);
			 	 user.save();
			 	 user.sendPassword();
			}
		catch(UserJustPresent e){}
		
		}
	
	
	public String getName() 		{return name;}
	public String getRoot() 		{return root;}
	public Envelope getWelcome() 	{return welcome;}
	
	
	public User addUser(String mail) throws UserJustPresent
		{
		if(Repositories.user.exists(mail)) throw new UserJustPresent();
		User user = new User(mail,this, new File(this.root,mail));			
			
		return user;
		}
	
	
	public User me()				{return session.getUser();}
	
	
	public void logout()			{session.setUser(null);}
	
	
	public User login(String mail, String psw) throws UserNotFound
		{		
		User user = Repositories.user.findOne(mail);	
		
		if(user!=null && user.canAccess(psw,this))
			{
			setActualUser(user);
			return user;
			}
			
		throw new UserNotFound();
		}

	

	
	public User getUser(String mail)
		{
		return Repositories.user.findOne(mail);
		}
	
	
	
	public Role getUserRole()
		{
		return Repositories.role.findOne(new Role(Role.USER,this.name).getId());
		}
	
	
	public Role getAdminRole()
		{
		return Repositories.role.findOne(new Role(Role.ADMIN,this.name).getId());
		}
	
	
	public Role getRole(String name)
		{
		return Repositories.role.findOne(new Role(name,this.name).getId());
		}	
	
	
	public void addRole(String name)
		{
		Repositories.role.save(new Role(name,this.name));
		}
	
	
	public List<Role> getRoles()
		{		
		return Repositories.role.findAll();
		}
	
	
	//@Transactional
	public void save(User user)
		{		
		try{user.getRoot().mkdirs();}catch(Exception e){}
		Repositories.user.save(user);
		}
	
	
	@JsonIgnore
	public List<User> getUsers()
		{		
		return Repositories.user.findDistinctUserByRolesCompany(name);
		}
	
	
	public void resetPasswordMail(User user)
		{
		System.out.println("send to:"+user.getMail()+" "+welcome);
		welcome.send(user.getMail(),user);		
		}
	
	
	public void sendPasswordMail(User user)
		{
		System.out.println("send to:"+user.getMail()+" "+welcome);
		welcome.send(user.getMail(),user);		
		}
	
	
	public void sendWelcomeMail(User user)
		{
		System.out.println("send to:"+user.getMail()+" "+welcome);
		welcome.send(user.getMail(),user);		
		}
	public String getSecretKey() 
		{
		return secretKey;
		}
	public int maxLoginTry()
		{
		return 3;
		}
	public void setActualUser(User user)
		{
		this.session.setUser(user);
		}
	public void confirmRegistration(String id) 
		{
		Repositories.pending.findOne(id).execute();
		}
	
	}
