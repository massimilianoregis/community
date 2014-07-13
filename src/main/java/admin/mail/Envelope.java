package admin.mail;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Envelope {
	private String from;
	private String subject;
	private String url;
	@Autowired private JavaMailSender postman;	
	
	public Envelope(String from, String subject, String url)
		{
		this.from=from;
		this.subject=subject;
		this.url=url;
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
		
//		SimpleMailMessage sm = new SimpleMailMessage();
//			sm.setFrom(this.from);
//			sm.setSubject(this.subject);
//			sm.setTo(to);
//			sm.setText("prova mail");
//			
//		System.out.println("postman:"+postman);
//		postman.send(sm);
		}
	
}
