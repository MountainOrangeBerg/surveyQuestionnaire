package com.kingdee.shr.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kingdee.shr.sso.SslUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.kingdee.shr.sso.client.ltpa.LtpaTokenManager;
import com.kingdee.shr.sso.client.util.SSOUtil;
import com.kingdee.shr.sso.client.util.UrlUtil;

public class SHRClient {
	private static final String ALLOW_CIRCULAR_REDIRECTS = "http.protocol.allow-circular-redirects";
	private static final String JSESSIONID_KEY = "JSESSIONID";
	private static final String SERVICE_PATH = "/shr/msf/service.do";

	private static final String SHR_SSO_TOKEN = "_shr_sso_token";
	private static final String USERNAME = "username";
	private static final String SERVICE_NAME = "serviceName";
	private static final String METHOD = "method";
	private static final String METHOD_NAME = "callService";
	private static final String REFERER = "Referer";
	private static String CONTENT_TYPE = "Content-Type";
	private static String CONTENT_TYPE_POLICY = "application/x-www-form-urlencoded;charset=UTF8";

	public Token login(HttpServletRequest request) throws HttpException, IOException {
		return login(request, null);
	}

	public Token login(HttpServletRequest request, String serverUrl) throws HttpException, IOException {
		String url = SSOUtil.generateUrl(request, serverUrl);
		return loginShr(request, url);
	}

	public Token loginByUser(HttpServletRequest request) throws HttpException, IOException {
		return loginByUser(request, null);
	}

	public Token loginByUser(HttpServletRequest request, String serverUrl) throws HttpException, IOException {
		String loginUrl = SSOUtil.getLoginUrl(serverUrl);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(USERNAME, "administrator");
		SSOUtil.assembleLoginParameters(request, parameters);
		String url = UrlUtil.assembleUrl(loginUrl, parameters);
		return loginShr(request, url);
	}
	
	/**
	 * 调用EAS的退出接口执行用户退出
	 * 
	 * @param serverUrl
	 * @param token
	 */
	public void logout(String serverUrl, Token token,HttpClient client) {
		if (serverUrl == null || serverUrl.isEmpty()) {
			return;
		}

		// 取eas的访问地址, 如果是以shr结尾则截掉它
		boolean endWithSHR = serverUrl.endsWith("shr");
		if (endWithSHR || serverUrl.endsWith("shr/")) {
			int sepLength = (endWithSHR ? 3 : 4);
			serverUrl = serverUrl.substring(0, serverUrl.length() - sepLength);
		} else {
			// 注意, 如果是配置的域名指向到shr(如 shr.kd.com -> 172.18.5.6:6888/shr), 无法获取出正确的eas访问地址, 导致退出失败.
			System.out.println("login shr fail, unsupport to logout by domain, serverUrl: " + serverUrl);
			return;
		}

//		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.protocol.allow-circular-redirects", Boolean.valueOf(true));
		GetMethod logoutPortalMethod = new GetMethod(serverUrl + "easportal/logout.jsp?JSESSIONID=" + token.getValue());
		GetMethod logoutMethod = new GetMethod(serverUrl + "easweb/logout?isSso=true&JSESSIONID=" + token.getValue());

		try {
			SslUtils.ignoreSsl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetMethod m1 = new GetMethod(serverUrl + "portal/web_frame/uncheck/logout.action?ticked=true");
		GetMethod m2 = new GetMethod(serverUrl + "shr/web_frame/easrpc/frame.do?method=logout&ticked=true");
		GetMethod m3 = new GetMethod(serverUrl + "shr/shr_loginout/logout.do?ticked=true");
		GetMethod m4 = new GetMethod(serverUrl + "easportal/logout.jsp?ticked=true");
		//GetMethod m5 = new GetMethod(serverUrl + "shr/gui/logout.jsp?ticked=true");
		GetMethod m5 = new GetMethod(serverUrl + "eassso/logout?url=/portal");
		GetMethod isSsoMethod = new GetMethod(serverUrl + "easweb/logout?isSso=true");
		GetMethod shrlogout = new GetMethod(serverUrl + "shr/shr_loginout/logoutAndReleaseResource.do?ticked=true");
		GetMethod portallogout = new GetMethod(serverUrl + "portal/web_frame/uncheck/easrpc/logoutAndReleaseResource.action?ticked=true");
			try {
			client.executeMethod(logoutPortalMethod);
			client.executeMethod(logoutMethod);
				client.executeMethod(m1);
				client.executeMethod(m2);
				client.executeMethod(m3);
				client.executeMethod(m4);
				client.executeMethod(m5);
				client.executeMethod(isSsoMethod);
				client.executeMethod(shrlogout);
				client.executeMethod(portallogout);


			} catch (IOException e) {
			System.out.println("login shr, server: " + serverUrl + ", token: " + token);
		} catch (Exception e) {
			System.out.println("login shr fail, server: " + serverUrl + ", token: " + token);
		}
	}

	
	public Response executeServiceByUsername(String username, String serverUrl, String serviceName, Map<String, Object> param) throws HttpException, IOException {


		HttpClient client = new HttpClient();
		Token token = loginShrByUsername(username,serverUrl,client);
		return executeOSFService(serverUrl, token, serviceName, param,client);
	}
	
	public Token loginShrByUsername(String username, String serverUrl, HttpClient client) throws IOException, HttpException {
		String loginUrl = serverUrl + "/OTP2sso.jsp";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(USERNAME, username);
		String password = LtpaTokenManager.generate((String) parameters.get(USERNAME), LtpaTokenManager.getDefaultLtpaConfig(), "OTP").toString();
		parameters.put("password", password);
		parameters.put("userAuthPattern", "OTP");
		parameters.put("isNotCheckRelogin", "true");
		
		//parameters.put("redirectTo", "/attachmentUpload.do?method=download&id=MrQAAAADvST0r08D");
		
		String url = UrlUtil.assembleUrl(loginUrl, parameters);
		System.out.println("单点登录 url: " + url);
		client.getParams().setParameter(ALLOW_CIRCULAR_REDIRECTS, Boolean.valueOf(true));
		try {
			SslUtils.ignoreSsl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetMethod method = new GetMethod(url);
		method.addRequestHeader(REFERER, serverUrl);
		method.getParams().setParameter("http.protocol.single-cookie-header", true);
		method.getParams().setParameter(HttpMethodParams.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

		method.setFollowRedirects(true);
		int status = client.executeMethod(method);
		Token token = null;
		if (status != 200) {
			throw new HttpException("login shr fail, status: " + status);
		}else {
			String content = method.getResponseBodyAsString();
			System.out.println("login shr success!");
		}
		token = getToken(client.getState().getCookies());
		return token;
	}

	public Response executeService(String serverUrl, String serviceName, Map<String, Object> param) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		Token token = loginShrByUser(serverUrl,client);
		return executeOSFService(serverUrl, token, serviceName, param,client);
	}

	public Response executeOSFService(String serverUrl, Token token, String serviceName, Map<String, Object> param, HttpClient client) throws HttpException, IOException {
		//client = new HttpClient();
		String loginUrl = serverUrl + SERVICE_PATH;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(METHOD, METHOD_NAME);
		parameters.put(SERVICE_NAME, serviceName);
		String url = UrlUtil.assembleUrl(loginUrl, parameters);
		client.getParams().setParameter("http.protocol.allow-circular-redirects", Boolean.valueOf(true));
		System.out.println(url);
		PostMethod method = new PostMethod(url);
		try {
			SslUtils.ignoreSsl();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((param != null) && (param.size() > 0)) {
			Iterator iter = param.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				//URLEncoder.encode()
                method.setParameter(entry.getKey().toString(),entry.getValue().toString());
			}
	
		}
	
		method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		method.setRequestHeader("Referer", serverUrl);
		addToken(client, token);
		String cookieString = UrlUtil.cookiesToString(client.getState().getCookies());
		System.out.println("cookieString = " + cookieString);
		//cookieString = cookieString.substring(0, cookieString.length()-51);
		//System.out.println("new cookieString = " + cookieString);
		method.setRequestHeader("Cookie", cookieString);
		int status = client.executeMethod(method);
		Response response = new Response();
		if (status == 200) {
			String content = method.getResponseBodyAsString();
			response.setData(content);
		} else {
			System.err.println("Method failed: " + method.getStatusLine());
			throw new HttpException("status: " + status + ", request url: " + url + " failed");
		}
		method.releaseConnection();
		
		// 调完接口进行用户退出
		try {
			logout(serverUrl, token,client);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return response;
	}

	public Token loginShrByUser(String serverUrl, HttpClient client) throws IOException, HttpException {
		String loginUrl = serverUrl + "/OTP2sso.jsp";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(USERNAME,"administrator");
		String password = LtpaTokenManager.generate((String) parameters.get(USERNAME), LtpaTokenManager.getDefaultLtpaConfig(), "OTP").toString();
		parameters.put("password", password);
		parameters.put("userAuthPattern", "OTP");
		parameters.put("isNotCheckRelogin", "true");
		
		//parameters.put("redirectTo", "/shr/msf/service.do?method=callService&serviceName=getJobs");
		
		String url = UrlUtil.assembleUrl(loginUrl, parameters);
		System.out.println("单点登录 url: " + url);
		client.getParams().setParameter(ALLOW_CIRCULAR_REDIRECTS, Boolean.valueOf(true));
		try {
			SslUtils.ignoreSsl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetMethod method = new GetMethod(url);
		method.addRequestHeader(REFERER, serverUrl);
		method.getParams().setParameter("http.protocol.single-cookie-header", true);
		method.getParams().setParameter(HttpMethodParams.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

		method.setFollowRedirects(true);
		int status = client.executeMethod(method);
		Token token = null;
		if (status != 200) {
			throw new HttpException("login shr fail, status: " + status);
		}else {
			System.out.println(UrlUtil.cookiesToString(client.getState().getCookies()));
			String content = method.getResponseBodyAsString();
			System.out.println("login shr success! content = " + content);
		}
		token = getToken(client.getState().getCookies());
		return token;
	}

	private Token loginShr(HttpServletRequest request, String loginUrl) throws IOException, HttpException {
		HttpClient client = new HttpClient();
		client.getParams().setParameter(ALLOW_CIRCULAR_REDIRECTS, Boolean.valueOf(true));
		try {
			SslUtils.ignoreSsl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetMethod method = new GetMethod(loginUrl);
		method.addRequestHeader(REFERER, loginUrl);
		method.getParams().setParameter("http.protocol.single-cookie-header", true);
		method.getParams().setParameter(HttpMethodParams.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		method.setFollowRedirects(true);

		int status = client.executeMethod(method);
		Token token = null;
		if (status != 200) {
			System.err.println("Method failed: " + method.getStatusLine());
			throw new HttpException("login shr fail, status: " + status);
		}
		token = getToken(client.getState().getCookies());

		method.releaseConnection();
		request.getSession().setAttribute(SHR_SSO_TOKEN, token);
		return token;
	}

	private Token getToken(Cookie[] cookies) throws HttpException {
		Cookie cookie = null;
		for (int i = 0; i < cookies.length; ++i) {
			cookie = cookies[i];
			if ((JSESSIONID_KEY.equalsIgnoreCase(cookie.getName())) && (cookie.getPath().indexOf("sso") == -1)) {
				Token token = new Token();
				token.setDomain(cookie.getDomain());
				token.setPath(cookie.getPath());
				token.setValue(cookie.getValue());
				return token;
			}
		}

		throw new HttpException("getToken fail, cookies: " + cookieToString(cookies));
	}

	private String cookieToString(Cookie[] cookies) {
		Cookie cookie = null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cookies.length; ++i) {
			cookie = cookies[i];
			if (i != 0)
				sb.append(",");

			sb.append("[name=");
			sb.append(cookie.getName());
			sb.append(", path=");
			sb.append(cookie.getPath());
			sb.append(", value=");
			sb.append(cookie.getValue());
			sb.append("]");
		}
		return sb.toString();
	}

	private String generateOSFUrl(String serverUrl, String serviceName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(METHOD, METHOD_NAME);
		parameters.put(SERVICE_NAME, serviceName);
		return UrlUtil.assembleUrl(serverUrl + SERVICE_PATH, parameters);
	}

	private void setOSFPostParams(PostMethod method, Map<String, Object> params) {
		if (params == null || params.isEmpty()) {
			return;
		}

		if ((params != null) && (params.size() > 0)) {
			String key = null;
			String value = null;
			Object object = null;
			Iterator<String> paramIterator = params.keySet().iterator();
			while (paramIterator.hasNext()) {
				key = paramIterator.next();
				object = params.get(key);
				if (object == null) {
					continue;
				}

				if (object instanceof Map) {
					value = UrlUtil.toJSON((Map<?, ?>) object);
				} else {
					value = object.toString();
				}
				method.setParameter(key, value);
			}
		}

	}
	
	public Response execute(HttpServletRequest request, String serverUrl, String serviceName, Map<String, Object> params) throws IOException {
		if ((serverUrl == null) || (serverUrl.length() == 0)) {
			throw new HttpException("serverUrl is null");
		}

		if (serviceName == null) {
			throw new HttpException("serviceName is null");
		}

		Token token = (Token) request.getSession().getAttribute(SHR_SSO_TOKEN);
		if (token == null) {
			token = login(request);
		}


		HttpClient client = new HttpClient();
		client.getParams().setParameter(ALLOW_CIRCULAR_REDIRECTS, Boolean.valueOf(true));
		String url = generateOSFUrl(serverUrl, serviceName);
		PostMethod method = new PostMethod(url);
		method.addRequestHeader(REFERER, serverUrl);
		method.setRequestHeader(CONTENT_TYPE, CONTENT_TYPE_POLICY);
		method.getParams().setParameter(HttpMethodParams.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		setOSFPostParams(method, params);

		addToken(client, token);
		
		System.out.println("start executeMethod :" + method.getURI());
		try {
			SslUtils.ignoreSsl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int status = client.executeMethod(method);
		Response response = new Response();
		if (status == 200) {
			String content = method.getResponseBodyAsString();
			response.setData(content);
		} else {
			System.err.println("Method failed: " + method.getStatusLine());
			throw new HttpException("status: " + status + ", request url: " + url + " failed");
		}
		method.releaseConnection();
		
		// 调完接口进行用户退出
		try {
			logout(serverUrl, token,client);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	public Response execute(HttpServletRequest request, String serviceName, Map<String, Object> parameters) throws IOException {
		String serverUrl = SSOUtil.getServerUrl();
		return execute(request, serverUrl, serviceName, parameters);
	}

	private void addToken(HttpClient client, Token token) {
		Cookie cookie = new Cookie();
		cookie.setName(JSESSIONID_KEY);
		cookie.setDomain(token.getDomain());
		cookie.setPath(token.getPath());
		cookie.setValue(token.getValue());
		client.getState().addCookie(cookie);
	}

}