package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 权限资源表
 *
 * @author chenfuqian
 */
@Entity
@Table(name = "t_sys_permission_resource")
@TableName("t_sys_permission_resource")
public class PermissionResource implements Serializable {
    /**
     * 权限ID
     */
    @Id
    private Long permissionId;

    /**
     * 资源ID
     */
    @Id
    private Long resourceId;

    public PermissionResource(Long permissionId, Long resourceId) {
        this.permissionId = permissionId;
        this.resourceId = resourceId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}
