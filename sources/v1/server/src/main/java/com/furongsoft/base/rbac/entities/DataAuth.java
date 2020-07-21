package com.furongsoft.base.rbac.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_sys_data_auth")
@TableName("t_sys_data_auth")
@Getter
@Setter
@AllArgsConstructor
public class DataAuth implements Serializable {

    @Id
    private Long userId;

    @Id
    private Long targetUserId;
}
