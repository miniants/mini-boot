package cn.miniants.framework.pagesqlhandle.filter;

/**
 * @author guoqianyou
 * @date 2018-06-25 17:51
 */
public enum FilterOperational {
    $eq("$eq", "等于"),
    $ne("$ne", "不等于"),
    $like("$like", "模糊"),
    $in("$in", "包含"),
    $gt("$gt", "大于"),
    $ge("$ge", "大于等于"),
    $lt("$lt", "小于"),
    $le("$le", "小于等于"),
    ;

    private String value;
    private String desc;

    FilterOperational(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
