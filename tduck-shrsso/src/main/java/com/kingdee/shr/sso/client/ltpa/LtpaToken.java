/*
 * FROM: http://www-12.lotus.com/ldd/doc/tools/c/6.5/api65ug.nsf/0/ceda2cb8df47607f85256c3d005f816d?OpenDocument
 * 
 * The format of the Domino Single Sign-On token is as follows:
 * [token] = BASE64([header][creation time][expiration time][username][SHA-1 hash])
 * Where the SHA-1 hash is taken over the token data and the shared secret: 
 * [SHA-1 hash] = SHA-1([header][creation time][expiration time][username][shared secret]) 
 * 
 * FROM: http://www.looseleaf.net/Looseleaf/Forum.nsf/0/3F74621D2C81902885256D010066F0B8?OpenDocument
 * 
 * It's a little rough around the edges, but I whipped up a little class based
 *  on previous post information. It only works with Domino SSO tokens, not
 *  Domino/WebSphere tokens. I'm planning on writing a realm for standalone
 *  Tomcat and a module for JBoss/Tomcat using this class. The only thing that
 *  wasn't specified in previous posts was the first 4 bytes of the token.
 *  Parsing a domino created token revealed 0,1,2,3 as the secret bytes.
 *  Might be different for your server. Let me know if it works for anyone else.
 * 
 * PROBLEM: You will have problems, if the domino name contains chars greater
 *  then 0x80!! You need LMBCS encode the username! -- Solved
 */

//$Id: LtpaToken.java,v 1.4 2007-07-11 08:21:56 feng_ren Exp $
package com.kingdee.shr.sso.client.ltpa;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.kingdee.shr.sso.client.util.BASE64Util;
import com.kingdee.shr.sso.client.util.LMBCSUtil;

/**
 * Lightweight Third Party Authentication. Generates and validates ltpa tokens
 * used in Domino single sign on environments. Does not work with WebSphere SSO
 * tokens. You need a properties file named LtpaToken.properties which holds two
 * properties.
 * <pre>
 * 1) domino.secret=The base64 encoded secret found in the field LTPA_DominoSecret in the SSO configuration document.
 * 2) cookie.domain=The domain you want generated cookies to be from. e.g. '.domain.com' (Note the leading dot)
 * </pre>
 *
 * @author $Author: feng_ren $
 * @version $Revision: 1.4 $ $Date: 2007-07-11 08:21:56 $
 */
/**
 * updated by linhh on 2006.2.16
 *
 * 
 */
public class LtpaToken {

   
    private byte[] creation;

    private Date creationDate;

    private byte[] digest;

    private byte[] expires;

    private Date expiresDate;

    @SuppressWarnings("unused")
	private byte[] hash;

    private byte[] header;

    private String tokenStr;

    private byte[] rawToken;

    private byte[] user;

    /**
     * Constructor for the LtpaToken object
     */
    public LtpaToken() {
        init();
    }

    /**
     * Constructor for the LtpaToken object
     * 
     * @param token
     *            Description of the Parameter
     */
    public LtpaToken(String token) {
        init();
        tokenStr = token;
        rawToken = BASE64Util.decodeAsBytes(token);
        user = new byte[(rawToken.length) - 60];
        for (int i = 0; i < 4; i++) {
            header[i] = rawToken[i];
        }
        for (int i = 4; i < 12; i++) {
            creation[i - 4] = rawToken[i];
        }
        for (int i = 12; i < 20; i++) {
            expires[i - 12] = rawToken[i];
        }
        for (int i = 20; i < (rawToken.length - 40); i++) {
            user[i - 20] = rawToken[i];
        }
        for (int i = (rawToken.length - 40); i < rawToken.length; i++) {
            digest[i - (rawToken.length - 40)] = rawToken[i];
        }
        creationDate = new Date(Long.parseLong(new String(creation), 16) * 1000);
        expiresDate = new Date(Long.parseLong(new String(expires), 16) * 1000);
    }



    /**
     * Gets the creationDate attribute of the LtpaToken object
     * 
     * @return The creationDate value
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Creates a new SHA-1 <code>MessageDigest</code> instance.
     * 
     * @return The instance.
     */
    public MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-1");
//        	  return MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException nsae) {
        }
        return null;
    }

    /**
     * Gets the expiresDate attribute of the LtpaToken object
     * 
     * @return The expiresDate value
     */
    public Date getExpiresDate() {
        return expiresDate;
    }

    /**
     * Gets the user attribute of the LtpaToken object
     * 
     * @return The user value
     */
    public String getUsername() {
    	String userName=null;
    	try {
			userName=new String(user,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch 块
			userName=new String(user);
		}
        return userName;
    }
    
    public String getUsername(String code) {
    	String userName=null;
    	try {
			userName=new String(user,code);
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成 catch 块
			userName=new String(user);
		}
        return userName;
    }
    
    private void init() {
        creation = new byte[8];
        digest = new byte[40];
        expires = new byte[8];
        hash = new byte[20];
        header = new byte[4];
    }

    /**
     * Validates the SHA-1 digest of the token with the Domino secret key.
     * @param secretKey 
     * 
     * @return Returns true if valid.
     */
    public boolean isValid(String secretKey) {
    	boolean validDigest = false;
        boolean validDateRange = false;
        byte[] newDigest;
        byte[] bytes = null;
        Date now = new Date();

        MessageDigest md = getMessageDigest();
        bytes = LMBCSUtil.concatenate(bytes, header);
        bytes = LMBCSUtil.concatenate(bytes, creation);
        bytes = LMBCSUtil.concatenate(bytes, expires);
        bytes = LMBCSUtil.concatenate(bytes, user);
        //用传入的domino私钥，建立消息摘要
        bytes = LMBCSUtil.concatenate(bytes, BASE64Util
                .decodeAsBytes(secretKey));

        newDigest = md.digest(bytes);
        
        String di=byte2hex(newDigest);
        validDigest = MessageDigest.isEqual(digest, di.getBytes());
        System.out.println("valid message digest :"+validDigest);
        
        //validDateRange = now.after(creationDate) && now.before(expiresDate);
        //仅对比是否在过期时间前，以避免服务器的时钟不同，导致now.after可能为false
        validDateRange = now.before(expiresDate);
        boolean result = (validDigest && validDateRange );
        /*boolean result = validDigest ;
        System.out.println("result1:"+result);
        //然后再检查否在有效期内
        if(!validDateRange)
        {
        	result = false ;
        	System.out.println("result2:"+result);
        }*/

        System.out.println("creationDate["+creationDate+"]<now["+now+"]<expiresDate["+expiresDate+"],validDateRange:"+validDateRange+",verify result:"+result);
        return result;
    }
    
	 public static String byte2hex(byte[] b) //二行制转字符串
	    {
	     String hs="";
	     String stmp="";
	     for (int n=0;n<b.length;n++)
	      {
	       stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
	       if (stmp.length()==1) hs=hs+"0"+stmp;
	       else hs=hs+stmp;
//	       if (n<b.length-1)  hs=hs+":";
	      }
	     return hs.toUpperCase();
	    }

    /**
     * String representation of LtpaToken object.
     * 
     * @return Returns token String suitable for cookie value.
     */
    public String toString() {
    	return encodeToken(tokenStr);
    }
    
    public String toPlainString() {
    	return tokenStr;
    }

	/**
	 * @return 返回 creation。
	 */
	public byte[] getCreation() {
		return creation;
	}

	/**
	 * @param creation 要设置的 creation。
	 */
	public void setCreation(byte[] creation) {
		this.creation = creation;
	}

	/**
	 * @return 返回 expires。
	 */
	public byte[] getExpires() {
		return expires;
	}

	/**
	 * @param expires 要设置的 expires。
	 */
	public void setExpires(byte[] expires) {
		this.expires = expires;
	}

	/**
	 * @return 返回 header。
	 */
	public byte[] getHeader() {
		return header;
	}

	/**
	 * @param header 要设置的 header。
	 */
	public void setHeader(byte[] header) {
		this.header = header;
	}

	/**
	 * @param creationDate 要设置的 creationDate。
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @param digest 要设置的 digest。
	 */
	public void setDigest(byte[] digest) {
		this.digest = digest;
	}

	/**
	 * @param expiresDate 要设置的 expiresDate。
	 */
	public void setExpiresDate(Date expiresDate) {
		this.expiresDate = expiresDate;
	}

	/**
	 * @param user 要设置的 user。
	 */
	public void setUser(byte[] user) {
		this.user = user;
	}

	/**
	 * @return 返回 user。
	 */
	public byte[] getUser() {
		return user;
	}

	/**
	 * @return 返回 tokenStr。
	 */
	public String getTokenStr() {
		return encodeToken(tokenStr);
	}
	
	/**
	 * 对Token串编码便于在网络中传输，避免特殊字符引起问题，如+号会被WEB服务器转换成空格....
	 * TODO, 最终解决方案最好是限制token的字符集
	 * @param token
	 * @return
	 */
	public static String encodeToken(String token){
		try {
    		//两次encode，避免被request.getParameter()解码一次就变成了原生字符串了
			return URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(token, "UTF-8"), "UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Token encode error[UnsupportedEncodingException]!");
			e.printStackTrace();
		}
		
		return token;
	}
	
	/**
	 * TODO, 最终解决方案最好是限制token的字符集
	 * @see #encodeToken
	 * @param token
	 * @return
	 */
	public static String decodeToken(String token){
		//+号为特殊字符，会被decode成空格或request.getParameter时也被转成了空格
    	if(token.indexOf("+")==-1){
	    	/*@link LtpaToken#toString #getTokenStr*/
	    	try {
	    		String decodeToken1 = URLDecoder.decode(token, "UTF-8");
	    		if(decodeToken1.indexOf("+")==-1){
	    			String decodeToken2 = URLDecoder.decode(decodeToken1, "UTF-8");
		    		if(decodeToken2.indexOf("+")==-1){
		    			return URLDecoder.decode(decodeToken2, "UTF-8");
		    		}else{
		    			return decodeToken2;
		    		}
	    			
	    		}else{
	    			return decodeToken1;
	    		}
			} catch (UnsupportedEncodingException e) {
				System.err.println("Token decode error[UnsupportedEncodingException]!");
				e.printStackTrace();
			}
    	}
		
		return token;
	}
	
	/*public static void main(String[] args){
		String prime = "%2525252BEE";
		String encode = LtpaToken.encodeToken(prime);
		String decode =  LtpaToken.decodeToken(encode);
		System.out.println(encode);
		System.out.println(decode);
		System.out.println(prime.equals(decode));
	}*/

}