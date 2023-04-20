package cn.miniants.framework.pagesqlhandle.filter;

/**
 * @author guoqianyou
 * @date 2018-06-25 17:57
 */
public enum FilterGroup {
    $and("$and", "and"),
    $or("$or", "or"),;

    private String value;

    private String desc;

    FilterGroup(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
