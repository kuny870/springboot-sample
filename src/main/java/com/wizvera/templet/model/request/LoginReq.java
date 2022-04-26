/*
 *  AcitionReq.java
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
package com.wizvera.templet.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginReq {
	private String	id;
	private String	password;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginReq [");
		if ( id != null ) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if ( password != null ) {
			builder.append("password=");
			builder.append(password);
		}
		builder.append("]");
		return builder.toString();
	}

}