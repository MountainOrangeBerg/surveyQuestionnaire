package com.kingdee.shr.sso.client.user;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenliang
 */
public interface IUserNameBuilder {

	public String getUserName(HttpServletRequest request);
}
