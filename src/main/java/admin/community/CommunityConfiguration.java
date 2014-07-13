package admin.community;

import java.util.List;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import admin.mail.Envelope;

@PropertySource({"community.properties"})
@Configuration
public class CommunityConfiguration 
	{
	@Value("${welcome.url}")	private String wUrl;
	@Value("${welcome.from}")	private String to;
	@Value("${community.root}") private String basePath;
	@Value("${smtp.url}") 		private String smtpUrl;
	@Value("${smtp.port}") 		private int smtpPort;
	@Value("${smtp.user}") 		private String smtpUser;
	@Value("${smtp.password}") 	private String smtpPassword;
	@Value("${smtp.auth}") 		private boolean smtpAuth;
	
	@Bean
	public List<Community> list()
		{	
		return null;
		}
	@Bean
	public String root()
		{
		return basePath;
		};
	
	
	
	@Bean
	public Envelope welcome()
		{
		return new Envelope(to, "esempio", wUrl);
		}
	@Bean
	public JavaMailSender postman()
		{
		org.springframework.mail.javamail.JavaMailSenderImpl result = new org.springframework.mail.javamail.JavaMailSenderImpl();
			result.setHost(smtpUrl);
			result.setPort(smtpPort);
			result.setUsername(smtpUser);
			result.setPassword(smtpPassword);
			if(smtpAuth)
				{
				result.getJavaMailProperties().setProperty("mail.smtp.auth", "true");
				result.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");
				result.getJavaMailProperties().setProperty("mail.smtp.ssl.trust", smtpUrl);
				}
		return result;
		}
	}
