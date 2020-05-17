package open.source.encrypt.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author Rao
 * @Date 2020/5/5
 **/
@Setter
@Getter
@ToString
public class Result implements Response {

    private static final long serialVersionUID = -4979234998968472213L;

    private Integer code;

    private String message;

    private Boolean encrypt = false;

    private Object data;

    private Result code(int code){
        this.code = code;
        return this;
    }

    private Result data(Object data){
        this.data = data;
        return this;
    }

    private Result message(String msg){
        this.message = msg;
        return this;
    }

    public Result encrypt(){
        this.encrypt = true;
        return this;
    }

    public static Result success(){
        return new Result().code(200).message("操作成功");
    }


    public static Result success(Object data){
        return new Result().code(200).message("").data(data);
    }

    public static Result error(int code,String message){
        return new Result().code(code).message(message);
    }

}
