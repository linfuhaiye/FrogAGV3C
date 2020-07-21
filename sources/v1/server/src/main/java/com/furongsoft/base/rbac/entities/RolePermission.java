package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 角色权限表
 *
 * @author chenfuqian
 */
@Entity
@Table(name = "t_sys_role_permission")
@TableName("t_sys_role_permission")
public class RolePermission implements Serializable {
    /**
     * 权限ID
     */
    @Id
    private Long permissionId;

    /**
     * 角色ID
     */
    @Id
    private Long roleId;

    public RolePermission(Long permissionId, Long roleId) {
        this.permissionId = permissionId;
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
