package cn.miniants.framework.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@Getter@Setter
public class AuthException extends OAuth2Exception {
    public AuthException(String msg, int code, String redirect) {
        super(msg);
        this.addAdditionalInformation("code", String.valueOf(code));
        this.addAdditionalInformation("msg", msg);
        this.addAdditionalInformation("redirect", redirect);
    }

    public String getOAuth2ErrorCode() {
        return "access_denied";
    }


    public int getHttpErrorCode() {
        return 401;
    }

}
