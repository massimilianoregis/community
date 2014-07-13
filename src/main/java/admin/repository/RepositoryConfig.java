package admin.repository;


import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

@PropertySource({"community.properties"})
@Component("repositoryConfig")
@Configuration
public class RepositoryConfig {
	@Autowired
    private ServletContext servletContext;	
	private DriverManagerDataSource datasource;
	@Value("${community.db.url}") private String dburl;
	@Value("${community.db.driver}") private String dbDriver;
	@Value("${community.db.user}") private String dbUser;
	@Value("${community.db.psw}") private String dbPsw;

	 @Autowired
	    ApplicationContext context; 
	
	@Bean
	public DataSource dataSource() {
		  	System.out.println("datasource:"+servletContext.getRealPath("WEB-INF/data"));
		  	
	        datasource = new DriverManagerDataSource();
	        datasource.setDriverClassName(dbDriver);
	        datasource.setUrl(dburl);
	        datasource.setUsername(dbUser);
	        datasource.setPassword(dbPsw);	        
		  	
	        return datasource;
	    }

	  @Bean
	  public SessionFactory sessionFactory() throws Exception
	  {
		  System.out.println("SessionFactory");
		  LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean()
				  	{
			  		public SessionFactory getObject()
			  			{
			  			System.out.println("getSession");
			  			SessionFactory factory =super.getObject(); 
			  			
			  			return factory;
			  			}
				  	};
				  	

		SessionFactory session =sessionFactoryBean.getObject();
		
	      return session;
	  }
	  
	  @Bean 
	  public EntityManagerFactory entityManagerFactory() {
		  System.out.println("Entity Manager");
		  JpaVendorAdapter vendorAdapter = jpaVendorAdapter(null);
		  LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		    
		  Properties jpaProperties = new Properties();
		    		 jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		    
		  factory.setJpaProperties(jpaProperties);
		  factory.setJpaVendorAdapter(vendorAdapter);
		  factory.setPackagesToScan("*");
		  factory.setDataSource(dataSource());

		  factory.afterPropertiesSet();

		  return factory.getObject();
		  }
	  
	@Bean
    public JpaVendorAdapter jpaVendorAdapter(final Environment environment) {	
		System.out.println("jpaVendorAdapter");
	final HibernateJpaVendorAdapter rv = new HibernateJpaVendorAdapter();
	
	rv.setDatabase(Database.H2);
	rv.setDatabasePlatform(H2Dialect.class.getName());
	rv.setGenerateDdl(false);
	rv.setShowSql(true);
	
	return rv;
    }
	


}
