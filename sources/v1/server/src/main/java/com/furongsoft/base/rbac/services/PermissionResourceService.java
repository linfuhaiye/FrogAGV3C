package com.furongsoft.base.rbac.services;

import com.furongsoft.base.rbac.entities.PermissionResource;
import com.furongsoft.base.rbac.mappers.PermissionResourceDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限资源服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionResourceService extends BaseService<PermissionResourceDao, PermissionResource> {

    protected PermissionResourceService(PermissionResourceDao baseMapper) {
        super(baseMapper);
    }
}
