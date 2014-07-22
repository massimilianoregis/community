package org.community;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.community.mail.Envelope;
import org.community.repository.CommunityRepository;
import org.community.repository.RoleRepository;
import org.community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
 * Admin class to collect a list of community configuration 
 */
@Component
public class Admin 
	{
	@Autowired	private Community community;	
	@Autowired	private CommunityRepository repository;	
	@Autowired	private UserRepository userRepository;
	@Autowired	private RoleRepository roleRepository;
	@Value("${community.root}") 	private String basePath;
	@Value("${welcome.url}")		private String wUrl;
	@Value("${welcome.from}")		private String wfrom;
	@Value("${welcome.subject}")	private String wSubject;
	private List<Class<Driver>> drivers;
		
	public Community getCommunity(String name)
		{
		if(name==null) return community;
		return repository.findOne(name);
		}
	public List<Community> list()
		{
		return repository.findAll();
		}
	public List<String> driverList()
		{		
		List<String> result= new ArrayList<String>();
		for(Class driver:drivers)
			result.add(driver.getName());
		return result;
		}

	public void addCommunity(String name,String mail)
		{
		
		Community comm = new Community(name,userRepository,roleRepository,basePath,new Envelope(wfrom,wSubject,wUrl));
		
			comm.addRole(Role.USER);		
			comm.addRole(Role.ADMIN);		
			try{comm.addUser(mail).save();}catch(Exception e){}
			comm.getUser(mail).addRole(comm.getUserRole());
			comm.getUser(mail).addRole(comm.getAdminRole());
		repository.save(comm);
		}
	}
