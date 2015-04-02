package org.opencommunity.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("communityRepositories")
public class Repositories {
	@Autowired
	public void setRepositories(						
			RoleRepository roleRepository,
			PendingRepository pendingRepository,
			UserRepository userRepository,
			EnvelopeRepository envelopeRepository,
			JavaMailSender postman	
			)
		{		
		user=userRepository;
		role=roleRepository;		
		pending=pendingRepository;
		envelope=envelopeRepository;
		}

	public static PendingRepository pending;
	public static RoleRepository role;
	public static UserRepository user;
	public static EnvelopeRepository envelope;
}
