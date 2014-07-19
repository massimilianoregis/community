package org.community.repository;

import java.util.List;

import org.community.User;
import org.springframework.data.repository.Repository;



public interface UserRepository extends Repository<User, String> 
{
	public List<User> findAll();
	public User save(User wsdl);
	public User findOne(String mail);
	public void delete(String entity);
	public boolean exists(String entity);
}
