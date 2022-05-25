/*
 *  MyLogoutHandler.java
 * 
 *  Copyright (c) 2008-2022 WIZVERA Corp.
 *  All rights reserved. http://www.wizvera.com/
 *
 *  This software is the confidential and proprietary information of
 *  WIZVERA Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with WIZVERA.
 */
package com.wizvera.templet.config;

import com.wizvera.templet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void logout(	HttpServletRequest request,
						HttpServletResponse response,
						Authentication authentication) {
		if ( authentication != null ) {
			userRepository.findByUserId(authentication.getName()).get().setToken(null);
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
//		ObjectMapper mapper = new ObjectMapper();
//		CommonResult result = new CommonResult();
//		result.setResultCode("0");
//		result.setErrorMsg("");
//		try {
//			mapper.writeValueAsString(result);
//			response.getWriter().write(mapper.writeValueAsString(result));
//		}
//		catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}