package com.furongsoft.agv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 验收记录前端对象
 *
 * @author linyehai
 */
@Getter
@Setter
public class AcceptanceRecordModel implements Serializable {

    /**
     * 叫料ID
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
     * 操作类型[1：新增；2：修改；3：删除]
     */
    private Integer type;

    /**
     * 区域ID(产线ID)
     */
    private Long areaId;

    /**
     * 是否启用
     */
    private Integer enabled;

}
