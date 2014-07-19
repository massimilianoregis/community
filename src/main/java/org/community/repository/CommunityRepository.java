package org.community.repository;

import java.util.List;

import org.community.Community;
import org.community.User;
import org.springframework.data.repository.Repository;



public interface CommunityRepository extends Repository<Community, String> 
{
	public List<Community> findAll();
	public Community save(Community wsdl);
	public Community findOne(String mail);
	public void delete(String entity);
	public boolean exists(String entity);
}
