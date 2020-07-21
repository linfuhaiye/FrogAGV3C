package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.rbac.entities.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 角色权限操作对象
 *
 * @author chenfuqian
 */
@Mapper
@Component
public interface RolePermissionDao extends BaseMapper<RolePermission> {
}
