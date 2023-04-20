package cn.miniants.framework.pagesqlhandle.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.miniants.toolkit.JSONUtil;

import java.util.*;

/**
 * @author guoqianyou
 * @date 2018-06-25 17:23
 */
public class FilterHandlerUtil {
    private static final LinkedHashMap<String, FilterGroup> GROUP_ENUM_MAP = EnumUtil.getEnumMap(FilterGroup.class);
    private static final LinkedHashMap<String, FilterOperational> OP_ENUM_MAP = EnumUtil.getEnumMap(FilterOperational.class);

    private String filterStr;
    private Filter filter;

    public FilterHandlerUtil(String filterStr) {
        if (StrUtil.isNotEmpty(filterStr)) {
            this.filterStr = filterStr;
            // 此处需要对 filter 进行过滤 剔除空的过滤条件
            Map<String, Object> stringObjectMap = JSONUtil.readMap(filterStr);
            Filter temp_filter = handleFilterJsonStr(FilterGroup.$and, stringObjectMap);
            this.filter = cleanFilter(temp_filter);
        }
    }

    public QueryWrapper getFilterWrapper() {
        QueryWrapper<?> condition = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(this.filter) && (CollUtil.isNotEmpty(this.filter.getFilterBeanList()) || CollUtil.isNotEmpty(this.filter.getChildFilterList()))) {
            groupHandle(condition, this.filter);
        }
        return condition;
    }

    private Filter cleanFilter(Filter filter) {
        if (ObjectUtil.isNotNull(filter)) {
            if (CollUtil.isEmpty(filter.getFilterBeanList())) {
                if (CollUtil.isEmpty(filter.getChildFilterList())) {
                    filter = null;
                } else {
                    List<Filter> tempChildFilter = new ArrayList<>(filter.getChildFilterList().size());
                    filter.getChildFilterList().forEach(childFilter -> tempChildFilter.add(cleanFilter(childFilter)));
                    tempChildFilter.removeIf(Objects::isNull);
                    if (CollUtil.isEmpty(tempChildFilter)) {
                        filter = null;
                    } else if (tempChildFilter.size() == 1) {
                        filter = tempChildFilter.get(0);
                    } else {
                        filter.setChildFilterList(tempChildFilter);
                    }
                }
            }
        }
        return filter;
    }

    private Filter handleFilterJsonStr(FilterGroup filterGroup, Map<String, Object> stringObjectMap) {
        Filter filter = new Filter();
        filter.setFilterGroup(filterGroup);
        if (CollUtil.isNotEmpty(stringObjectMap)) {
            stringObjectMap.forEach((key, value) -> {
                if (ObjectUtil.isNotEmpty(value)) {
                    if (checkValueSimple(value)) {
                        filter.addFilterBean(key, FilterOperational.$eq, value);
                    } else {
                        if (GROUP_ENUM_MAP.containsKey(key)) {
                            filter.addChildFilter(handleFilterJsonStr(GROUP_ENUM_MAP.get(key), (Map<String, Object>) value));
                        } else {
                            Map<String, Object> childMap = (Map<String, Object>) value;
                            childMap.forEach((op, childValue) -> {
                                FilterOperational filterOperational = OP_ENUM_MAP.get(op);
                                Assert.notNull(filterOperational, "不支持的操作符：{}", op);
                                filter.addFilterBean(key, filterOperational, childValue);
                            });
                        }
                    }
                }
            });
        }

        return filter;
    }


    private boolean checkValueSimple(Object value) {
        if (value instanceof String) {
            return true;
        } else if (value instanceof Number) {
            return true;
        } else if (value instanceof Boolean) {
            return true;
        }
        return false;
    }


    private void groupHandle(QueryWrapper<?> queryWrapper, Filter filter) {
        FilterGroup filterGroup = filter.getFilterGroup();
        switch (filterGroup) {
            case $and:
                queryWrapper.and(condition -> {
                    filter.getFilterBeanList().forEach(childFilterBean -> {
                        opHandle(condition, childFilterBean);
                    });
                    filter.getChildFilterList().forEach(childFilter -> {
                        groupHandle(condition, childFilter);
                    });
                });
                break;
            case $or:
                queryWrapper.or(condition -> {
                    filter.getFilterBeanList().forEach(childFilterBean -> {
                        opHandle(condition, childFilterBean);
                    });
                    filter.getChildFilterList().forEach(childFilter -> {
                        groupHandle(condition, childFilter);
                    });
                });
                break;
        }
    }

    private void opHandle(QueryWrapper<?> queryWrapper, Filter.FilterBean filterBean) {
        FilterOperational filterOperational = OP_ENUM_MAP.get(filterBean.getOp());
        boolean notEmpty = StrUtil.isNotEmpty(filterBean.getValue().toString());
        switch (filterOperational) {
            case $eq:
                queryWrapper.eq(notEmpty, filterBean.getFiled(), filterBean.getValue());
                break;
            case $ne:
                queryWrapper.ne(notEmpty, filterBean.getFiled(), filterBean.getValue());
                break;
            case $like:
                queryWrapper.like(notEmpty, filterBean.getFiled(), filterBean.getValue().toString());
                break;
            case $in:
                Collection<?> value = (Collection<?>) filterBean.getValue();
                queryWrapper.in(value.size() > 0, filterBean.getFiled(), value);
                break;
            case $gt:
                queryWrapper.gt(notEmpty, filterBean.getFiled(), filterBean.getValue());
                break;
            case $ge:
                queryWrapper.ge(notEmpty, filterBean.getFiled(), filterBean.getValue());
                break;
            case $lt:
                queryWrapper.lt(notEmpty, filterBean.getFiled(), filterBean.getValue());
                break;
            case $le:
                queryWrapper.le(notEmpty, filterBean.getFiled(), filterBean.getValue());
                break;
        }
    }

}
