package cn.miniants.toolkit;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

public class PageUtil {
    public static <T> Page<T> createPage(List<T> records, long total, long size, long current) {
        if (size == 0) size = 10;
        if (total == 0 && !CollUtil.isEmpty(records)) total = records.size();
        if (current == 0) current = 1;
        return new Page<>(records, size, total, current);
    }

    @Setter
    @Getter
    public static class Page<T> {
        protected List<T> records = Collections.emptyList();
        protected long total = 0;
        /**
         * 每页显示条数，默认 10
         */
        protected long size = 10;

        /**
         * 当前页
         */
        protected long current = 1;

        public Page(List<T> records, long size, long total, long current) {
            this.records = records;
            this.size = size;
            this.total = total;
            this.current = current;
        }
    }
}

