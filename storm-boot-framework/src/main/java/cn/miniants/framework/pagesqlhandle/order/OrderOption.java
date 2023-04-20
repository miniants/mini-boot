package cn.miniants.framework.pagesqlhandle.order;

public enum OrderOption {
    ASC("ASC", "ASC"),
    DESC("DESC", "DESC"),
    ;

    private String value;

    private String desc;

    OrderOption(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
