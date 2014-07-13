package admin.community;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {
	@Id
	private String id;
	private String name;
	public Role()
		{
		
		}
	public Role(String role)
		{
		this.id=role;
		}
	
	public String getId() {
		return id;
	}
	@Override
	public int hashCode() {
		
		return id.hashCode();
		}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof String)
			return obj.toString().equals(id);
		return super.equals(obj);
	}
}
