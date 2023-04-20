package cn.miniants.framework.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.URLDecoder;
import cn.miniants.framework.pagesqlhandle.SqlDbHandelUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.miniants.framework.api.ApiController;
import cn.miniants.framework.api.PageParam;
import cn.miniants.framework.bean.SuperEntity;
import cn.miniants.framework.validation.Create;
import cn.miniants.framework.validation.Update;
import cn.miniants.framework.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;

/**
 * @author guoqianyou
 * @date 2022/5/26 1:15
 */
@Slf4j
public class BaseController<BS extends BaseServiceImpl<?, M>, M extends SuperEntity>  extends ApiController {
    @Autowired
    protected BS baseService;

    @PostMapping
    public boolean add(@Validated(Create.class) @RequestBody M entity) {
        return baseService.save(entity);
    }

    @GetMapping("/{id}")
    public M get(@PathVariable Long id) {
        return baseService.getById(id);
    }

    @PutMapping
    public boolean update(@Validated(Update.class) @RequestBody M entity) {
        Assert.notNull(entity.getId(), "更新 Id 不能为空！");
        return baseService.updateById(entity);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return baseService.removeById(id);
    }

    @GetMapping("/page")
    public Page<?> page(PageParam<?> myPageParam) {
        String filterStr = URLDecoder.decode(myPageParam.getFilter(), Charset.defaultCharset());
        SqlDbHandelUtil sqlDbHandelUtil = new SqlDbHandelUtil(filterStr, myPageParam.getOrder());
        return baseService.page(myPageParam.page(), sqlDbHandelUtil.getWrapper());
    }


}
