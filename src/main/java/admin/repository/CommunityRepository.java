package admin.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import admin.community.User;


public interface CommunityRepository extends Repository<User, String> 
{
	public List<User> findAll();
	public User save(User wsdl);
	public User findOne(String mail);
	public void delete(String entity);
	public boolean exists(String entity);
}
