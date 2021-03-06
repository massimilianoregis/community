package org.opencommunity.util;

import org.opencommunity.exception.InvalidJWT;
import org.opencommunity.objs.Community;
import org.opencommunity.services.CommunityService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;

public class JWT 
	{
	static private ObjectMapper json = new ObjectMapper();
	private String jwt;
	private String payload;
	public JWT(String jwt,Community community) throws InvalidJWT
		{
		try
			{
			this.jwt=jwt;
			JWSObject object =JWSObject.parse(jwt);		
			
			CommunityService.JWTResponse jwtObject = json.readValue(object.getPayload().toString(), CommunityService.JWTResponse.class);
			String secret = community.getSecretKey();
			
			JWSVerifier verifier = new MACVerifier(secret.getBytes());
			if(!object.verify(verifier)) throw new InvalidJWT();
			
			this.payload= object.getPayload().toString();
			}
		catch (Exception e) {
			throw new InvalidJWT();
			}
		}
	public String getPayload() 
		{
		return payload;
		}
	}
