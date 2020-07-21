package com.furongsoft.base.rbac.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 汽车系统用户信息
 *
 * @author chenfuqian
 */
@Getter
@Setter
public class CarUser {

    @NotBlank(message = "user.name.notNull")
    private String name;

    /**
     * 电子邮件
     */
//    @NotBlank(message = "user.email.notNull")
    @Email(message = "user.email.reg")
    @Length(max = 255, message = "user.email.maxLength")
    private String email;

    /**
     * 移动电话
     */
    @NotBlank(message = "user.mobile.notNull")
    @Length(max = 32, message = "user.mobile.maxLength")
    @Pattern(regexp = "[0-9]{11}", message = "user.mobile.pattern")
    private String mobile;

    /**
     * 证件
     */
    @Length(max = 32, message = "user.identification.maxLength")
    private String identification;

    /**
     * 支付宝账号
     */
    private String alipayNo;

    /**
     * 微信账号
     */
    private String wechatNo;
}
