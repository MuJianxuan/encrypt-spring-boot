package open.source.encrypt.advice;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import open.source.encrypt.config.EncryptConfig;
import open.source.encrypt.factory.Encrypt;
import open.source.encrypt.factory.EncryptFactory;
import open.source.encrypt.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author Rao
 * @msg 对数据根据注解类型进行 加密
 **/
@ControllerAdvice
@Order(20)
public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private EncryptFactory encryptFactory;

    @Autowired
    private EncryptConfig config;


    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //如果没有开启加密框架的注解则不执行该
        if (!config.getEnable()) {
            return false;
        }
        //true才会执行下面的beforeBodyWrite
        Objects.requireNonNull(methodParameter.getMethod());
        Annotation[] annotations = methodParameter.getMethod().getAnnotations();

        //如果返回值的RestController上的注解为空则不需要验证
        if (annotations.length == 0) {
            return false;
        }

        return encryptFactory.existEncrypt(annotations);
    }

    /**
     * 需要实现动态获取当前加密对象以及
     *
     * @param returnBody         返回的数据集
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object returnBody, MethodParameter methodParameter,
                                  MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        //获取需加密数据的对象
        if (returnBody == null) {
            throw new NullPointerException("[封装加密数据] --> 返回的需要加密数据不能为 null");
        }
        String data = JSON.toJSONString(returnBody);
        Annotation[] annotations = Objects.requireNonNull(methodParameter.getMethod()).getAnnotations();
        //获取对象
        Encrypt encrypt = encryptFactory.getEncrypt(annotations);

        return Result.success(encrypt.encrypt(data, encrypt.getEncryptKey())).encrypt();
    }
}
