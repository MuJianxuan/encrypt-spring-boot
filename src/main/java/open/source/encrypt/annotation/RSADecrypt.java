package open.source.encrypt.annotation;


import open.source.encrypt.constant.ParameterType;

import java.lang.annotation.*;

/**
 * @author Rao
 * @Mag RSA入参解密注解
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RSADecrypt {

    /** 参数类型 json/String参数 **/
    ParameterType type() default ParameterType.JSON_TYPE;

}
