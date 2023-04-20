package cn.miniants.framework.pagesqlhandle.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.miniants.toolkit.JSONUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author guoqi
 * @date 2019/4/18
 */
public class OrderHandlerUtil {
    private static final LinkedHashMap<String, OrderOption> ORDER_OPTION_MAP = EnumUtil.getEnumMap(OrderOption.class);

    private String orderStr;
    private List<Order> orderList;

    public OrderHandlerUtil(String orderStr) {
        if (StrUtil.isNotEmpty(orderStr)) {
            if (!cn.hutool.json.JSONUtil.isTypeJSON(orderStr)) {
                orderStr = URLUtil.decode(orderStr);
            }
            this.orderStr = orderStr;
            this.orderList = handleOrderJsonStr(orderStr);
        }
    }

    public Wrapper handelWrapperOrder(QueryWrapper wrapper) {
        if (ObjectUtil.isNull(wrapper)) {
            wrapper = Wrappers.emptyWrapper();
        }
        if (CollUtil.isNotEmpty(this.orderList)) {
            orderListHandle(wrapper, this.orderList);
        }
        return wrapper;
    }

    private List<Order> handleOrderJsonStr(String orderStr) {
        List<Order> orderList = new LinkedList<>();
        Map<String, Object> stringObjectMap = JSONUtil.readMap(orderStr);
        Assert.notNull(stringObjectMap, "order 字段错误！");
        stringObjectMap.forEach((key, value) -> {
            if (!StrUtil.isEmptyIfStr(value)) {
                Assert.isTrue(ORDER_OPTION_MAP.containsKey(value.toString()), "order 字段错误！【ASC,DESC】");
                orderList.add(new Order(StringUtils.camelToUnderline(key), value.toString()));
            }
        });
        return orderList;
    }


    private void orderListHandle(QueryWrapper<?> queryWrapper, List<Order> orderList) {
        orderList.forEach(order -> {
            OrderOption orderOption = ORDER_OPTION_MAP.get(order.getOrder());
            switch (orderOption) {
                case ASC:
                    queryWrapper.orderByAsc(order.getFiled());
                    break;
                case DESC:
                    queryWrapper.orderByDesc(order.getFiled());
                    break;
            }
        });
    }
}
