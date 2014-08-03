package org.community;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.community.exceptions.InvalidPassword;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class User implements Serializable 
{		
	@Id
	private String mail;
	private String psw;	
	private String firstName;
	private String lastName;
	private String root;
	//private List<String> tags;
	@Transient
	private String registerId;
	@Column(columnDefinition="TEXT") private String jsondata;
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
		this.psw=AutomaticPassword.newPassword();		
		}
	public void setCommunity(Community cm)
		{
		this.cm=cm;
		}
	
	public void setName(String fname, String sname)	{this.firstName=fname; this.lastName=sname;}
	public void setPassword(String psw)				
		{
		if(psw!=null && !psw.isEmpty())
			this.psw=psw;
//		byte[] bytesOfMessage = psw.getBytes("UTF-8");
//
//		MessageDigest md = MessageDigest.getInstance("MD5");
//		byte[] thedigest = md.digest(bytesOfMessage);
//		this.psw=new String(thedigest);
		
		}
	
	public String getMail() 		{return mail;}
	public String getFirstName() 	{return firstName;}
	public String getLastName() 	{return lastName;}
	public String getPsw() 			{return psw;}	
	public Set<Role> getRoles() 	{return roles;}
	public String getRegisterId() 	{return registerId;}
	public String getJsondata() 	{return jsondata;}
	
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
		this.cm.resetPasswordMail(this);
		}
	public void sendPassword()
		{
		this.cm.sendPasswordMail(this);
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
	public void addUserRole()
		{
		addRole(this.cm.getUserRole());
		}
	public void addRole(Role role)
		{
		this.roles.add(role);
		}
	public void register()
		{
		this.save();
		this.registerId = new Pending(this,this.cm.getUserRole()).save();
		this.sendWelcome();
		}
	public void save()
		{		
		Community.repository.save(this);		
		}
	public void sendWelcome()
		{
		System.out.println("send mail");
		this.cm.sendWelcomeMail(this);
		}
}
