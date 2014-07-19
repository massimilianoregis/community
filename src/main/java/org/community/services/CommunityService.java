package org.community.services;

import java.util.List;

import javax.transaction.Transactional;

import org.community.Admin;
import org.community.Community;
import org.community.Role;
import org.community.User;
import org.community.repository.RoleRepository;
import org.community.security.Secured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Transactional
@Controller
public class CommunityService 
	{
	@Autowired	
	private Admin admin;
	@Autowired
	private RoleRepository roleRepository;
	
	
	
	@Secured("Admin")	
	@RequestMapping("/community/list")
	public @ResponseBody List<User> list(String realm) throws Exception
		{		
		Community community = admin.getCommunity(realm);
		return community.getUsers();		
		}
	
	//@Secured("Admin")	
	@RequestMapping("/community/user/role")
	public @ResponseBody void list(String realm, String mail,String role) throws Exception
		{
		Community community = admin.getCommunity(realm);
		
		community.getUser(mail).addRole(roleRepository.findOne(role));		
		}
	
	/** ROLES **/
	@RequestMapping("/community/role/remove")
	public @ResponseBody void removeRole(String realm, String role) throws Exception
		{
		this.roleRepository.delete(new Role(role,realm).getId());
		}
	@RequestMapping("/community/role/add")
	public @ResponseBody void addRole(String realm, String role) throws Exception
		{
		admin.getCommunity(realm).addRole(role);
		}
	@RequestMapping("/community/role/list")
	public @ResponseBody List<Role> getRoles(String realm) throws Exception
		{
		return admin.getCommunity(realm).getRoles();
		}
	/** /ROLES **/
	
	@RequestMapping("/community/register")
	public @ResponseBody User execute(String realm,String mail, String psw, String first_name, String second_name) throws Exception
		{		
		Community community = admin.getCommunity(realm);
		User user = community.addUser(mail); 
			user.setName(first_name,second_name);
			user.setPassword(psw);
		user.save();
		
		return user;
		}	
	@RequestMapping("/community/login")
	public @ResponseBody User login(String realm, String mail, String psw) throws Exception
		{		
		Community community = admin.getCommunity(realm);
		return community.login(mail, psw);		
		}
	@RequestMapping("/community/logout")
	public @ResponseBody void logout(String realm) throws Exception
		{		
		Community community = admin.getCommunity(realm);
		community.logout();		
		}
	
	@RequestMapping("/community/me")
	public @ResponseBody User me(String realm)
		{				
		Community community = admin.getCommunity(realm);
		return community.me();		
		}
	}
