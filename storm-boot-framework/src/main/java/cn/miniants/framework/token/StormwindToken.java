package cn.miniants.framework.token;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.kisso.security.token.SSOToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;

/**
 * @author guoqianyou
 * @date 2022/10/12 18:58
 */
@Setter
@Getter
public class StormwindToken extends SSOToken {
    private TokenUserType userType;

    public StormwindToken() {
        super();
    }


    public StormwindToken(JwtBuilder jwtBuilder) {
        super(jwtBuilder);
    }

    @Override
    public String getToken() {
        Assert.notNull(userType, "没有指定token 的userType");
        if (null == this.getJwtBuilder()) {
            this.setJwtBuilder(Jwts.builder());
        }
        getJwtBuilder().claim(StormwindTokenConstants.TOKEN_USER_TYPE, this.userType.getValue());
        return super.getToken();
    }

    public static StormwindToken parser(SSOToken ssoToken) {
        if (ObjectUtil.isNull(ssoToken)) {
            return null;
        }
        StormwindToken stormwindToken = new StormwindToken();
        BeanUtil.copyProperties(ssoToken, stormwindToken, "claims");
        Claims claims = ssoToken.getClaims();
        stormwindToken.setClaims(claims);
        Integer userType = claims.get(StormwindTokenConstants.TOKEN_USER_TYPE, Integer.class);
        if (ObjectUtil.isNotNull(userType)) {
            TokenUserType tokenUserType = TokenUserType.fromValue(userType);
            stormwindToken.setUserType(tokenUserType);
        }
        return stormwindToken;
    }

}
