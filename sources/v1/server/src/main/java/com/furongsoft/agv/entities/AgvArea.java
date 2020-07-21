package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 区域信息
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_area")
@Getter
@Setter
public class AgvArea extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 父级ID
     */
    private long parentId;

    /**
     * 类型[1：生产区；2：灌装区；3：包装区；4：消毒间；5：拆包间；6：包材仓；7：生产线] TODO 后续改用枚举
     */
    private int type;

    /**
     * 名称
     */
    private String name;

    /**
     * 编号
     */
    private String code;

    /**
     * 是否启用
     */
    private Integer enabled;
}
