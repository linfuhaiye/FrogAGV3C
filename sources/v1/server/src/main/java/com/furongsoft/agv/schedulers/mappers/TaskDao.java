package com.furongsoft.agv.schedulers.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.schedulers.entities.Task;
import org.apache.ibatis.annotations.Mapper;

/**
 * AGV任务数据库操作
 *
 * @author Alex
 */
@Mapper
public interface TaskDao extends BaseMapper<Task> {
}
