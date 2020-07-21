package com.furongsoft.base.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类型
 *
 * @author Alex
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity implements Serializable {
    /**
     * 创建用户
     */
    @JsonIgnore
    @CreatedBy
    @Column(nullable = false, columnDefinition = "BIGINT(20) COMMENT '创建用户'")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonIgnore
    @CreatedDate
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    @Temporal(TemporalType.TIMESTAMP)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 最后修改用户
     */
    @JsonIgnore
    @LastModifiedBy
    @Column(nullable = false, columnDefinition = "BIGINT(20) COMMENT '最后修改用户'")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long lastModifiedUser;

    /**
     * 最后修改时间
     */
    @JsonIgnore
    @LastModifiedDate
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间'")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedTime;
}
