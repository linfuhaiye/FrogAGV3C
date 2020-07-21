package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户
 *
 * @author Alex
 */
@Entity
@Table(name = "t_sys_user")
@TableName("t_sys_user")
@Getter
@Setter
public class User extends BaseEntity implements Serializable {
    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 登录账户
     */
    @NotBlank(message = "user.username.notNull")
    @Length(max = 32, message = "user.username.maxLength")
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "user.username.needLater")
    private String username;

    /**
     * 密码
     */
    @Length(max = 32, message = "user.password.maxLength")
    private String password;

    private String openid;

    /**
     * 用户编码
     */
//    @NotBlank(message = "user.code.notNull")
    @Length(max = 128, message = "user.code.maxLength")
    private String code;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 姓名
     */
//    @Length(max = 16, message = "user.name.maxLength")
    @NotBlank(message = "user.name.notNull")
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 头衔
     */
    private Long title;

    /**
     * 头衔
     */
    private Long title2;

    /**
     * 头衔
     */
    private Long title3;

    /**
     * 工作单位
     */
    @Length(max = 64, message = "user.company.maxLength")
    private String company;

    /**
     * 工作单位
     */
    @Length(max = 64, message = "user.company.maxLength")
    private String company2;

    /**
     * 工作单位
     */
    @Length(max = 64, message = "user.company.maxLength")
    private String company3;

    /**
     * 工作地址
     */
    @Length(max = 64, message = "user.businessAddress.maxLength")
    private String businessAddress;

    /**
     * 工作地址
     */
    @Length(max = 64, message = "user.businessAddress.maxLength")
    private String businessAddress2;

    /**
     * 住宅地址
     */
    @Length(max = 64, message = "user.address.maxLength")
    private String address;

    /**
     * 住宅地址
     */
    @Length(max = 64, message = "user.address.maxLength")
    private String address2;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 电子邮件
     */
//    @NotBlank(message = "user.email.notNull")
    @Email(message = "user.email.reg")
    @Length(max = 255, message = "user.email.maxLength")
    private String email;

    /**
     * 电子邮件
     */
    @Email(message = "user.email.reg")
    @Length(max = 255, message = "user.email.maxLength")
    private String email2;

    /**
     * 电子邮件
     */
    @Email(message = "user.email.reg")
    @Length(max = 255, message = "user.email.maxLength")
    private String email3;

    /**
     * 网站
     */
    @Length(max = 255, message = "user.website.maxLength")
    private String webSite;

    /**
     * 网站
     */
    @Length(max = 255, message = "user.website.maxLength ")
    private String webSite2;

    /**
     * 网站
     */
    @Length(max = 255, message = "user.website.maxLength ")
    private String webSite3;

    /**
     * 移动电话
     */
    @NotBlank(message = "user.mobile.notNull")
    @Length(max = 32, message = "user.mobile.maxLength")
    @Pattern(regexp = "[0-9]{11}", message = "user.mobile.pattern")
    private String mobile;

    /**
     * 移动电话
     */
    @Length(max = 32, message = "user.mobile.maxLength")
    private String mobile2;

    /**
     * 固定电话
     */
    @Length(max = 32, message = "user.telephone.maxLength")
    private String telephone;

    /**
     * 固定电话
     */
    @Length(max = 32, message = "user.telephone.maxLength")
    private String telephone2;

    /**
     * 社交软件账号
     */
    @Length(max = 255, message = "user.snsAccount.maxLength")
    private String snsAccount;

    /**
     * 社交软件账号
     */
    @Length(max = 255, message = "user.snsAccount.maxLength")
    private String snsAccount2;

    /**
     * 社交软件账号
     */
    @Length(max = 255, message = "user.snsAccount.maxLength")
    private String snsAccount3;

    /**
     * 国家
     */
    @Length(max = 32, message = "user.country.maxLength")
    private String country;

    /**
     * 省份/州
     */
    private String province;

    /**
     * 市/县
     */
    private String city;

    /**
     * 区/镇
     */
    private String town;

    /**
     * 街道
     */
    @Length(max = 64, message = "user.street.maxLength")
    private String street;

    /**
     * 邮编
     */
    @Length(max = 32, message = "user.zip.maxLength")
    private String zip;

    /**
     * 证件
     */
    @Length(max = 32, message = "user.identification.maxLength")
    private String identification;

    /**
     * 证件
     */
    @Length(max = 32, message = "user.identification.maxLength")
    private String identification2;

    /**
     * 证件
     */
    @Length(max = 32, message = "user.identification.maxLength")
    private String identification3;

    /**
     * 头像
     */
    private String iconUrl;

    /**
     * 照片
     */
    private String pictureUrl;

    /**
     * 备注
     */
    @Length(max = 255, message = "user.remark.maxLength")
    private String remark;

    /**
     * 部门
     */
//    @NotNull(message = "user.department.notNull")
    private Long department;

    /**
     * 部门
     */
    private Long department2;

    /**
     * 部门
     */
    private Long department3;

    /**
     * 欢迎页
     */
    private String home;

    private String layout;

    @Transient
    @TableField(exist = false)
    private List<Long> areas;

    /**
     * 账户状态
     */
    private boolean isEnable;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expireDate;

    /**
     * 车牌号
     */
    private String carNumber;

    @Transient
    @TableField(exist = false)
    private String rePassword;

    /**
     * 原始密码
     */
    private String originalPsw;

    /**
     * 推荐人ID
     */
    private Long recommender;

    /**
     * 余额
     */
    private int balance;

    /**
     * 支付宝账号
     */
    private String alipayNo;

    /**
     * 是否关注
     */
    private Boolean subscribe;

    /**
     * 是否vip
     */
    private Boolean vip;

    /**
     * 微信账号
     */
    private String wechatNo;

    /**
     * unionId
     */
    private String unionId;

    /**
     * 账户类型
     * <p>
     * 1.公众号 2.小程序
     */
    private int type;

    /**
     * 省份ID
     */
    private Long provinceId;

    /**
     * 城市ID
     */
    private Long cityId;

    /**
     * 区Id
     */
    private Long townId;

    /**
     * 会员等级
     */
    private Integer vipLevel;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 门店地址
     */
    private String shopAddress;

    /**
     * 是否已享优惠
     */
    private Boolean discount;
}
