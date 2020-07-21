package com.furongsoft.agv.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.furongsoft.base.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 退货记录信息 TODO 未用上
 *
 * @author linyehai
 */
@Entity
@TableName("t_agv_sales_return_record")
@Getter
@Setter
public class SalesReturnRecord extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date operationTime;

    /**
     * 班组唯一标识（uuid）
     */
    private String teamId;

    /**
     * 班组名称
     */
    private String teamName;

    /**
     * 源数据
     */
    private String source;

    /**
     * 结果数据
     */
    private String results;

    /**
     * 操作类型[1：新增；2：修改；3：删除]  TODO 后续改用枚举
     */
    private int type;

    /**
     * 区域ID(产线ID)
     */
    private Long areaId;

    /**
     * 是否启用
     */
    private Integer enabled;
}
