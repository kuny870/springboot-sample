/*
 *  HttpUtils.java
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
package com.wizvera.templet.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 *
 * @author jeehoon.song
 * @since 2022. 3. 30.
 */
public class HttpUtils {
	public static final String[] HEADERS_TO_TRY = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	public static final String getClientIpAddress(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if ( ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip) ) {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}
}
