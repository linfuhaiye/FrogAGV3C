package com.furongsoft.base.monitor.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.monitor.entities.LogInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 日志管理数据接口层
 *
 * @author liujianning
 */
@Mapper
@Component
public interface LogInfoDao extends BaseMapper<LogInfo> {
}