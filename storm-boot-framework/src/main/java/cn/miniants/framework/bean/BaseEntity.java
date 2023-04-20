/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package cn.miniants.framework.bean;

import cn.miniants.framework.ApiConstants;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 爱组搭 http://aizuda.com
 * ----------------------------------------
 * 业务框架基础实体
 *
 * @author 青苗
 * @since 2021-10-28
 */
@Setter
@Getter
public class BaseEntity extends SuperEntity {

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    protected String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = ApiConstants.DATE_MM)
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.UPDATE)
    protected String updateBy;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = ApiConstants.DATE_MM)
    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime updateTime;


}
