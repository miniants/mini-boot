package cn.miniants.framework.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author guoqianyou
 * @date 2022/10/12 19:00
 */

@Getter
@AllArgsConstructor
public enum TokenUserType {

    SYS_USER(0, "SYS_USER"),
    SIMPLE_USER(1, "SIMPLE_USER");
    private final Integer value;
    private final String desc;

    public static TokenUserType fromValue(Integer value) {
        TokenUserType[] values = TokenUserType.values();
        for (TokenUserType it : values) {
            if (it.getValue().equals(value)) {
                return it;
            }
        }
        return null;
    }

}
