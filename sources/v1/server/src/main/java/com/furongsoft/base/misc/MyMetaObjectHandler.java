package com.furongsoft.base.misc;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * @author Alex
 */
@Component
public class MyMetaObjectHandler extends MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createUser = getFieldValByName("createUser", metaObject);
        Long userId = 0L;
        if (createUser == null) {
            try {
                if (!Objects.isNull(SecurityUtils.getCurrentUser())) {
                    userId = SecurityUtils.getCurrentUser().getId();
                }
            } catch (Exception e) {
                Tracker.error(e);
            }

            setFieldValByName("createUser", userId, metaObject);
        }

        Object lastModifyUser = getFieldValByName("lastModifiedUser", metaObject);
        if (lastModifyUser == null) {
            setFieldValByName("lastModifiedUser", userId, metaObject);
        }

        Object createTime = getFieldValByName("createTime", metaObject);
        if (createTime == null) {
            setFieldValByName("createTime", new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = 0L;
        try {
            if (!Objects.isNull(SecurityUtils.getCurrentUser())) {
                userId = SecurityUtils.getCurrentUser().getId();
            }
        } catch (Exception e) {
            Tracker.error(e);
        }

        setFieldValByName("lastModifiedUser", userId, metaObject);
    }
}
