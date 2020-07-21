package com.furongsoft.base.rbac.models;

import com.furongsoft.base.rbac.entities.Resource;
import com.furongsoft.base.rbac.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户信息
 *
 * @author chenfuqian
 */
@Getter
@Setter
public class UserInfo implements Serializable {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户编号
     */
    private String code;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 登录账号
     */
    private String userName;

    /**
     * 部门名称
     */
    private String deparmentName;

    /**
     * 部门编码
     */
    private String deparmentCode;

    /**
     * 用户头像
     */
    private String iconUrl;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 用户资源
     */
    private List<Resource> resources;

    /**
     * 用户配置信息
     */
    private UserConfigure userConfigure;

    /**
     * 欢迎页
     */
    private String home;

    /**
     * 布局
     */
    private String layout;

    /**
     * 角色列表
     */
    private List<Role> roles;

    /**
     * 是否关注
     */
    private Boolean subscribe;

    /**
     * 是否vip
     */
    private Boolean isVip;

    /**
     * 到期日期
     */
    private Date expireDate;

    /**
     * 是否到期
     */
    private Boolean isExpire;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 身份证
     */
    private String identityNo;

    private String realName;
}
