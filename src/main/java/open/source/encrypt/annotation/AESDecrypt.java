package open.source.encrypt.annotation;

import open.source.encrypt.constant.ParameterType;

import java.lang.annotation.*;

/**
 * @author Rao
 * @Msg AES 解密注解
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AESDecrypt {

    /**
     * 参数类型 json/String
     **/
    ParameterType type() default ParameterType.JSON_TYPE;

}
