package com.kingdee.shr.sso.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.kingdee.shr.sso.client.ltpa.LtpaTokenManager;
import com.kingdee.shr.sso.client.user.IUserNameBuilder;

/**
 * @author chenliang
 */
public class SSOUtil {

	private static final String SSO_CONFIG = "shr-ssoClient.properties";

	private static Properties properties = null;

	public static String generateUrl(HttpServletRequest request) {
		return generateUrl(request, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String generateUrl(HttpServletRequest request, String serverUrl) {
		Map parameters = new HashMap();
		assembleLoginParameters(request, parameters);
		if (parameters.get("username") == null || "null".equals((String) parameters.get("username"))
				|| "".equals((String) parameters.get("username"))) {
			return "userNameIsNull";
		}
		String loginUrl = getLoginUrl(serverUrl);
		return UrlUtil.assembleUrl(loginUrl, parameters);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map assembleLoginParameters(HttpServletRequest request, Map parameters) {
		if (parameters == null) {
			parameters = new HashMap();
		}

		Properties properties = getConfig();
		String userName = (String) parameters.get("username");
		if (userName == null) {
			userName = getUserName(request, properties);
		}
		
		parameters.put("username", userName);
		if (userName != null) {
			String authPattern = (properties != null) ? properties.getProperty("auth.pattern") : "OTP";
			String password = LtpaTokenManager.generate(userName, LtpaTokenManager.getDefaultLtpaConfig(), authPattern).toString();
			parameters.put("password", password);
		} else {
			System.out.println("用户名为空");
		}

		String redirectTo = request.getParameter("redirectTo");
		if (redirectTo != null && redirectTo.trim() != null) {
			parameters.put("redirectTo", redirectTo);
		}
		String callback = request.getParameter("callback");
		if (callback != null && callback.trim() != null) {
			parameters.put("callback", callback);
		}

		return parameters;
	}

	public static Properties getConfig() {
		if (properties != null) {
			return properties;
		}

		InputStream inputStream = null;
		try {
			inputStream = SSOUtil.class.getClassLoader().getResourceAsStream(SSO_CONFIG);
			properties = new Properties();
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	@SuppressWarnings("rawtypes")
	private static String getUserName(HttpServletRequest request, Properties properties) {
		String userNameBuilder = null;
		if (properties != null) {
			userNameBuilder = properties.getProperty("userNameBuilder");
		} else {
			userNameBuilder = "com.kingdee.shr.sso.client.user.DefaultUserNameBuilder";
		}

		try {
			Class clazz = Class.forName(userNameBuilder);
			Object obj = clazz.newInstance();
			if (obj instanceof IUserNameBuilder) {
				return ((IUserNameBuilder) obj).getUserName(request);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getLoginUrl(String serverUrl) {
		Properties properties = getConfig();
		String realServerURL = (serverUrl != null) ? serverUrl : (properties != null ? properties.getProperty("server.url") : "unknowhost");
		String realServerPath = (properties != null) ? properties.getProperty("server.path") : "/OTP2sso.jsp";
		return realServerURL + realServerPath;
	}

	public static String getServerUrl() {
		Properties properties = getConfig();
		String serverUrl = null;
		if (properties != null) {
			serverUrl = properties.getProperty("server.url");
		}
		return serverUrl;
	}
}
