package cn.miniants.framework.pagesqlhandle.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author guoqianyou
 * @date 2018-06-25 17:28
 */

@Setter
@Getter
public class Filter {
    private FilterGroup filterGroup ;
    private List<FilterBean> filterBeanList = new LinkedList<>();
    private List<Filter> childFilterList = new LinkedList<>();

    public void addFilterBean(String filed, FilterOperational filterOperational,Object value){
        boolean addStatus = true;
        if (ObjectUtil.isNotNull(value)){
            addStatus = StrUtil.isNotEmpty(value.toString());
        }
      if (addStatus)
          this.filterBeanList.add(new FilterBean( StringUtils.camelToUnderline(filed),filterOperational.toString(),value));
    }
    public void addChildFilter(Filter filter){
        this.childFilterList.add(filter);
    }
    @Setter
    @Getter
    public static class FilterBean{
        private  String filed;
        private String op;
        private Object value;

        public FilterBean(String filed, String op, Object value) {
            this.filed = filed;
            this.op = op;
            this.value = value;
        }
    }

}
