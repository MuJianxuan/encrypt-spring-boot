package open.source.encrypt.utils;

import open.source.encrypt.annotation.RSADecrypt;
import open.source.encrypt.annotation.RSAEncrypt;
import open.source.encrypt.config.EncryptConfig;
import open.source.encrypt.factory.Encrypt;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA Util
 * @author Rao
 * @msg 框架本身是基于服务端的(类设计的好差)
 * 客户端使用公钥加密 服务端使用私钥解密
 * 服务端使用私钥加密 客户端使用公钥解密
 **/
@Component
public class RSAUtil implements Encrypt {

    @Autowired
    private EncryptConfig config;

//    private RSAUtil(){}

    private static final String KEY_ALGORITHM = "RSA";
    private static final int RSA_KEY_SIZE = 1024;
    public final static String UTF8 = "utf-8";

    public final static String PRIVATE_KEY = "private_key";
    public final static String PUBLIC_KEY = "public_key";


    public static Map<String, String> keyMap = new HashMap<>();

    /**密钥处理**/
    public static String formatString(String source) {
        if (source == null) {
            return null;
        }
        return source.replaceAll("\\r", "").replaceAll("\\n", "");
    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        //KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(RSA_KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(PUBLIC_KEY, publicKeyString);
        keyMap.put(PRIVATE_KEY, privateKeyString);
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */

    public static String encryptByPublicKey(String str, String publicKey) throws Exception {
        publicKey = formatString(publicKey);
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(UTF8)));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptByPrivateKey(String str, String privateKey) throws Exception {
        privateKey = formatString(privateKey);
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(UTF8));
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * 私钥加密
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     */
    public static String encryptByPrivateKey(String data, String privateKey) {
        try {
            privateKey = formatString(privateKey);
            byte[] kb = Base64.decodeBase64(privateKey.getBytes(UTF8));
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(kb);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] b = data.getBytes(UTF8);
            byte[] encrypt = cipher.doFinal(b);
            return Base64.encodeBase64String(encrypt);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 公钥解密
     *
     * @param data      解密数据
     * @param publicKey 公钥
     * @return
     */
    public static String decryptByPublicKey(String data, String publicKey) {
        try {
            publicKey = formatString(publicKey);
            byte[] kb = Base64.decodeBase64(publicKey.getBytes(UTF8));
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(kb);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            //Cipher cipher = Cipher.getInstance(RSA_PADDING_KEY);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] b = data.getBytes(UTF8);
            byte[] decrypt = cipher.doFinal(Base64.decodeBase64(b));
            return new String(decrypt, UTF8);
        } catch (Exception e){
            throw new RuntimeException(""+e.getMessage());
        }
    }
    /**
     * 私钥解密
     * @param data 数据
     * @param privateKey 密钥
     * @return
     */
    @Override
    public String decrypt(String data, String privateKey) throws Exception {
        return RSAUtil.decryptByPublicKey(data,privateKey);
    }

    /**
     * 私钥加密
     * @param data 数据
     * @param privateKey 密钥
     * @return
     */
    @Override
    public String encrypt(String data, String privateKey) throws Exception {
        return RSAUtil.encryptByPrivateKey(data,privateKey);
    }

    /**
     * 获取私钥 私钥加密  私钥解密
     * @return
     */
    @Override
    public String getEncryptKey() {
        return config.getRsaPrivateKey();
    }

    @Override
    public String getDecryptKey() {
        return config.getRsaPublicKey();
    }

    /**
     * 获取注解名称
     * @return
     */
    @Override
    public String getEncryptTypeClassName() {
        return RSAEncrypt.class.getName();
    }

    @Override
    public String getDecryptTypeClassName() {
        return RSADecrypt.class.getName();
    }


    //用于封装随机产生的公钥与私钥
    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        genKeyPair();
        //加密字符串
        String message = "hello";
        System.out.println("随机生成的公钥为:" + keyMap.get(PUBLIC_KEY));
        System.out.println("随机生成的私钥为:" + keyMap.get(PRIVATE_KEY));

        //公钥加密、私钥解密
//        String messageEn = RSAUtil.encryptByPublicKey(message, keyMap.get(PUBLIC_KEY));
//        System.out.println(message + "公钥加密后的字符串为:" + messageEn);
//        String messageDe = decryptByPrivateKey(messageEn, keyMap.get(PRIVATE_KEY));
//        System.out.println("私钥解密字符串为:" + messageDe);

        //私钥加密、公钥解密
        String enc = encryptByPrivateKey(message, keyMap.get(PRIVATE_KEY));
        System.out.println(message + "私钥加密后的字符串为:" + enc);
        String de = decryptByPublicKey(enc, keyMap.get(PUBLIC_KEY));
        System.out.println("公钥解密后的字符串:" + de);

    }
}
