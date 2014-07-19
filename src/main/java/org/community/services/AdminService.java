package org.community.services;

import java.util.List;

import javax.transaction.Transactional;

import org.community.Admin;
import org.community.Community;
import org.community.User;
import org.community.repository.RoleRepository;
import org.community.security.Secured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Transactional
@Controller
public class AdminService 
	{
	@Autowired	
	private Admin admin;
	
	@RequestMapping("/community/admin/add")
	public @ResponseBody void execute(String realm, String mail) throws Exception
		{
		admin.addCommunity(realm,mail);		
		}
	
	
	@RequestMapping("/community/admin/list")
	public @ResponseBody List<Community> list() throws Exception
		{				
		return admin.list();		
		}
	
	}
