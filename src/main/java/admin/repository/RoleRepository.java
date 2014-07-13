package admin.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import admin.community.Role;


public interface RoleRepository extends Repository<Role, String> 
{
	public List<Role> findAll();
	public Role save(Role wsdl);
	public Role findOne(String mail);
	public void delete(String entity);
	public boolean exists(String entity);
}
