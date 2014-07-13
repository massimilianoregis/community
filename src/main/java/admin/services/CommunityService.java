package admin.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import admin.community.Community;
import admin.community.User;
import admin.community.security.Secured;
import admin.repository.RoleRepository;

@Transactional
@Controller
public class CommunityService 
	{
	@Autowired
	private Community community;	
	@Autowired
	private RoleRepository roleRepository;
	
	@RequestMapping("/community/register")
	public @ResponseBody User execute(String mail, String psw, String first_name, String second_name) throws Exception
		{		
		User user = community.addUser(mail); 
			user.setName(first_name,second_name);
			user.setPassword(psw);
		user.save();
		
		return user;
		}
	
	@Secured("Admin")	
	@RequestMapping("/community/list")
	public @ResponseBody List<User> list() throws Exception
		{		
		return community.getUsers();		
		}
	
	@Secured("Admin")	
	@RequestMapping("/community/role")
	public @ResponseBody void list(String mail,String role) throws Exception
		{		
		community.getUser(mail).addRole(roleRepository.findOne(role));		
		}
	
	@RequestMapping("/community/login")
	public @ResponseBody User login(String mail, String psw) throws Exception
		{		
		return community.login(mail, psw);		
		}
	@RequestMapping("/community/logout")
	public @ResponseBody void logout() throws Exception
		{		
		community.logout();		
		}
	
	@RequestMapping("/community/me")
	public @ResponseBody User me()
		{		
		return community.me();		
		}
	}
