package cn.ce.api.exception;


import cn.ce.api.enums.ResultEnum;

public class ResultException extends RuntimeException {
    private Integer code;

    public ResultException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public ResultException(Integer code, String msg) {
        super(msg);
        this.code = code;
        System.out.println("123");
    }
}
