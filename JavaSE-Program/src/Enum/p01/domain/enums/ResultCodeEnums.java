package Enum.p01.domain.enums;

public enum ResultCodeEnums {
    SUCCESS("200" , "success"),
    FAIL("400" , "fail ")
    ;
    private String code;//状态码
    private String msg;//信息

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    ResultCodeEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
