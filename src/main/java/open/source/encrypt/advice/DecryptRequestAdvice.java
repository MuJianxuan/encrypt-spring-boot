package open.source.encrypt.advice;


import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import open.source.encrypt.factory.Encrypt;
import open.source.encrypt.factory.EncryptData;
import open.source.encrypt.factory.EncryptFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Rao
 * @Msg 对RequestBody请求参数进行解密
 **/
@ControllerAdvice
@Order(20)
public class DecryptRequestAdvice implements RequestBodyAdvice {

    @Autowired
    private EncryptFactory encryptFactory;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        //true才会执行下面的beforeBodyWrite
        Objects.requireNonNull(methodParameter.getMethod());
        Annotation[] annotations = methodParameter.getMethod().getAnnotations();

        //如果返回值的RestController上的注解为空则不需要验证
        if (annotations.length == 0) {
            return false;
        }

        return encryptFactory.existDecrypt(annotations);
    }

    @SneakyThrows
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        //获取传入的加密数据
        String content = new BufferedReader(new InputStreamReader(httpInputMessage.getBody()))
                .lines().collect(Collectors.joining(System.lineSeparator()));

        if (StringUtils.isEmpty(content)) {
            throw new IllegalArgumentException("加密串为空");
        }
        EncryptData encryptData = JSONObject.parseObject(content, EncryptData.class);

        if (encryptData == null) {
            throw new IllegalArgumentException("加密串为空");
        }

        Annotation[] annotations = Objects.requireNonNull(methodParameter.getMethod()).getAnnotations();
        //获取对象
        Encrypt encrypt = encryptFactory.getDecrypt(annotations);
        String decryptData = encrypt.decrypt(encryptData.getData(), encrypt.getDecryptKey());

        return new DecryptHttpInputMessage(httpInputMessage, decryptData);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }
}
