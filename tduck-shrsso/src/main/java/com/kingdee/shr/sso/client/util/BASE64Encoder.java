package com.kingdee.shr.sso.client.util;


public class BASE64Encoder {
public static String encodeBuffer(byte[] bytes) {
	 char[] Base64Code = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T',  
             'U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n',  
             'o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7',  
         '8','9','+','/','='};  
         byte empty = (byte)0;  
      StringBuffer outmessage = new StringBuffer(); 
         int messageLen = bytes.length;  
         int page = messageLen / 3;  
         int use = 0;  
         if ((use = messageLen % 3) > 0)  
         {  

        	 byte[] newbyte= new byte[messageLen+3-use];
        	 
        	 for(int i=0;i<messageLen;i++){
        		 newbyte[i]=bytes[i];
        	 }
        	 
             for (int i = 0; i < 3 - use; i++)  
            	 newbyte[messageLen+i] = empty;
             
             page++;  
             bytes=newbyte;
         }  
         outmessage = new StringBuffer(page*4);   
         for (int i = 0; i < page; i++)  
         {  
             byte[] instr = new byte[3];  
             instr[0] = (byte)bytes[i * 3];  
             instr[1] = (byte)bytes[i * 3 + 1];  
             instr[2] = (byte)bytes[i * 3 + 2];  
             int[] outstr = new int[4];  
             outstr[0] = (instr[0] >>> 2) & 0x3f;  
             outstr[1] = ((instr[0] & 0x03) << 4) ^ ((instr[1] >>> 4) & 0xf);  
             if (!(instr[1]==empty))  
                 outstr[2] = ((instr[1] & 0x0f) << 2) ^ ((instr[2] >>> 6) & 0x3);  
             else  
                 outstr[2] = 64;  
             if (!(instr[2]==empty))  
                 outstr[3] = (instr[2] & 0x3f);  
             else  
                 outstr[3] = 64;  
             
             outmessage.append(Base64Code[outstr[0]]);  
             outmessage.append(Base64Code[outstr[1]]);  
             outmessage.append(Base64Code[outstr[2]]);  
             outmessage.append(Base64Code[outstr[3]]);  
         }  
         return outmessage.toString();  
     }

    
}
