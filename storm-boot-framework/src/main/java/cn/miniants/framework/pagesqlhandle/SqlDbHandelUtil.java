package cn.miniants.framework.pagesqlhandle;

import cn.miniants.framework.pagesqlhandle.filter.FilterHandlerUtil;
import cn.miniants.framework.pagesqlhandle.order.OrderHandlerUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * @author guoqi
 * @date 2019/4/18
 */
@Setter
@AllArgsConstructor
public class SqlDbHandelUtil {
    private String filter;
    private String order;

    public QueryWrapper getWrapper(){
        FilterHandlerUtil filterHandlerUtil = new FilterHandlerUtil(this.filter);
        OrderHandlerUtil orderHandlerUtil = new OrderHandlerUtil(this.order);
        return (QueryWrapper) orderHandlerUtil.handelWrapperOrder(filterHandlerUtil.getFilterWrapper());
    }
}
