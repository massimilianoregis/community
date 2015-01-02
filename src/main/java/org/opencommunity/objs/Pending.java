package org.opencommunity.objs;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.opencommunity.persistence.Repositories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Pending 
{
	@Id
	private String id;
	@ManyToOne
	private Role role;
	@ManyToOne	
	private User user;
		
	public Pending()
	{}
	public Pending(User user, Role role)
		{
		this.id= UUID.randomUUID().toString();
		this.user=user;
		this.role=role;		
		}
	/* (non-Javadoc)
	 * @see org.opencommunity.base.Pending#save()
	 */
	
	public String save()
		{
		Repositories.pending.save(this);
		return id;
		}
	/* (non-Javadoc)
	 * @see org.opencommunity.base.Pending#execute()
	 */
	
	public void execute()
		{
		user.addRole(role);
		user.save();
		Repositories.pending.delete(id);
		}
	/* (non-Javadoc)
	 * @see org.opencommunity.base.Pending#getId()
	 */
	
	public String getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see org.opencommunity.base.Pending#setId(java.lang.String)
	 */
	
	public void setId(String id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see org.opencommunity.base.Pending#getRole()
	 */
	
	public Role getRole() {
		return role;
	}
	/* (non-Javadoc)
	 * @see org.opencommunity.base.Pending#setRole(org.opencommunity.Role)
	 */
	
	public void setRole(Role role) {
		this.role = role;
	}
	/* (non-Javadoc)
	 * @see org.opencommunity.base.Pending#getUser()
	 */
	
	public User getUser() {
		return user;
	}
	/* (non-Javadoc)
	 * @see org.opencommunity.base.Pending#setUser(org.opencommunity.User)
	 */
	
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
