package org.opencommunity.services;

import java.io.IOException;
import java.util.List;

import org.opencommunity.exception.InvalidJWT;
import org.opencommunity.exception.UserNotFound;
import org.opencommunity.objs.Community;
import org.opencommunity.objs.Pending;
import org.opencommunity.objs.Role;
import org.opencommunity.objs.User;
import org.opencommunity.persistence.Repositories;
import org.opencommunity.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;


@Transactional("communityTransactionManager")
@Controller
@RequestMapping("/community")
public class CommunityService 
	{
	@Autowired
	private Community community;
	
		
	static private ObjectMapper json = new ObjectMapper();

	
	
	//@Secured("Admin")		
	@RequestMapping("/list")
	public @ResponseBody List<User> list() throws Exception
		{						
		return community.getUsers();		
		}
	
	//salva utente
	@RequestMapping(value="/user", method = RequestMethod.POST)
	public @ResponseBody void me(@RequestBody User user)
		{						
		User usr = community.getUser(user.getMail());
			 usr.setData(user.getData());
			 usr.setFirstName(user.getFirstName());
			 usr.setLastName(user.getLastName());
			 usr.setData(user.getData());
			 //usr.setRoot(community.getRoot()+"/"+usr.getMail());
	    usr.save();
		}
	//leggi lista utenti
	@RequestMapping(value= "/user",method=RequestMethod.GET)
	public @ResponseBody List<User> userList() throws Exception
		{						
		return community.getUsers();		
		}
	//leggi utente singolo
	@RequestMapping(value= "/user/{mail:.+}",method=RequestMethod.GET)
	public @ResponseBody User user(@PathVariable String mail) throws Exception
		{							
		return community.getUser(mail);		
		}
	
	
	@RequestMapping("/pendings")
	public @ResponseBody List<Pending> pendings() throws Exception
		{						
		return Repositories.pending.findAll();		
		}
	@RequestMapping("/confirm/{id}")
	public @ResponseBody void confirm(@PathVariable String id) throws Exception
		{		
		community.confirmRegistration(id);		
		}

	
	@RequestMapping(value="/role",method=RequestMethod.DELETE)
	public @ResponseBody void removeRole(String role) throws Exception
		{
		Repositories.role.delete(role);
		}	
	@RequestMapping(value="/role",method=RequestMethod.POST)
	public @ResponseBody void addRole(String role) throws Exception
		{
		community.addRole(role);		
		}
	@RequestMapping(value="/role",method=RequestMethod.GET)
	public @ResponseBody List<Role> getRoles() throws Exception
		{
		return community.getRoles();
		}	



	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public @ResponseBody User registerPost(@RequestBody RequestRegister request) throws Exception
		{
		return register(request.getMail(), request.getPsw(), request.getFirst_name(), request.getLast_name());
		}
	@RequestMapping(value="/register",method=RequestMethod.GET)
	public @ResponseBody User register(@RequestParam(defaultValue="") String mail, String psw, String first_name, String last_name) throws Exception
		{				
		User user = community.addUser(mail); 
			user.setName(first_name,last_name);
			user.setPassword(psw);
		user.register();
		
		return user;
		}
	
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public @ResponseBody User loginPost(@RequestBody RequestLogin request) throws Exception,UserNotFound
		{
		return login(request.getMail(), request.getPsw());
		}
	
	@RequestMapping("/login")	
	public @ResponseBody User login(String mail, String psw) throws Exception,UserNotFound
		{										
		User user=  community.login(mail, psw);
		System.out.println("logged:"+user);
		return new JWTResponse(user,community);
		}
	
	@RequestMapping("/login/uid")
	public @ResponseBody User loginByUID(String uid) throws Exception
		{						
		User user = Repositories.user.findUserByUid(uid);
		System.out.println("logged:"+user);
		return new JWTResponse(user,community);
		}
	
	@RequestMapping("/logout")
	public @ResponseBody void logout() throws Exception
		{				
		community.logout();		
		}	
	
	//@Secured("Admin")	
	@RequestMapping(value="/user/role",method=RequestMethod.GET)
	public @ResponseBody void list(String mail,String role) throws Exception
		{		
		community.getUser(mail).addRole(Repositories.role.findOne(role));				
		}
	@RequestMapping("/me")
	public @ResponseBody User me()
		{						
		return community.me();		
		}
	@RequestMapping("/{mail}/sendPsw")
	public @ResponseBody void sendPassword(@PathVariable String mail)
		{						
		community.getUser(mail).sendPassword();		
		}
	@RequestMapping("/{mail}/resetPsw")
	public @ResponseBody void resetPassword(@PathVariable String mail)
		{						
		community.getUser(mail).resetPassword();		
		}
	@RequestMapping("/me/data/{name:.+}")
	public @ResponseBody byte[] data(@PathVariable String name) throws IOException
		{											
		return org.apache.commons.io.IOUtils.toByteArray(community.me().getFile(name));		
		}
	
	
	@RequestMapping(value="/jwt")
	public @ResponseBody User check(String jwt) throws InvalidJWT
		{			
		return new JWTResponse(
				community.getUser( (String)new JWT(jwt,this.community).getObject().get("mail") ),
				this.community);				
		}
	static public class RequestLogin
		{
		private String mail;
		private String psw;		
		
		public String getMail() {return mail;}
		public void setMail(String mail) {this.mail = mail;}
		public String getPsw() {return psw;}
		public void setPsw(String psw) {this.psw = psw;}		
		}
	static public class RequestRegister
		{
		private String mail;
		private String psw;
		private String first_name;
		private String last_name;
		
		public String getMail() {return mail;}
		public void setMail(String mail) {this.mail = mail;}
		public String getPsw() {return psw;}
		public void setPsw(String psw) {this.psw = psw;}
		public String getFirst_name() {return first_name;}
		public void setFirst_name(String first_name) {this.first_name = first_name;}
		public String getLast_name() {return last_name;}
		public void setLast_name(String last_name) {this.last_name = last_name;}
		}
	static public class JWTResponse extends User
		{
		private String jwt;
		private String community;
		public JWTResponse(){}
		public JWTResponse(User user,Community community)
			{			
			setFirstName(user.getFirstName());
			setLastName(user.getLastName());
			setMail(user.getMail());
			setRegisterId(user.getRegisterId());
//			setRoot(user.getRoot());
			this.community=community.getName();
			
			try{Payload payload = new Payload(json.writeValueAsString(this));
				JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
				JWSObject jwsObject = new JWSObject(header, payload);		
				JWSSigner signer = new MACSigner(community.getSecretKey().getBytes());
				jwsObject.sign(signer);		
				this.jwt=jwsObject.serialize();
				
			setData(user.getData());	
				
			}catch(Exception e){e.printStackTrace();}
			}
		public String getCommunity() 
			{
			return community;
			}
		public String getJwt() 
			{
			return jwt;
			}
		}
	}

