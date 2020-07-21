package com.furongsoft.agv.frog.entities;

import com.furongsoft.base.misc.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 基本的请求对象
 *
 * @author linyehai
 */
@Getter
@Setter
@AllArgsConstructor
public class BaseRequestMsg {
    /**
     * API接口名称_true
     */
    private String method;

    /**
     * 分配给应用的AppKey_true
     */
    private String app_key;

    /**
     * API服务端允许客户端请求最大时间误差为10分钟_true
     */
    private String timestamp;

    /**
     * 默认为json格式，可选值：xml，json。_false
     */
    private String format;

    /**
     * API协议版本，可选值：1.0_true
     */
    private String version;

    /**
     * 是否采用精简JSON返回格式，仅当format=json时有效，默认值为：false_false
     */
    private Boolean simplify;

    /**
     * 帐套 design_true
     */
    private String account;

    /**
     * 业务请求参数 json字符串
     */
    private String filter;

    /**
     * 转化为Get请求参数模式
     *
     * @return get请求参数
     */
    public String toParameter() {
        StringBuffer parameters = new StringBuffer();
        parameters.append("method=");
        parameters.append(method);
        parameters.append("&app_key=");
        parameters.append(app_key);
        parameters.append("&timestamp=");
        parameters.append(URLEncoder.encode(timestamp, StandardCharsets.UTF_8));
        parameters.append("&version=");
        parameters.append(version);
        if (!StringUtils.isNullOrEmpty(format)) {
            parameters.append("&format=");
            parameters.append(format);
        }
        if (ObjectUtils.isNotEmpty(simplify)) {
            parameters.append("&simplify=");
            parameters.append(simplify);
        }
        if (!StringUtils.isNullOrEmpty(account)) {
            parameters.append("&account=");
            parameters.append(account);
        }
        if (!StringUtils.isNullOrEmpty(filter)) {
            parameters.append("&filter=");
            parameters.append(filter);
        }
        return parameters.toString();
    }
}
