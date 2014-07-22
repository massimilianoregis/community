package org.community;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitProperties 
	{
	@Value("${community.db.url}") private String dburl;
	@Value("${community.db.driver}") private String dbDriver;
	@Value("${community.db.user}") private String dbUser;
	@Value("${community.db.psw}") private String dbPsw;
	@Value("${community.db.dialect}") private String dbDialect;
	
	public String getDbDialect() {
		return dbDialect;
	}
	public String getDbDriver() {
		return dbDriver;
	}
	public String getDbPsw() {
		return dbPsw;
	}
	public String getDburl() {
		return dburl;
	}
	public String getDbUser() {
		return dbUser;
	}
	}
