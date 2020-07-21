package com.furongsoft.base.rbac.services;

import com.furongsoft.base.rbac.entities.UserRole;
import com.furongsoft.base.rbac.mappers.UserRoleDao;
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
public class UserRoleService extends BaseService<UserRoleDao, UserRole> {
    protected UserRoleService(UserRoleDao baseMapper) {
        super(baseMapper);
    }
}
