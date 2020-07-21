package com.furongsoft.agv.schedulers.geekplus.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 执行搬运响应消息
 *
 * @author Alex
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovingResponseMsg {
    /**
     * 当前回调应答消息的唯一标识
     */
    private String requestId;
    /**
     * 回调应答代码，成功：0，失败：非0
     */
    private String code;
    /**
     * 返回消息说明，默认填写“Success”
     */
    private String msg;
    /**
     * 与回调的消息类型一一对应
     */
    private String msgType;
    /**
     * 返回失败和成功
     */
    private ResponseData[] data;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseData {
        /**
         * 搬运系统任务id
         */
        private String workflowWorkId;
    }
}
