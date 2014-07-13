package admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan
@EnableJpaRepositories
@EnableAutoConfiguration
@EnableAspectJAutoProxy

public class CommunityApplication extends SpringBootServletInitializer 
{
	@Override
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
		{
		return application.sources(CommunityApplication.class);
		}
	
    public static void main(String[] args) {
    //	System.setProperty("spring.profiles.default", System.getProperty("spring.profiles.default", "dev"));
        ConfigurableApplicationContext ctx =SpringApplication.run(CommunityApplication.class, args);
        
       
       
    }
}