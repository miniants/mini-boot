package cn.miniants.framework.bean;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * @author guoqianyou
 * @date 2022/5/25 18:43
 */
@Setter
@Getter
public class BaseLogicEntity extends BaseEntity {
    /**
     * 删除 0、否  时间戳为删除
     */
//    @JsonIgnore
    @TableLogic(value = "false", delval = "now()")
    private Long deleted;

}
