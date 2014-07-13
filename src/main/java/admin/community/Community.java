package admin.community;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import admin.community.exceptions.UserJustPresent;
import admin.community.exceptions.UserNotFound;
import admin.mail.Envelope;
import admin.repository.CommunityRepository;


@Component("community")
public class Community 
	{
	@Autowired
	private String root;
	@Autowired
	private Envelope welcome;
	private Envelope changePsw;
	private Envelope resetPsw;
	@Autowired
	private CommunityRepository repository;
	
	public Community(){}
	
	
	public User addUser(String mail) throws UserJustPresent
		{
		if(repository.exists(mail)) throw new UserJustPresent();
		User user = new User(mail,this, new File(this.root,mail).getAbsolutePath());
		
		return user;
		}
	
	public User me()
		{
		return SessionData.getUser();
		}
	public User login(String mail, String psw) throws UserNotFound
		{
		User user = repository.findOne(mail);
		user.setCommunity(this);
		if(user!=null && user.getPsw().equals(psw)) 
			{
			SessionData.setUser(user);
			return user;
			}
		throw new UserNotFound();
		}
	public void logout()
		{
		SessionData.setUser(null);
		}
	public User getUser(String mail)
		{
		return this.repository.findOne(mail);
		}
	@Transactional
	public void save(User user)
		{
		System.out.println("save...."+user.getMail());
		user.getRoot().mkdirs();
		repository.save(user);
		}
	public List<User> getUsers()
		{		
		return repository.findAll();
		}
	public void sendWelcomeMail(User user)
		{
		System.out.println("send to:"+user.getMail());
		welcome.send(user.getMail(),user);		
		}
	
	}
