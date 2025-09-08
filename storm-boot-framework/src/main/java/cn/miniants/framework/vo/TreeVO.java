
package cn.miniants.framework.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreeVO implements Serializable {
    private Long id;
    private String label;
    private List<TreeVO> children;

}
