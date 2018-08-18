public enum resultCode {
    SUCCESS("200", "成功"),
    FAIL("400", "失败");

    private String code;
    private String msg;

    resultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
