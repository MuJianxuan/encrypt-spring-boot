package open.source.encrypt.utils;


import org.apache.tomcat.util.codec.binary.Base64;

/**
 * Base64
 * @author Rao
 **/
public class Base64Util{

    /**
     * Decoding to binary string转字节数组
     * @param base64 base64
     * @return byte
     * @throws Exception Exception
     */
    public static byte[] decode(String base64){
        return Base64.decodeBase64(base64);
    }

    /**
     * Binary encoding as a string 字节数组转string
     * @param bytes byte
     * @return String
     * @throws Exception Exception
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }
}
