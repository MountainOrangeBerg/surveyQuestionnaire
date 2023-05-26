package com.kingdee.shr.sso.client.util;



public class BASE64Decoder {
	static String base64_alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	
	/**
   * 解码原理:将4个字节转换成3个字节. 先读入4个6位(用或运算),每次左移6位,再右移3次,每次8位.
   * 
   * @param data
   *            需解码的Base64字符串。
   * @return byte[]－解码出的字节数组
   */
  public static byte[] decodeBuffer(String data) {
	    String Base64Code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";  
            int page = data.length() / 4 ; 
            byte[] outMessage =new  byte[(page * 3)];  
            char[] message = data.toCharArray();
            for (int i = 0; i < page; i++)  
            {  
                byte[] instr = new byte[4];  
                instr[0] = (byte)Base64Code.indexOf(message[i * 4]);  
                instr[1] = (byte)Base64Code.indexOf(message[i * 4 + 1]);  
                instr[2] = (byte)Base64Code.indexOf(message[i * 4 + 2]);  
                instr[3] = (byte)Base64Code.indexOf(message[i * 4 + 3]);  
                byte[] outstr = new byte[3];  
                outstr[0] = (byte)((instr[0] << 2) ^ ((instr[1] & 0x30) >> 4));  
                if (instr[2] != 64)  
                {  
                    outstr[1] = (byte)((instr[1] << 4) ^ ((instr[2] & 0x3c) >> 2));  
                }  
                else  
                {  
                    outstr[2] = 0;  
                }  
                if (instr[3] != 64)  
                {  
                    outstr[2] = (byte)((instr[2] << 6) ^ instr[3]);  
                }  
                else  
                {  
                    outstr[2] = 0;  
                }  
                outMessage[3*i]=outstr[0];  
                if (outstr[1] != 0)  
                	outMessage[3*i+1]=outstr[1];  
                if (outstr[2] != 0)  
                	outMessage[3*i+2]=outstr[2];  
            }  
            
            int remove=0;
            if(outMessage[outMessage.length-1]==0){
            	remove++;
            }
            
            if(outMessage[outMessage.length-2]==0){
            	remove++;
            }
            
            if(remove>0){
            	return copyOf(outMessage,outMessage.length-remove);
            }
            
            return outMessage; 
  
      }
      
      private static byte[] copyOf(byte[] result, int length){
    	  byte[] newResult = new byte[length];
    	  for(int i=0;i<length;i++){
    		  newResult[i]= result[i];
    	  }
    	  
    	  return newResult;
      }
}
