package com.furongsoft.agv.geek.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * request header
 *
 * @author linyehai
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class RequestHeader {

    private String requestId;

    private String clientCode;

    private String warehouseCode;

    private String channelId;

    private String userId;

    private String userKey;

    private String language;

    private String version;
}
