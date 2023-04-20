package cn.miniants.toolkit;

import cn.hutool.core.util.NumberUtil;

public class NumUtil {
    public static Double round(Double d) {
        return round(d, 2);
    }

    public static Double round(Double d, int len) {
        double f2 = null == d ? 0.0f : d;
        return NumberUtil.round(f2, len).doubleValue();
    }
}
