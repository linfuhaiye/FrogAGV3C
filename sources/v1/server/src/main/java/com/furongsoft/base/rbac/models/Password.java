package com.furongsoft.base.rbac.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 密码
 *
 * @author liujianning
 */
@Getter
@Setter
public class Password {

    /**
     * 旧密码
     */
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    private String newPassword;

    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    private String reNewPassword;
}
