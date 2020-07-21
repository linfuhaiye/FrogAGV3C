package com.furongsoft.base.entities;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页列表请求
 *
 * @author Alex
 */
@Getter
@Setter
public class PageRequest {
    /**
     * 页码
     */
    private int pageNum;

    /**
     * 页内数据长度
     */
    private int pageSize;

    private List<String> ascs;
    private List<String> descs;

    /**
     * 获取分页对象
     *
     * @return 分页对象
     */
    public <T> Page<T> getPage() {
        if ((ascs != null) && (ascs.size() > 0)) {
            return new Page<T>(pageNum, pageSize, String.join(",", ascs));
        }
        if ((descs != null) && (descs.size() > 0)) {
            return new Page<T>(pageNum, pageSize, String.join(",", descs), false);
        }
        return new Page<T>(pageNum, pageSize);
    }
}
