package org.opencommunity.objs;

import java.io.StringWriter;
import java.net.URL;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.fasterxml.jackson.databind.ObjectMapper;


@Entity
public class Envelope
	{	
	@Id	
	private int id;
	@Column(name="efrom")
	private String from;
	private String subject;
	private String url;
	@Transient
	static public JavaMailSender postman;	
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
			
			VelocityContext context = new VelocityContext(model);				
			StringWriter writer = new StringWriter();
			Velocity.evaluate(context, writer, "VELOCITY", this.subject);			
			
			MimeMessage mimeMessage = this.postman.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setFrom(this.from);
				message.setTo(to);
				message.setSubject(this.subject);
				message.setText(body,true);
				
			this.postman.send(mimeMessage);
			}
		catch(Exception e)
			{
			e.printStackTrace();
			}
		}

	
}
