package open.source.encrypt.annotation;

import java.lang.annotation.*;

/**
 * @author Rao
 * @msg Rsa返回值加密注解
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RSAEncrypt {
}
