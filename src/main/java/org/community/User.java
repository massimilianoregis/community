package org.community;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.community.exceptions.InvalidPassword;


import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
public class User 
{	
	private boolean justbuild=true;
	@Id
	private String mail;
	private String psw;
	private String first_name;
	private String second_name;
	private String root;
	//private Map jsonData;
	@ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	private Set<Role> roles = new HashSet<Role>(); 
	
	@Transient	
	private Community cm;
	
	public User()
		{
		
		}
	public User(String mail,Community cm, String root)
		{
		this.mail=mail;
		this.cm=cm;
		this.root=root;
		}
	public void setCommunity(Community cm)
		{
		this.cm=cm;
		}
	public void setName(String fname, String sname)	{this.first_name=fname; this.second_name=sname;}
	public void setPassword(String psw)				
		{
		if(psw==null)
			psw="admin";
//		byte[] bytesOfMessage = psw.getBytes("UTF-8");
//
//		MessageDigest md = MessageDigest.getInstance("MD5");
//		byte[] thedigest = md.digest(bytesOfMessage);
//		this.psw=new String(thedigest);
		this.psw=psw;
		}
	
	public String getMail() 		{return mail;}
	public String getFirstName() 	{return first_name;}
	public String getSecondName() 	{return second_name;}
	public String getPsw() 			{return psw;}	
	public Set<Role> getRoles() 	{return roles;}
	
	@JsonIgnore
	public File getRoot() 			{return new File(root);}
	
	public void setPassword(String psw, String psw2) throws InvalidPassword
		{
		if(!psw.equals(psw2)) throw new InvalidPassword();
		this.psw=psw;
		this.save();
		}
	public void resetPassword()
		{
		
		}
	public void sendPassword()
		{
		
		}
	public boolean canAccess(String role, String company)
		{
		for(Role r : roles)
			{			
			if(r.getId().equals(role)) 				return true;
			if(r.getId().equals(company+"."+role))	return true;
			}
		return false;
		}
	public void addRole(Role role)
		{
		this.roles.add(role);
		}
	public void save()
		{
		boolean send = this.justbuild;
		this.justbuild=false;
		cm.save(this);
		if(send) this.sendWelcome();		
		}
	public void sendWelcome()
		{
		System.out.println("send mail");
		this.cm.sendWelcomeMail(this);
		}
}
