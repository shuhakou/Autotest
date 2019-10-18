package cn.ce.api.enums;

import lombok.Getter;

@Getter
public enum  ResultEnum  {
    REQUEST_URL(1,"请求接口:"),
    REQUEST_PARAM(2,"请求参数:"),
    EXPECTED_RESULT(3,"接口预期值:"),
   ACTUAL_RESULT(4,"接口返回值:"),
   IS_DEPEND(5,"是否被依赖:"),
    UNREALIZED_FULL_COMPARISON(6,"本用例未实现全量对比"),
    REQUEST_TYPE(7,"请求类型:"),


    NOT_EXEC(10,"此接口不执行"),
    EXPECT_FORMAT_ERROR(20,"预期结果不是json格式"),
    ACTUAL_FIELD_NULL(30,"实际结果无此字段:"),
    ACT_ISNOT_EXP(40,"该字段的预期结果和实际结果不相等："),
    ACTUAL_RESULT_NULL(50,"实际结果为空");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

	public String getMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
