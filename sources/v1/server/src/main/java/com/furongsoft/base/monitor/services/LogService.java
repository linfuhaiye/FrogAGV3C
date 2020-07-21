package com.furongsoft.base.monitor.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.furongsoft.base.misc.StringUtils;
import com.furongsoft.base.monitor.entities.LogInfo;
import com.furongsoft.base.monitor.mappers.LogInfoDao;
import com.furongsoft.base.services.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日志管理服务层
 *
 * @author liujianning
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LogService extends BaseService<LogInfoDao, LogInfo> {
    protected LogService(LogInfoDao baseMapper) {
        super(baseMapper);
    }

    /**
     * 获取操作日志列表
     *
     * @param name      用户名
     * @param type      类型
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @param page      分页参数
     * @return 操作日志列表
     */
    public Page<LogInfo> getList(String name, String type, String startTime, String endTime, Page<LogInfo> page) {
        EntityWrapper<LogInfo> ew = new EntityWrapper<>();
        if (!StringUtils.isNullOrEmpty(name)) {
            ew.like("username", name);
        }
        if (!StringUtils.isNullOrEmpty(type)) {
            ew.eq("type", type);
        }
        if (!StringUtils.isNullOrEmpty(startTime) && !StringUtils.isNullOrEmpty(endTime)) {
            ew.between("create_time", startTime, endTime);
        }
        ew.orderBy("create_time DESC");
        return super.selectPage(page, ew);
    }

    /**
     * 获取操作日志列表
     *
     * @param name      用户名
     * @param type      类型
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return 日志列表
     */
    public List<LogInfo> getLogReport(String name, String type, String startTime, String endTime) {
        EntityWrapper<LogInfo> ew = new EntityWrapper<>();
        if (!StringUtils.isNullOrEmpty(name)) {
            ew.like("username", name);
        }
        if (!StringUtils.isNullOrEmpty(type)) {
            ew.eq("type", type);
        }
        if (!StringUtils.isNullOrEmpty(startTime) && !StringUtils.isNullOrEmpty(endTime)) {
            ew.between("create_time", startTime, endTime);
        }
        ew.orderBy("create_time DESC");
        return super.selectList(ew);
    }
}