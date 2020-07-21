package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户角色表
 *
 * @author chenfuqian
 */
@Entity
@Table(name = "t_sys_user_role")
@TableName("t_sys_user_role")
public class UserRole implements Serializable {
    /**
     * 用户ID
     */
    @Id
    private Long userId;

    /**
     * 角色ID
     */
    @Id
    private Long roleId;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
