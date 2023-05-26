package com.kingdee.shr.sso.client.util;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;

/**
 * TODO
 * LMBCS (Lotus Multi-Byte Character Set) 简易工具类，没有经过严格测试  
 * @link http://www.batutis.com/i18n/papers/lmbcs/LMBCS.html
 * 
 * @author liyingri
 *
 * TODO
 * @version $Revision: 1.1 $ $Date: 2006-02-16 09:08:11 $
 */
@SuppressWarnings("unchecked")
public final class LMBCSUtil {
    private final static String ULMBCS_GRP_L1 = "0x01"; /* Latin-1 :ibm-850 */

    private final static String ULMBCS_GRP_GR = "0x02"; /* Greek :ibm-851 */

    private final static String ULMBCS_GRP_HE = "0x03"; /* Hebrew :ibm-1255 */

    private final static String ULMBCS_GRP_AR = "0x04"; /* Arabic :ibm-1256 */

    private final static String ULMBCS_GRP_RU = "0x05"; /* Cyrillic :ibm-1251 */

    private final static String ULMBCS_GRP_L2 = "0x06"; /* Latin-2 :ibm-852 */

    private final static String ULMBCS_GRP_TR = "0x08"; /* Turkish :ibm-1254 */

    private final static String ULMBCS_GRP_TH = "0x0B"; /* Thai :ibm-874 */

    private final static String ULMBCS_GRP_JA = "0x10"; /* Japanese :ibm-943 */

    private final static String ULMBCS_GRP_KO = "0x11"; /* Korean :ibm-1261 */

    private final static String ULMBCS_GRP_TW = "0x12"; /* Chinese TC :ibm-950 */

    private final static String ULMBCS_GRP_CN = "0x13"; /* Chinese SC :ibm-1386 */

    private final static String ULMBCS_GRP_UNICODE = "0x14"; /* Unicode (UTF-16) */

    @SuppressWarnings("rawtypes")
	private static Hashtable groupMap = new Hashtable();

    static {
        groupMap.put(new Locale("ar", ""), ULMBCS_GRP_AR);
        groupMap.put(new Locale("be", ""), ULMBCS_GRP_RU);
        groupMap.put(new Locale("bg", ""), ULMBCS_GRP_L2);
        groupMap.put(new Locale("cs", ""), ULMBCS_GRP_L2);
        groupMap.put(new Locale("el", ""), ULMBCS_GRP_GR);
        groupMap.put(new Locale("he", ""), ULMBCS_GRP_HE);
        groupMap.put(new Locale("hu", ""), ULMBCS_GRP_L2);
        groupMap.put(new Locale("iw", ""), ULMBCS_GRP_HE);
        groupMap.put(new Locale("ja", ""), ULMBCS_GRP_JA);
        groupMap.put(new Locale("ko", ""), ULMBCS_GRP_KO);
        groupMap.put(new Locale("mk", ""), ULMBCS_GRP_RU);
        groupMap.put(new Locale("pl", ""), ULMBCS_GRP_L2);
        groupMap.put(new Locale("ro", ""), ULMBCS_GRP_L2);
        groupMap.put(new Locale("ru", ""), ULMBCS_GRP_RU);
        groupMap.put(new Locale("sh", ""), ULMBCS_GRP_L2);
        groupMap.put(new Locale("sk", ""), ULMBCS_GRP_L2);
        groupMap.put(new Locale("sl", ""), ULMBCS_GRP_L2);
        groupMap.put(new Locale("sq", ""), ULMBCS_GRP_L2);
        groupMap.put(new Locale("sr", ""), ULMBCS_GRP_RU);
        groupMap.put(new Locale("th", ""), ULMBCS_GRP_TH);
        groupMap.put(new Locale("tr", ""), ULMBCS_GRP_TR);
        groupMap.put(new Locale("uk", ""), ULMBCS_GRP_RU);
        groupMap.put(new Locale("zh", "TW"), ULMBCS_GRP_TW);
        groupMap.put(new Locale("zh", "HK"), ULMBCS_GRP_TW);
        groupMap.put(new Locale("zh", ""), ULMBCS_GRP_CN);
        groupMap.put(new Locale("zh", "CN"), ULMBCS_GRP_CN);
    }

    public static byte getLMBCGroupId(Locale locale) {
        byte groupId = Byte.decode(ULMBCS_GRP_L1).byteValue();
        if (null == groupMap.get(locale)) {
            String language = locale.getLanguage();
            Locale l = new Locale(language, "");
            if (null != groupMap.get(l)) {
                groupId = Byte.decode(groupMap.get(l).toString()).byteValue();
            }
        } else {
            groupId = Byte.decode(groupMap.get(locale).toString()).byteValue();
        }
        return groupId;
    }

    public static byte getDefaultGroupId() {
        return getLMBCGroupId(Locale.getDefault());
    }

    @SuppressWarnings("unused")
	public static byte[] getLMBCSLocalGroupBytes(String input) {
        byte[] groupId = new byte[] { LMBCSUtil.getDefaultGroupId() };
        byte[] bytes = input.getBytes(); // local encoding
        byte[] result = null;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            Character ch = new Character(c);
            String s = ch.toString();
            if (s.getBytes()[0] < 0)
                result = concatenate(result, groupId);

            result = concatenate(result, s.getBytes());
        }
        return result;
    }

    public static byte[] getLMBCSUnicodeGroupBytes(String input) {
        byte[] unicodeGroupId = new byte[] { Byte.decode(ULMBCS_GRP_UNICODE)
                .byteValue() };
        String s = null;
        byte[] unicodeBytes = null;
        byte[] LMBCSBytes = null;
        byte highByte;
        byte lowByte;
        for (int i = 0; i < input.length(); i++) {
            s = new Character(input.charAt(i)).toString();
            try {
                unicodeBytes = s.getBytes("Unicode");
                highByte = unicodeBytes[2];
                lowByte = unicodeBytes[3];

                if (highByte == 0) {
                    LMBCSBytes = concatenate(LMBCSBytes, new byte[] { lowByte });
                } else {
                    LMBCSBytes = concatenate(LMBCSBytes, unicodeGroupId);
                    LMBCSBytes = concatenate(LMBCSBytes,
                            new byte[] { highByte });
                    LMBCSBytes = concatenate(LMBCSBytes, new byte[] { lowByte });
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return LMBCSBytes;
    }

    /**
     * 连接两个byte数组
     * @param a
     * @param b
     * @return
     */
    public static byte[] concatenate(byte[] a, byte[] b) {
        if (a == null) {
            return b;
        } else {
            byte[] bytes = new byte[a.length + b.length];

            System.arraycopy(a, 0, bytes, 0, a.length);
            System.arraycopy(b, 0, bytes, a.length, b.length);
            return bytes;
        }
    }
}