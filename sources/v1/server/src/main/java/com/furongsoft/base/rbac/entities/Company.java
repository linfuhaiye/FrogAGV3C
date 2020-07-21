package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 公司信息
 *
 * @author linyehai
 */
@Entity
@Table(name = "t_sys_company")
@TableName("t_sys_company")
public class Company extends BaseEntity implements Serializable {
    /**
     * 索引
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 公司名称
     */
    @NotBlank(message = "company.name.notNull")
    @Length(max = 128, message = "company.name.maxLength")
    private String name;

    /**
     * 联系电话
     */
    @Pattern(regexp = "^(1[34587]\\d{9}|(?:(?:0\\d{2,3}[- ]?[1-9]\\d{6,7}))|(?:[48]00[- ]?[1-9]\\d{6}))$", message = "company.mobile.reg")
    private String mobile;

    /**
     * 公司地址
     */
    @Length(max = 512, message = "company.address.maxLength")
    private String address;

    /**
     * 邮政编码
     */
    @Length(max = 6, message = "company.zip.maxLength")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "company.zip.reg")
    private String zip;

    /**
     * 官方网站
     */
    @Length(max = 128, message = "company.website.maxLength")
    private String webSite;

    /**
     * 官方邮箱
     */
    @Email(message = "company.email.reg")
    @Length(max = 64, message = "company.email.maxLength")
    private String email;

    /**
     * 传真号码
     */
    @Pattern(regexp = "^(\\d{3,4}-)?\\d{7,8}$", message = "company.fax.reg")
    @Length(max = 64, message = "company.fax.maxLength")
    private String fax;

    /**
     * 公司LOGO
     */
    private String logo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}