/*
 *  JwtAuthException.java
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
package com.wizvera.templet.config.JWT;

import lombok.Getter;

@Getter
public class JwtAuthException extends RuntimeException {
	private static final long serialVersionUID = -2140891467297225823L;

	private int errCode;

	/**
	 * @param errCode
	 */
	public JwtAuthException(int errCode, String message) {
		super(message);
		this.errCode = errCode;
	}

	public JwtAuthException(String message) {
		super(message);
	}
}
