package org.community.mail;

import java.net.URL;
import java.util.Map;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.fasterxml.jackson.databind.ObjectMapper;


@Entity
public class Envelope {
	
	@Id	
	private int id;
	@Column(name="efrom")
	private String from;
	private String subject;
	private String url;
	@Transient
	@Autowired private JavaMailSender postman;	
	public Envelope()
		{
		}
	public Envelope(String from, String subject, String url)
		{		
		this.from=from;
		this.subject=subject;
		this.url=url;
		this.id=subject.hashCode();
		}
	public Envelope(String from, String subject, String url, JavaMailSender postman)
		{		
		this.from=from;
		this.subject=subject;
		this.url=url;
		this.postman=postman;
		this.id=subject.hashCode();
		System.out.println(this.postman);
		}
	public String getFrom()
		{	
		return from;
		}

	public void send(String to, Object obj) 
		{
		try
			{			
			ObjectMapper m = new ObjectMapper();
			Map<String,Object> model = m.convertValue(obj, Map.class);
				
			URL url = new URL(this.url);						
			VelocityEngineFactoryBean vf =  new VelocityEngineFactoryBean();
				vf.setResourceLoaderPath(url.getProtocol()+"://"+url.getHost()+":"+url.getPort());
				vf.setPreferFileSystemAccess(false);			
			String body = VelocityEngineUtils.mergeTemplateIntoString(vf.createVelocityEngine(), url.getPath(), model);
			
			MimeMessage mimeMessage = this.postman.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setFrom(this.from);
				message.setTo(to);
				message.setSubject(this.subject);
				message.setText(body);
			this.postman.send(mimeMessage);
			}
		catch(Exception e)
			{
			e.printStackTrace();
			}
		}
	
}
