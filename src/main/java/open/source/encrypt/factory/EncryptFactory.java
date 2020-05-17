package open.source.encrypt.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rao
 **/
@Component
public class EncryptFactory {

    @Autowired
    private List<Encrypt> encrypts;

    public Boolean existEncrypt(Annotation[] annotations){
        Map<String, Encrypt> encryptMap = encrypts.stream().collect(Collectors.toMap(Encrypt::getEncryptTypeClassName, encrypt -> encrypt));
        for (Annotation annotation : annotations){
            Encrypt encrypt = encryptMap.get(annotation.annotationType().getName());
            if(encrypt != null){
                return true;
            }
        }
        return false;
    }

    public Boolean existDecrypt(Annotation[] annotations){
        Map<String, Encrypt> encryptMap = encrypts.stream().collect(Collectors.toMap(Encrypt::getDecryptTypeClassName, encrypt -> encrypt));
        for (Annotation annotation : annotations){
            Encrypt encrypt = encryptMap.get(annotation.annotationType().getName());
            if(encrypt != null){
                return true;
            }
        }
        return false;
    }

    public Encrypt getEncrypt(String className){
        Map<String, Encrypt> encryptMap = encrypts.stream().collect(Collectors.toMap(Encrypt::getEncryptTypeClassName, encrypt -> encrypt));
        Encrypt encrypt = encryptMap.get(className);
        if(encrypt == null){
            throw new NullPointerException("[根据类名获取加密]加密对象获取失败"+className);
        }
        return encrypt;
    }



    /**
     * 获取加密对象
     * @param annotations
     * @return
     */
    public Encrypt getEncrypt(Annotation[] annotations) {
        Map<String, Encrypt> encryptMap = encrypts.stream().collect(Collectors.toMap(Encrypt::getEncryptTypeClassName, encrypt -> encrypt));
        for (Annotation annotation : annotations){
            Encrypt encrypt = encryptMap.get(annotation.annotationType().getName());
            if(encrypt != null){
                return encrypt;
            }
        }
        throw new RuntimeException("获取加密对象失败确定注入改对象");
    }

    /**
     * 获取解密对象
     * @param annotations
     * @return
     */
    public Encrypt getDecrypt(Annotation[] annotations) {
        Map<String, Encrypt> encryptMap = encrypts.stream().collect(Collectors.toMap(Encrypt::getDecryptTypeClassName, encrypt -> encrypt));
        for (Annotation annotation : annotations){
            Encrypt encrypt = encryptMap.get(annotation.annotationType().getName());
            if(encrypt != null){
                return encrypt;
            }
        }
        throw new RuntimeException("获取解密对象失败确定注入改对象");
    }

    public Encrypt getDecrypt(String className){
        Map<String, Encrypt> encryptMap = encrypts.stream().collect(Collectors.toMap(Encrypt::getDecryptTypeClassName, encrypt -> encrypt));
        Encrypt encrypt = encryptMap.get(className);
        if(encrypt == null){
            throw new NullPointerException("[根据类名获取加密]加密对象获取失败"+className);
        }
        return encrypt;
    }
}
