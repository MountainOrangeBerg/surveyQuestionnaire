package com.kingdee.shr.sso.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;

public class UrlUtil {
	
	private static final String EQUALS = "=";

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static String assembleUrl(String url, Map<String, Object> params) {
		StringBuilder redirectUrl = new StringBuilder(url);
		
		if(redirectUrl.indexOf("?") == -1) {
			redirectUrl.append('?');
		}else{
			redirectUrl.append('&');
		}

		String key = null, value = null;
		Object object = null;
		int index = 0;
		Iterator<String> paramIterator = params.keySet().iterator();
		while (paramIterator.hasNext()) {
			key = paramIterator.next();			
			object = params.get(key);
			if (object == null) {
				continue;
			}
			
			if (index != 0) {
				redirectUrl.append('&');
			}
			redirectUrl.append(key);
			redirectUrl.append(EQUALS);
			if (object instanceof Map) {
				// Map
				value = toJSON((Map) object);
			} else {
				value = object.toString();
			}
			try {
				redirectUrl.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				redirectUrl.append(URLEncoder.encode(value));
			}
			index++;
		}
		return redirectUrl.toString();
	}
	
	/**
	 * 转换为简单JSON格式，不依赖JSON包
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String toJSON(Map map) {
		StringBuffer sb = new StringBuffer();
		
		if (map != null && !map.isEmpty()) {
			sb.append("{");
			Iterator it = map.keySet().iterator();
			int index = 0;
			String key = null;
			Object value = null;
			while (it.hasNext()) {
				key = (String) it.next();
				if (index > 0) {
					sb.append(",");
				}
				
				sb.append("\"");
				sb.append(key);
				sb.append("\"");
				sb.append(":");
				
				value = map.get(key);
				sb.append("\"");
				if (value != null) {
					sb.append(value.toString());
				}
				sb.append("\"");
				
				index++;
			}
			sb.append("}");
		}
		
		return sb.toString();
	}

	public static String cookiesToString(Cookie[] cookies) {
	    StringBuffer buf = new StringBuffer();
	    Cookie[] arrayOfCookie = cookies;int j = cookies.length;
	    
	    for (int i = 0; i < j; i++){
		    Cookie cookie = arrayOfCookie[i];
		    buf.append(cookie.toString()).append(";");
	    }
	    
	    if ((buf != null) && (buf.length() > 0)){
	    	return buf.substring(0, buf.length() - 1);
	    }
	    
	    return null;
	}
}
