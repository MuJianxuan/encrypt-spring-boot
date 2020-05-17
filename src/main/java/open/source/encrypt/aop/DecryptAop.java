package open.source.encrypt.aop;

import open.source.encrypt.annotation.AESDecrypt;
import open.source.encrypt.annotation.RSADecrypt;
import open.source.encrypt.constant.ParameterType;
import open.source.encrypt.factory.Encrypt;
import open.source.encrypt.factory.EncryptFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Rao
 * @msg 解密aop 参数传参 对Param参数进行解析  String
 **/
@Component
@Aspect
@Order(1)
public class DecryptAop {

    private final EncryptFactory encryptFactory;

    @Autowired
    public DecryptAop(EncryptFactory encryptFactory) {
        this.encryptFactory = encryptFactory;
    }

    /**
     * 针对RSA加密的字符串进行解密
     * @param pjp
     * @param rsaDecrypt
     * @return
     * @throws Throwable
     */
    @Around("@annotation(rsaDecrypt)")
    public Object around(ProceedingJoinPoint pjp,
                         RSADecrypt rsaDecrypt) throws Throwable {

        /**只对Pram入参进行解密**/
        if(rsaDecrypt.type().equals(ParameterType.JSON_TYPE)){
            return pjp.proceed();
        }

        //对Param 的参数进行解密
        Object[] args = pjp.getArgs();
        if(args.length == 0){
            return pjp.proceed();
        }

        //目前只能针对 字符串进行加密
        Encrypt encrypt = encryptFactory.getEncrypt(rsaDecrypt.annotationType().getName());

        return pjp.proceed(decryptArgs(encrypt, args));
    }

    /**
     * 针对AES加密的字符串进行解密
     * @param pjp
     * @param aesDecrypt
     * @return
     * @throws Throwable
     */
    @Around("@annotation(aesDecrypt)")
    public Object around(ProceedingJoinPoint pjp, AESDecrypt aesDecrypt) throws Throwable {

        Object[] args = pjp.getArgs();

        Encrypt encrypt = encryptFactory.getDecrypt(aesDecrypt.annotationType().getName());
        if(encrypt == null){
            throw new NullPointerException("加密对象不存在");
        }

        return pjp.proceed(decryptArgs(encrypt, args));
    }

    /**
     * 针对字符串入参进行解密
     * @param encrypt
     * @param args
     * @return
     * @throws Exception
     */
    private Object[] decryptArgs(Encrypt encrypt,Object[] args) throws Exception {
        Object[] params = new Object[args.length];
        int i = 0;
        for (Object arg : args) {
            //如果是字符串则需要解密
            if(arg instanceof String){
                String decrypt = encrypt.decrypt((String) arg, encrypt.getDecryptKey());
                params[i] = decrypt.substring(1,decrypt.length()-1);
                continue;
            }
            params[i] = arg;
        }
        return params;
    }

}
