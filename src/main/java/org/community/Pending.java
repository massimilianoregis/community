package org.community;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.community.repository.PendingRepository;
@Entity
public class Pending 
{
	@Id
	private String id;
	@ManyToOne
	private Role role;
	@ManyToOne	
	private User user;
	
	static public PendingRepository repository;
	public Pending()
	{}
	public Pending(User user, Role role)
		{
		this.id= UUID.randomUUID().toString();
		this.user=user;
		this.role=role;		
		}
	public String save()
		{
		this.repository.save(this);
		return id;
		}
	public void execute()
		{
		user.addRole(role);
		user.save();
		this.repository.delete(id);
		}
}
