package org.opencommunity.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.opencommunity.exception.InvalidJWT;
import org.opencommunity.exception.UserNotFound;
import org.opencommunity.objs.Community;
import org.opencommunity.objs.Pending;
import org.opencommunity.objs.Role;
import org.opencommunity.objs.User;
import org.opencommunity.persistence.Repositories;
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
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;


@Transactional("communityTransactionManager")
@Controller
//@RequestMapping("/community")
public class CommunityService 
	{
	@Autowired
	private Community community;
	
		
	static private ObjectMapper json = new ObjectMapper();

	
	
	//@Secured("Admin")		
	@RequestMapping("/community/list")
	public @ResponseBody List<User> list() throws Exception
		{						
		return community.getUsers();		
		}
	@RequestMapping(value="/community/user", method = RequestMethod.POST)
	public @ResponseBody void me(@RequestBody User user)
		{
		;
		User usr = community.getUser(user.getMail());
			 usr.setData(user.getData());
			 usr.save();		
		}
	@RequestMapping(value= "/community/user",method=RequestMethod.GET)
	public @ResponseBody List<User> userList() throws Exception
		{						
		return community.getUsers();		
		}
	@RequestMapping(value= "/community/user/{mail:.+}",method=RequestMethod.GET)
	public @ResponseBody User user(@PathVariable String mail) throws Exception
		{							
		return community.getUser(mail);		
		}
	
	
	@RequestMapping("/community/pendings")
	public @ResponseBody List<Pending> pendings() throws Exception
		{						
		return Repositories.pending.findAll();		
		}
	@RequestMapping("/community/confirm/{id}")
	public @ResponseBody void confirm(@PathVariable String id) throws Exception
		{		
		community.confirmRegistration(id);		
		}

	
	@RequestMapping(value="/community/role",method=RequestMethod.DELETE)
	public @ResponseBody void removeRole(String role) throws Exception
		{
		Repositories.role.delete(role);
		}	
	@RequestMapping(value="/community/role",method=RequestMethod.POST)
	public @ResponseBody void addRole(String role) throws Exception
		{
		community.addRole(role);		
		}
	@RequestMapping(value="/community/role",method=RequestMethod.GET)
	public @ResponseBody List<Role> getRoles() throws Exception
		{
		return community.getRoles();
		}	



	
	@RequestMapping(value="/community/register",method=RequestMethod.POST)
	public @ResponseBody User registerPost(@RequestBody RequestRegister request) throws Exception
		{
		return register(request.getMail(), request.getPsw(), request.getFirst_name(), request.getLast_name());
		}
	@RequestMapping(value="/community/register",method=RequestMethod.GET)
	public @ResponseBody User register(@RequestParam(defaultValue="") String mail, String psw, String first_name, String last_name) throws Exception
		{				
		User user = community.addUser(mail); 
			user.setName(first_name,last_name);
			user.setPassword(psw);
		user.register();
		
		return user;
		}
	
	
	@RequestMapping(value="/community/login",method=RequestMethod.POST)
	public @ResponseBody User loginPost(@RequestBody RequestLogin request) throws Exception,UserNotFound
		{
		return login(request.getMail(), request.getPsw());
		}
	
	@RequestMapping("/community/login")	
	public @ResponseBody User login(String mail, String psw) throws Exception,UserNotFound
		{										
		User user=  community.login(mail, psw);
		System.out.println("logged:"+user);
		return new JWTResponse(user,community);
		}
	
	@RequestMapping("/community/login/uid")
	public @ResponseBody User loginByUID(String uid) throws Exception
		{						
		User user = Repositories.user.findUserByUid(uid);
		System.out.println("logged:"+user);
		return new JWTResponse(user,community);
		}
	
	@RequestMapping("/community/logout")
	public @ResponseBody void logout() throws Exception
		{				
		community.logout();		
		}	
	
	//@Secured("Admin")	
	@RequestMapping(value="/community/user/role",method=RequestMethod.GET)
	public @ResponseBody void list(String mail,String role) throws Exception
		{		
		community.getUser(mail).addRole(Repositories.role.findOne(role));				
		}
	@RequestMapping("/community/me")
	public @ResponseBody User me()
		{						
		return community.me();		
		}
	@RequestMapping("/community/{mail}/sendPsw")
	public @ResponseBody void sendPassword(@PathVariable String mail)
		{						
		community.getUser(mail).sendPassword();		
		}
	@RequestMapping("/community/{mail}/resetPsw")
	public @ResponseBody void resetPassword(@PathVariable String mail)
		{						
		community.getUser(mail).resetPassword();		
		}
	@RequestMapping("/community/me/data/{name:.+}")
	public @ResponseBody byte[] data(@PathVariable String name) throws IOException
		{											
		return org.apache.commons.io.IOUtils.toByteArray(community.me().getFile(name));		
		}
	
	
	@RequestMapping(value="/community/jwt")
	public @ResponseBody String check(String jwt) throws InvalidJWT
		{
		try{
			JWSObject object =JWSObject.parse(jwt);		
			
			CommunityService.JWTResponse jwtObject = json.readValue(object.getPayload().toString(), CommunityService.JWTResponse.class);
			String secret = community.getSecretKey();
			
			JWSVerifier verifier = new MACVerifier(secret.getBytes());
			if(!object.verify(verifier)) throw new InvalidJWT();
			
			return object.getPayload().toString();
			}
		catch (Exception e) {
			throw new InvalidJWT();
			}			
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
			setData(user.getData());
			setFirstName(user.getFirstName());
			setLastName(user.getLastName());
			setMail(user.getMail());
			setRegisterId(user.getRegisterId());
//			setRoot(user.getRoot());
			this.community=community.getName();
			
			try{Payload payload = new Payload(json.writeValueAsString(user));
				JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
				JWSObject jwsObject = new JWSObject(header, payload);		
				JWSSigner signer = new MACSigner(community.getSecretKey().getBytes());
				jwsObject.sign(signer);		
				this.jwt=jwsObject.serialize();
				
				
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

