/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package cn.miniants.framework.web;

import cn.hutool.core.lang.Assert;
import cn.miniants.framework.token.StormwindToken;
import cn.miniants.toolkit.JSONUtil;
import cn.miniants.toolkit.MiniStrUtil;
import cn.miniants.toolkit.ThreadLocalUtils;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.security.token.SSOToken;
import cn.miniants.framework.spring.SpringHelper;
import cn.miniants.framework.token.TokenUserType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

import static cn.miniants.framework.constant.StormwindConstant.AuthConstants.JWT_USER_SESSION;

/**
 * 爱组搭 http://aizuda.com
 * ----------------------------------------
 * 用户会话信息
 *
 * @author 青苗
 * @since 2021-10-28
 */
@Getter
@Setter
@Builder
public class UserSession {
    private JsonNode originalData;

    private Long id;
    private String username;
    private String authorities;
    private String scope;
    private String clientId;
    private String userPayload;
    private TokenUserType userType;

    private JsonNode principals; // 用于存储用户实名认证信息


    public String pathValue(String fieldPath) {
        return MiniStrUtil.getOrEmpty(JSONUtil.pathReadNestedValue(originalData, fieldPath));
    }
    public static Optional<UserSession> getLoginInfoOpt() {
        return Optional.ofNullable(getCurSession(SpringHelper.getCurrentRequest(), true));
    }

    public static UserSession getCurSession() {
        return getCurSession(SpringHelper.getCurrentRequest(), false);
    }

    public static <T> T getUserPayload(Class<T> clazz) {
        return JSONUtil.parse(getCurSession().getUserPayload(), clazz);
    }

    private static UserSession getCurSession(HttpServletRequest request, boolean allowNull) {
        UserSession userSession = ThreadLocalUtils.get(JWT_USER_SESSION);

        if (null != userSession) {
            return userSession;
        }

        // TODO 改造后要处理一下这里，暂时兼容一下
        StormwindToken ssoToken = getSSOToken(request, allowNull);
        userSession =  Optional.ofNullable(ssoToken)
                .map(ssoToken1 -> UserSession.builder()
                        .id(Long.valueOf(ssoToken1.getId()))
                        .username(ssoToken1.getIssuer())
                        .userType(ssoToken1.getUserType())
                        .build())
                .orElse(null);
        ThreadLocalUtils.put(JWT_USER_SESSION, userSession);

        return userSession;
    }

    public static StormwindToken getSSOToken(HttpServletRequest request, boolean allowNull) {
        StormwindToken ssoToken = getSSOToken(request);
        Assert.isFalse(null == ssoToken && !allowNull, "未登录");
        return ssoToken;
    }

    /**
     * 获取当前登录账户 SSOToken 登录票据
     *
     * @param request 当前请求
     * @return SSOToken 登录票据
     */
    private static StormwindToken getSSOToken(HttpServletRequest request) {
        SSOToken ssoToken = null;
        if (null != request) {
            ssoToken = SSOHelper.attrToken(request);
            if (null == ssoToken) {
                ssoToken = SSOHelper.getSSOToken(request);
            }
        }
        return StormwindToken.parser(ssoToken);
    }

    /**
     * 判断是否为管理员
     *
     * @param id 用户ID
     * @return
     */
    public static boolean isAdmin(Long id) {
        return Objects.equals(0L, id);
    }
}
