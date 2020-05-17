package open.source.encrypt.factory;

/**
 * @author Rao
 **/
public interface Encrypt {
    /**
     * 数据解密
     *
     * @param data 数据
     * @param key  密钥
     * @return
     */
    String decrypt(String data, String key) throws Exception;

    /**
     * 数据加密
     *
     * @param data 数据
     * @param key  密钥
     * @return
     */
    String encrypt(String data, String key) throws Exception;

    /**
     * 获取加解密的key
     *
     * @return
     */
    String getEncryptKey();

    /**
     * 获得解密key
     *
     * @return
     */
    String getDecryptKey();

    /**
     * 获取加密类全类名
     */
    String getEncryptTypeClassName();

    /**
     * 获取解密类的全类名
     */
    String getDecryptTypeClassName();
}
