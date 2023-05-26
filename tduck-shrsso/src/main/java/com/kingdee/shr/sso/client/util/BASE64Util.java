package com.kingdee.shr.sso.client.util;

public class BASE64Util
{
	private BASE64Util()
	{
	}

	public static BASE64Encoder getBASE64Encoder()
	{
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder;
	}
	
	public static BASE64Decoder getBASE64Decoder()
	{
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder;
	}

	@SuppressWarnings("static-access")
	public static String decodeAsString(String input)
	{
		BASE64Decoder decoder = getBASE64Decoder();
		String decoded = "";

//		try
//		{
			byte[] buf = decoder.decodeBuffer(input);
			decoded = new String(buf);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
		return decoded;
	}

	@SuppressWarnings("static-access")
	public static byte[] decodeAsBytes(String input)
	{
		BASE64Decoder decoder = getBASE64Decoder();
		byte[] buf = null;

//		try
//		{
			buf = decoder.decodeBuffer(input);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
		return buf;
	}
	
	@SuppressWarnings("static-access")
	public static String encode(byte[] buf)
	{
		BASE64Encoder encoder = getBASE64Encoder();
		String encoded = "";

		encoded = encoder.encodeBuffer(buf);
		return encoded;
	}
}
