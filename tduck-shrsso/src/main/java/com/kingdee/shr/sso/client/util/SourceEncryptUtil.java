package com.kingdee.shr.sso.client.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.kingdee.shr.CryptException;


public class SourceEncryptUtil {
	private static final byte[] pwd = { (byte)0xAE,(byte)0x9B,0x7F,0x34,(byte)0xF8,(byte)0x94,0x02,0x5D};
	private static final String ALGORITHM = "DES";
	public String encrypt(String key, String srcStr) throws CryptException
	{
		SecretKey deskey = new SecretKeySpec(pwd, ALGORITHM);
		Cipher cipher;
		try
		{
			cipher = Cipher.getInstance( ALGORITHM );
			cipher.init( Cipher.ENCRYPT_MODE, deskey);
			
			byte[] resByte = cipher.doFinal(srcStr.getBytes());
			String res = SourcePassBase64Encoder.byteArrayToBase64(resByte);
//			BASE64Encoder encoder = new BASE64Encoder();
//			String res =   encoder.encode(resByte);
			//gzb 修改使用sun内部类，改用util中的实现
			return res;
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new CryptException("NoSuchAlgorithmException",e);
		}
		catch (NoSuchPaddingException e)
		{

			throw new CryptException("NoSuchPaddingException",e);
		}
		catch (InvalidKeyException e)
		{

			throw new CryptException("InvalidKeyException",e);
		}
		catch (IllegalStateException e)
		{

			throw new CryptException("IllegalStateException",e);
		}
		catch (IllegalBlockSizeException e)
		{

			throw new CryptException("IllegalBlockSizeException",e);
		}
		catch (BadPaddingException e)
		{

			throw new CryptException("BadPaddingException",e);
		}
	}
	

	public String decrypt(String key, String encStr)  throws CryptException
	{
		SecretKey deskey = new SecretKeySpec(pwd, ALGORITHM);
		
		Cipher cipher;
		try
		{
			cipher = Cipher.getInstance( ALGORITHM );
			cipher.init( Cipher.DECRYPT_MODE, deskey);
//			BASE64Decoder decode = new BASE64Decoder();
//			byte[] sourceByte = decode.decodeBuffer(encStr);
//			gzb 修改使用sun内部类，改用util中的实现
			byte[] sourceByte = SourcePassBase64Encoder.base64ToByteArray(encStr);
			
			String res =  new String(cipher.doFinal(sourceByte));
			return res;
		}
		catch (NoSuchAlgorithmException e)
		{

			throw new CryptException("NoSuchAlgorithmException",e);
		}
		catch (NoSuchPaddingException e)
		{

			throw new CryptException("NoSuchPaddingException",e);
		}
		catch (InvalidKeyException e)
		{

			throw new CryptException("InvalidKeyException",e);
		}
		catch (IllegalStateException e)
		{

			throw new CryptException("IllegalStateException",e);
		}
		catch (IllegalBlockSizeException e)
		{

			throw new CryptException("IllegalBlockSizeException",e);
		}
		catch (BadPaddingException e)
		{
			throw new CryptException("BadPaddingException",e);
		}
//		catch (IOException e)
//		{
//			logger.error("",e);
//			throw new CryptException("IOException",e);
//		}
	}
	
	//	转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";

        for (int n=0;n<b.length;n++) {
            stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
            else hs=hs+stmp;
            if (n<b.length-1)  hs=hs+",0x";
        }
        return hs.toUpperCase();
    }
}
