package com.furongsoft.agv.schedulers.services;

import com.furongsoft.agv.schedulers.entities.Task;
import com.furongsoft.agv.schedulers.mappers.TaskDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AGV任务服务
 *
 * @author Alex
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskService extends BaseService<TaskDao, Task> {
    @Autowired
    protected TaskService(TaskDao baseMapper) {
        super(baseMapper);
    }
}
