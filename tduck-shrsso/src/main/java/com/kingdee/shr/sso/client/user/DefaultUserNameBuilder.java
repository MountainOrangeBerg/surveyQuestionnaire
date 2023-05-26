package com.kingdee.shr.sso.client.user;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenliang
 */
public class DefaultUserNameBuilder implements IUserNameBuilder {

	public String getUserName(HttpServletRequest request) {
		Object userName = request.getSession().getAttribute("username");
		return (String) (userName != null ? userName : request.getSession().getAttribute("userName"));
	}

}