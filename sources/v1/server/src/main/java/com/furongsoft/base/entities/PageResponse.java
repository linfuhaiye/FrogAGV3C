package com.furongsoft.base.entities;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;

/**
 * 分页列表请求返回数据
 *
 * @author Alex
 */
@Getter
@Setter
public class PageResponse<T> extends PagedModel<T> {
    /**
     * HTTP状态码
     */
    private int code;

    /**
     * 错误码
     */
    private int errno;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("_embedded")
    private Object data;

    /**
     * 新令牌
     */
    private String newToken;

    public PageResponse(HttpStatus status) {
        this(status.value(), null, null);
    }

    public PageResponse(HttpStatus status, String message, Object data) {
        this(status.value(), message, data);
    }

    public PageResponse(HttpStatus status, String message, Object data, String newToken) {
        this(status.value(), message, data, newToken);
    }

    public PageResponse(int code, String message, Object data) {
        this.code = code;
        this.errno = (HttpStatus.OK.value() == code) ? 0 : -1;
        this.message = message;
        this.data = data;
    }

    public PageResponse(int code, String message, Object data, String newToken) {
        this.code = code;
        this.errno = (HttpStatus.OK.value() == code) ? 0 : -1;
        this.message = message;
        this.data = data;
        this.newToken = newToken;
    }

    public PageResponse(HttpStatus status, Page<T> page) {
        super(page.getRecords(), new PageMetadata(page.getSize(), page.getCurrent(), page.getTotal(), page.getPages()));
        this.code = status.value();
        this.errno = (HttpStatus.OK.value() == code) ? 0 : -1;
    }

    public PageResponse(PagedModel<T> resources) {
        super(resources.getContent(), resources.getMetadata(), resources.getLinks());
        this.code = HttpStatus.OK.value();
        this.errno = 0;
    }
}
