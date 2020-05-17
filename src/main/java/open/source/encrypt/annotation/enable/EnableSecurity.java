package open.source.encrypt.annotation.enable;

import open.source.encrypt.advice.DecryptRequestAdvice;
import open.source.encrypt.advice.EncryptResponseAdvice;
import open.source.encrypt.aop.DecryptAop;
import open.source.encrypt.config.EncryptConfig;
import open.source.encrypt.config.GlobalWebConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Rao
 * @Msg 启用框架注解
 * 导入相关类文件
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DecryptRequestAdvice.class,
        EncryptResponseAdvice.class,
        DecryptAop.class,
        EncryptConfig.class,
        GlobalWebConfig.class
})
public @interface EnableSecurity {


}
