package open.source.encrypt.utils;


import open.source.encrypt.annotation.AESDecrypt;
import open.source.encrypt.annotation.AESEncrypt;
import open.source.encrypt.config.EncryptConfig;
import open.source.encrypt.factory.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 加密方法，是对称的密码算法(加密与解密的密钥一致)，这里使用最大的 256 位的密钥
 *
 * @author Rao
 */
@Component
public class AESUtil implements Encrypt {

    @Autowired
    private EncryptConfig config;

    /**
     * 获得一个 密钥长度为 256 位的 AES 密钥，
     *
     * @return 返回经 BASE64 处理之后的密钥字符串
     */
    public static String getStrKeyAES() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
        // 这里可以是 128、192、256、越大越安全
        keyGen.init(256, secureRandom);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 将使用 Base64 加密后的字符串类型的 secretKey 转为 SecretKey
     *
     * @param strKey
     * @return SecretKey
     */
    public static SecretKey strKey2SecretKey(String strKey) {
        byte[] bytes = Base64.getDecoder().decode(strKey);
        SecretKeySpec secretKey = new SecretKeySpec(bytes, "AES");
        return secretKey;
    }

    /**
     * 加密
     *
     * @param content   待加密内容
     * @param secretKey 加密使用的 AES 密钥
     * @return 加密后的密文 byte[]
     */
    public static byte[] encryptAES(byte[] content, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(content);
    }

    /**
     * 解密
     *
     * @param content   待解密内容
     * @param secretKey 解密使用的 AES 密钥
     * @return 解密后的明文 byte[]
     */
    public static byte[] decryptAES(byte[] content, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(content);
    }

    @Override
    public String decrypt(String data, String key) throws Exception {
        return new String(AESUtil.decryptAES(Base64Util.decode(data), AESUtil.strKey2SecretKey(key)), StandardCharsets.UTF_8);
    }

    @Override
    public String encrypt(String data, String key) throws Exception {
        return Base64Util.encode(AESUtil.encryptAES(data.getBytes(StandardCharsets.UTF_8), AESUtil.strKey2SecretKey(key)));
    }

    @Override
    public String getEncryptKey() {
        return config.getAesKey();
    }

    @Override
    public String getDecryptKey() {
        return config.getAesKey();
    }

    @Override
    public String getEncryptTypeClassName() {
        return AESEncrypt.class.getName();
    }

    @Override
    public String getDecryptTypeClassName() {
        return AESDecrypt.class.getName();
    }
}