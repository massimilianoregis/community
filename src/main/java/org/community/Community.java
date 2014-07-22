package org.community;

import java.io.File;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import org.community.exceptions.UserJustPresent;
import org.community.exceptions.UserNotFound;
import org.community.mail.Envelope;
import org.community.repository.RoleRepository;
import org.community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="Communities")
@Component("community")
public class Community 
	{	
	@Id			private String name;
	@Autowired	private String root;	
	@Autowired @OneToOne(cascade=CascadeType.ALL) private Envelope welcome;
	
	@Transient
	@Autowired
	private UserRepository repository;
	
	@Transient
	@Autowired
	private RoleRepository roles;
	
	public Community()
		{
		
		}
	public Community(String name,UserRepository repository,RoleRepository roleRepository,String root, Envelope welcome)
		{
		this.roles=roleRepository;
		this.repository=repository;
		this.root=root;
		this.welcome=welcome;
		this.name=name;
		}
	public String getName() 
		{
		return name;
		}
	public String getRoot() 
		{
		return root;
		}
	public Envelope getWelcome() 
		{
		return welcome;
		}
	
	public User addUser(String mail) throws UserJustPresent
		{
		if(repository.exists(mail)) throw new UserJustPresent();
		User user = new User(mail,this, new File(this.root,mail).getAbsolutePath());
		
		return user;
		}
	
	public User me()
		{
		return SessionData.getUser();
		}
	public User login(String mail, String psw) throws UserNotFound
		{
		User user = repository.findOne(mail);
		user.setCommunity(this);	
		if(user!=null && user.getPsw().equals(psw)) 
			{			
			if(!user.canAccess(Role.USER,this.name)) throw new UserNotFound();				
			SessionData.setUser(user);
			return user;
			}
		throw new UserNotFound();
		}
	public void logout()
		{
		SessionData.setUser(null);
		}
	public User getUser(String mail)
		{
		return this.repository.findOne(mail);
		}
	public Role getUserRole()
		{
		return this.roles.findOne(new Role(Role.USER,this.name).getId());
		}
	public Role getAdminRole()
		{
		return this.roles.findOne(new Role(Role.ADMIN,this.name).getId());
		}
	public Role getRole(String name)
		{
		return this.roles.findOne(new Role(name,this.name).getId());
		}
	public void addRole(String name)
		{
		this.roles.save(new Role(name,this.name));
		}
	public List<Role> getRoles()
		{
		return this.roles.findAll();
		}
	@Transactional
	public void save(User user)
		{
		System.out.println("save...."+user.getMail());
		try{user.getRoot().mkdirs();}catch(Exception e){}
		repository.save(user);
		}
	@JsonIgnore
	public List<User> getUsers()
		{		
		return repository.findAll();
		}
	public void sendWelcomeMail(User user)
		{
		System.out.println("send to:"+user.getMail()+" "+welcome);
		welcome.send(user.getMail(),user);		
		}
	
	}
