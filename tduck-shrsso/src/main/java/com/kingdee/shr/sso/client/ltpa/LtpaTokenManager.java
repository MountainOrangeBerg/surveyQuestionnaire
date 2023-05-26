/*
 * 创建日期 2006-2-16
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.kingdee.shr.sso.client.ltpa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;

import com.kingdee.shr.sso.client.util.BASE64Util;
import com.kingdee.shr.sso.client.util.LMBCSUtil;

/**
 * @author linhh
 *
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class LtpaTokenManager {
	public final static String COOKIE_DOMAIN = "cookie.domain";
	public final static String COOKIE_NAME = "LtpaToken";
	public final static String DOMINO_SECRET = "domino.secret";
	public final static String TOKEN_EXPIRATION = "token.expiration";
	public final static String ISLMBCSENCODE = "isLMBCSEncode";

	private static Properties properties = null;
	private static boolean isConfigLoaded = false;
	// LtpaToken.properties
	// private static String CONFIG_NAME = "LtpaToken.properties";

	/**
	 * @param configFile
	 */
	public static void loadConfig(String configFile) {
		// 只装载一次
		if (!isConfigLoaded()) {
			properties = new Properties();
			// File fl = new File(configFile);
			InputStream is = null;

			File file = new File(configFile);
			if (!file.exists()) {
				System.err.println("load jar ltpatoken.properties");

				InputStream input = new LtpaTokenManager().getClass().getResourceAsStream("LtpaToken.properties");

				try {
					properties.load(input);
					isConfigLoaded = true;
				} catch (IOException e) {
					throw new ConfigurationError("LtpaToken.properties not found. file = " + configFile);
				} finally {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} else {

				try {

					is = new FileInputStream(configFile);

					// is = LtpaTokenManager.class.getResourceAsStream(CONFIG_NAME);

					properties.load(is);
					isConfigLoaded = true;

				} catch (IOException ioe) {
					throw new ConfigurationError("LtpaToken.properties not found. file = " + configFile);
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (null == properties.getProperty(DOMINO_SECRET) || null == properties.getProperty(COOKIE_DOMAIN)
					|| null == properties.getProperty(TOKEN_EXPIRATION))
				throw new ConfigurationError("LtpaToken.properties not configured properly. file = " + configFile);
		}
	}

	/**
	 * @deprecated
	 * @param canonicalUser
	 * @return
	 */
	public static LtpaToken generate(String canonicalUser) {
		String configFile = getDefaultConfigFile();
		return generate(canonicalUser, configFile);
	}

	/**
	 * @param canonicalUser
	 * @param configFile
	 * @return
	 */
	public static LtpaToken generate(String canonicalUser, String configFile) {
		initConfig(configFile);
		Date creationDate = new Date();
		Date expirationDate = new Date();
		int interval = Integer.parseInt(properties.getProperty(TOKEN_EXPIRATION));
		expirationDate.setTime(creationDate.getTime() + 1000 * 60 * interval);

		return generate(canonicalUser, creationDate, expirationDate, configFile, null);
	}

	/**
	 * @param canonicalUser
	 * @param configFile
	 * @return
	 */
	public static LtpaToken generate(String canonicalUser, String configFile, String authPattern) {
		initConfig(configFile);
		Date creationDate = new Date();
		Date expirationDate = new Date();
		int interval = 0;
		if (properties.containsKey(authPattern.toLowerCase() + "." + TOKEN_EXPIRATION)) {
			interval = Integer.parseInt(authPattern.toLowerCase() + "." + TOKEN_EXPIRATION);
		} else {
			interval = Integer.parseInt(properties.getProperty(TOKEN_EXPIRATION));
		}
		expirationDate.setTime(creationDate.getTime() + 1000 * 60 * interval);

		return generate(canonicalUser, creationDate, expirationDate, configFile, authPattern);
	}

	/**
	 * Generates a new LtpaToken with given parameters.
	 * 
	 * @deprecated
	 * @param canonicalUser
	 *            User name in canonical form. e.g. 'CN=Robert
	 *            Kelly/OU=MIS/O=EBIMED'.
	 * @param tokenCreation
	 *            Token creation date.
	 * @param tokenExpires
	 *            Token expiration date.
	 * @return The generated token.
	 */
	public static LtpaToken generate(String canonicalUser, Date tokenCreation, Date tokenExpires) {
		String configFile = getDefaultConfigFile();
		return generate(canonicalUser, tokenCreation, tokenExpires, configFile, null);
	}

	/**
	 * 保留原mykingdee实现
	 * 
	 * @return
	 */
	private static String getDefaultConfigFile() {

		Properties prop = new Properties();
		String configFile = "";
		String defaultConfigFileName = "config.properties";
		InputStream is = LtpaTokenManager.class.getResourceAsStream(defaultConfigFileName);
		try {
			prop.load(is);
			configFile = prop.getProperty("config.path");
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return configFile;
	}

	/**
	 * @param canonicalUser
	 * @param tokenCreation
	 * @param tokenExpires
	 * @param configFile
	 * @return
	 */
	public static LtpaToken generate(String canonicalUser, Date tokenCreation, Date tokenExpires, String configFile,
			String authPattern) {
		boolean isLMBCSEncode = false;
		if (properties != null) {
			String isLMBC = properties.getProperty(ISLMBCSENCODE);
			if (isLMBC != null && "true".equals(isLMBC)) {
				isLMBCSEncode = true;
			}
		}
		return generate(canonicalUser, tokenCreation, tokenExpires, configFile, isLMBCSEncode, authPattern);
	}

	public static void main(String[] args) {
		byte[] b = new byte[4];
		for (byte i = 0; i < b.length; i++) {
			b[i] = (byte) (Math.abs(Math.random() * 127));
			System.out.println(b[i]);
		}
	}

	/**
	 * @param canonicalUser
	 * @param tokenCreation
	 * @param tokenExpires
	 * @param configFile
	 * @param isLMBCSEncode
	 * @return
	 */
	public static LtpaToken generate(String canonicalUser, Date tokenCreation, Date tokenExpires, String configFile,
			boolean isLMBCSEncode, String authPattern) {
		initConfig(configFile);

		LtpaToken ltpa = new LtpaToken();

		Calendar calendar = Calendar.getInstance();
		MessageDigest md = ltpa.getMessageDigest();
		// - Version 0 is [0x00][0x01][0x02][0x03]
		
		byte[] b = new byte[4];
		for (byte i = 0; i < b.length; i++) {
			b[i] = (byte) (new Random().nextInt(127) + 1);
		}
		
		ltpa.setHeader(b);
		if (isLMBCSEncode) {
			ltpa.setUser(LMBCSUtil.getLMBCSLocalGroupBytes(canonicalUser));
		} else {
			try {
				ltpa.setUser(canonicalUser.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成 catch 块
				ltpa.setUser(canonicalUser.getBytes());
			}
		}

		byte[] token = null;

		calendar.setTime(tokenCreation);
		ltpa.setCreation(Long.toHexString(calendar.getTime().getTime() / 1000 ).toUpperCase().getBytes());
		calendar.setTime(tokenExpires);
		ltpa.setExpires(Long.toHexString(calendar.getTime().getTime() / 1000 ).toUpperCase().getBytes());

		token = LMBCSUtil.concatenate(token, ltpa.getHeader());
		token = LMBCSUtil.concatenate(token, ltpa.getCreation());
		token = LMBCSUtil.concatenate(token, ltpa.getExpires());
		token = LMBCSUtil.concatenate(token, ltpa.getUser());

		md.update(token);

		String secret = null;
		if (authPattern != null && properties.containsKey(authPattern.toLowerCase() + "." + DOMINO_SECRET)) {
			secret = properties.getProperty(authPattern.toLowerCase() + "." + DOMINO_SECRET);
		} else {
			secret = properties.getProperty(DOMINO_SECRET);
		}

		byte[] digest = md.digest(BASE64Util.decodeAsBytes(secret));
		String di = byte2hex(digest);
		ltpa.setDigest(di.getBytes());
		token = LMBCSUtil.concatenate(token, di.getBytes());
		// 对token进行base64编码
		String tokenStr = BASE64Util.encode(token);
		String result = "";
		StringTokenizer st = new StringTokenizer(tokenStr);
		// remove the newline character, for the base64 encoded result
		// added a new line when encounting 57 characters
		while (st.hasMoreTokens()) {
			result += st.nextToken();
		}
		return new LtpaToken(result);
	}

	public static String byte2hex(byte[] b) // 二行制转字符串
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	/**
	 * @param configFile
	 */
	private static void initConfig(String configFile) {
		// 初始配置
		// 只装载一次
		// 移到loadConfig中
		// if(!isConfigLoaded())
		// {
		loadConfig(configFile);
		// }
	}

	/**
	 * Validates the SHA-1 digest of the token with the Domino secret key.
	 * 
	 * @param ltpaToken
	 *            Description of the Parameter
	 * @return The valid value
	 */
	public static boolean isValid(String ltpaToken) {
		checkConfig();
		LtpaToken ltpa = new LtpaToken(LtpaToken.decodeToken(ltpaToken));
		boolean result = ltpa.isValid(properties.getProperty(DOMINO_SECRET));
		System.out.println("LTPA token isValid result:" + result);
		if (!result)
			System.out.println("LTPA token compare false, token:" + ltpaToken);
		return result;
	}

	/**
	 * Validates the SHA-1 digest of the token with the Domino secret key.
	 * 
	 * @param ltpaToken
	 *            Description of the Parameter
	 * @return The valid value
	 */
	public static boolean isValid(String ltpaToken, String authPattern) {
		loadConfig("");
		checkConfig();
		LtpaToken ltpa = new LtpaToken(LtpaToken.decodeToken(ltpaToken));
		String secret = null;
		if (properties.containsKey(authPattern.toLowerCase() + "." + DOMINO_SECRET)) {
			secret = properties.getProperty(authPattern.toLowerCase() + "." + DOMINO_SECRET);
		} else {
			secret = properties.getProperty(DOMINO_SECRET);
		}
		boolean result = ltpa.isValid(secret);
		System.out.println("LTPA token isValid result:" + result);
		if (!result)
			System.out.println("LTPA token compare false, token:" + ltpaToken);
		return result;
	}

	/**
	 * Gets the cookie attribute of the LtpaToken object
	 * 
	 * @return The cookie value
	 */
	public static Cookie toCookie(String ltpaToken) {
		checkConfig();
		Cookie cookie = new Cookie(COOKIE_NAME, ltpaToken);
		String domain = properties.getProperty(COOKIE_DOMAIN);
		if (!(domain == null || "".equals(domain))) {
			cookie.setDomain(domain);
		}
		cookie.setPath("/");
		cookie.setSecure(false);
		cookie.setMaxAge(-1);
		return cookie;
	}

	private static void checkConfig() {
		if (!isConfigLoaded()) {
			throw new ConfigurationError("LtpaToken properties is unloaded properly. ");
		}

	}

	/**
	 * @return 返回 isConfigLoaded。
	 */
	private static boolean isConfigLoaded() {
		return isConfigLoaded;
	}

	/**
	 * @return portalConfig/LtpaToken.properties's full pathname
	 */
	public static String getDefaultLtpaConfig() {
		return "LtpaToken.properties";
	}

	/**
	 * 载入缺省配置
	 */
	public static synchronized void loadDefaultConfig() {
		loadConfig(getDefaultLtpaConfig());
	}

	/**
	 * 比较是否为合法TOKEN，给基于TOKEN的认证方式用
	 * 
	 * @param token
	 * @param userNumber
	 * @return
	 */
	public static boolean compare(String token, String userNumber) {
		boolean result = false;
		LtpaToken lt = new LtpaToken(LtpaToken.decodeToken(token));
		String username = lt.getUsername();
		System.out.println("token user =" + username+ ", userNumber= " + userNumber);
		result = (username == null ? false : (username.equals(userNumber)));
		System.out.println("LTPA token compare result:" + result);
		if (!result)
			System.out.println("LTPA token compare false, token:" + token);
		return result;
	}

	/**
	 * 校验LTPA Token的合法性
	 * 
	 * @author 林培森 2010.12.06
	 * 
	 * @param path
	 *            密钥文件路径
	 * @param token
	 *            LTPA Token串
	 * @param userNumber
	 *            用户账号
	 * @return LTPA Token是否合法
	 */
	public static boolean verifyToken(String path, String token, String userNumber) {
		boolean result = false;
		if (path == null || path.trim().length() == 0) {
			LtpaTokenManager.loadDefaultConfig();
		} else {
			LtpaTokenManager.loadConfig(path);
		}
		boolean isValid = LtpaTokenManager.isValid(LtpaToken.decodeToken(token));

		if (isValid) {
			result = LtpaTokenManager.compare(LtpaToken.decodeToken(token), userNumber);
			if (!result)
				System.out.println("LTPA token compare false, token:" + token);
		} else {
			System.out.println("LTPA token isValid false, token:" + token);
		}

		return result;
	}

	// 按校验器来验证token是否合法，不同校验器可以拥有不同的密码
	public static boolean verifyToken(String path, String token, String userNumber, String authPattern) {
		boolean result = false;
		if (path == null || path.trim().length() == 0) {
			LtpaTokenManager.loadDefaultConfig();
		} else {
			LtpaTokenManager.loadConfig(path);
		}
		boolean isValid = LtpaTokenManager.isValid(LtpaToken.decodeToken(token), authPattern);

		if (isValid) {
			result = LtpaTokenManager.compare(LtpaToken.decodeToken(token), userNumber);
			if (!result)
				System.out.println("LTPA token compare false, token:" + token);
		} else {
			System.out.println("LTPA token isValid false, token:" + token);
		}

		return result;
	}

}
