package open.source.encrypt.factory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Rao
 * @Msg 加密数据获取接口
 **/
@Getter
@Setter
@ToString
public class EncryptData implements Serializable {
    private static final long serialVersionUID = 237211734986143099L;

    /**加密数据**/
    private String data;

}
