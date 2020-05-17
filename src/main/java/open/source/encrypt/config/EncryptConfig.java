package open.source.encrypt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Rao
 * @msg 统一管加密配置
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "encrypt")
public class EncryptConfig {

    private Boolean enable = Boolean.TRUE;

    /**
     * Rsa私钥
     */
    private String rsaPrivateKey;

    /**
     * Rsa公钥
     */
    private String rsaPublicKey;

    /**
     * AES 密钥
     */
    private String aesKey;

}
