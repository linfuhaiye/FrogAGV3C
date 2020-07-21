package com.furongsoft.base.monitor.controllers.api.v1;

import com.furongsoft.base.entities.PageRequest;
import com.furongsoft.base.entities.PageResponse;
import com.furongsoft.base.misc.ExcelUtils;
import com.furongsoft.base.monitor.entities.LogInfo;
import com.furongsoft.base.monitor.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 日志管理API
 *
 * @author liujianning
 */
@RestController
@RequestMapping("/api/v1/monitor")
public class LogController {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
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
    @GetMapping
    @RequestMapping("/logs")
    public PageResponse getLogs(PageRequest page, @RequestParam(required = false) String name,
                                @RequestParam(required = false) String type,
                                @RequestParam(required = false) String startTime,
                                @RequestParam(required = false) String endTime) {
        return new PageResponse<>(HttpStatus.OK, logService.getList(name, type, startTime, endTime, page.getPage()));
    }

    @GetMapping
    @RequestMapping("/logs/report")
    public void getLogReport(HttpServletResponse response, @RequestParam(required = false) String name,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime) {
        ExcelUtils.exportExcel(logService.getLogReport(name, type, startTime, endTime), "操作日志记录", "日志信息", LogInfo.class, "log.xls", response);
    }
}