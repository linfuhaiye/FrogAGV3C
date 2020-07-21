package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.rbac.entities.PermissionResource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 权限资源操作对象
 *
 * @author chenfuqian
 */
@Mapper
@Component
public interface PermissionResourceDao extends BaseMapper<PermissionResource> {
}
