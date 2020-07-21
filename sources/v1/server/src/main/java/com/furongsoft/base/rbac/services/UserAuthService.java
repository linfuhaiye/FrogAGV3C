package com.furongsoft.base.rbac.services;

import com.furongsoft.base.rbac.entities.DataAuth;
import com.furongsoft.base.rbac.mappers.UserAuthDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户角色服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserAuthService extends BaseService<UserAuthDao, DataAuth> {
    protected UserAuthService(UserAuthDao userAuthDao) {
        super(userAuthDao);
    }
}
