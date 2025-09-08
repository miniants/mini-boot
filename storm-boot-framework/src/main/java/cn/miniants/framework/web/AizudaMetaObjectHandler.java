
package cn.miniants.framework.web;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Optional;

public class AizudaMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Optional<UserSession> sessionOptional = UserSession.getLoginInfoOpt();
        this.fillHasGetter(metaObject, "createId", sessionOptional.map(UserSession::getId).orElse(-1L));
        this.fillHasGetter(metaObject, "createBy", sessionOptional.map(UserSession::getUsername).orElse("system"));
        this.fillHasGetter(metaObject, "createTime", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Optional<UserSession> sessionOptional = UserSession.getLoginInfoOpt();
        this.fillHasGetter(metaObject, "updateBy", sessionOptional.map(UserSession::getUsername).orElse("system"));
        this.fillHasGetter(metaObject, "updateTime", LocalDateTime.now());
    }

    protected void fillHasGetter(MetaObject metaObject, String fieldName, Object fieldVal) {
        if (metaObject.hasGetter(fieldName)) {
            this.fillStrategy(metaObject, fieldName, fieldVal);
        }
    }
}
