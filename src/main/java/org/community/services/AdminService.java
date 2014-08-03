package org.community.services;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.community.Admin;
import org.community.Community;
import org.community.InitProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Transactional
@Controller
public class AdminService 
	{
	@Autowired	
	private Admin admin;
	@Autowired
	private InitProperties properties;
	
	@RequestMapping("/community/properties")
	public @ResponseBody Properties get(String driver, String url, String usr, String psw) throws Exception
		{
		Resource resource = new ClassPathResource("community.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(resource);
		
		
		props.store(new FileOutputStream(resource.getFile()), "Saved at "+new Date());
		return props;
		}
	@RequestMapping("/community/admin/add")
	public @ResponseBody void execute(String realm, String mail) throws Exception
		{
		admin.addCommunity(realm,mail);		
		}
	
	
	@RequestMapping("/community/admin/list")
	public @ResponseBody List<Community> list() throws Exception
		{				
		System.out.println("-----/community/admin/list");
		return admin.list();		
		}
	@RequestMapping("/community/db/list")
	public @ResponseBody List<String> dblist() throws Exception
		{				
		System.out.println("-----/community/db/list");
		return admin.driverList();		
		}
	}
