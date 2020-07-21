package com.furongsoft.agv.controllers;

import com.furongsoft.agv.models.EmployeeInfo;
import com.furongsoft.agv.models.Team;
import com.furongsoft.agv.services.MaterialService;
import com.furongsoft.base.restful.entities.RestResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 外部对接接口层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/external")
public class ExternalDockingController {
    private final MaterialService materialService;

    public ExternalDockingController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * 获取员工信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/employee")
    public RestResponse getEmployeeInfo(@NonNull String userId) {
        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.setId(userId);
        employeeInfo.setName("施若琳");
        return new RestResponse(HttpStatus.OK, null, employeeInfo);
    }

    /**
     * 获取生产班组
     *
     * @return
     */
    @GetMapping("/teams")
    public RestResponse getTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(1, "B03班组", UUID.randomUUID().toString().replace("-", "")));
        teams.add(new Team(2, "B04班组", UUID.randomUUID().toString().replace("-", "")));
        teams.add(new Team(3, "B05班组", UUID.randomUUID().toString().replace("-", "")));
        teams.add(new Team(4, "B06班组", UUID.randomUUID().toString().replace("-", "")));
        teams.add(new Team(5, "B07班组", UUID.randomUUID().toString().replace("-", "")));
        teams.add(new Team(6, "B08班组", UUID.randomUUID().toString().replace("-", "")));
        teams.add(new Team(7, "B09班组", UUID.randomUUID().toString().replace("-", "")));
        teams.add(new Team(8, "B10班组", UUID.randomUUID().toString().replace("-", "")));
        teams.add(new Team(9, "B11班组", UUID.randomUUID().toString().replace("-", "")));
        return new RestResponse(HttpStatus.OK, null, teams);
    }

    /**
     * 发布任务
     *
     * @return
     */
    @GetMapping("/publishTask")
    public RestResponse publishTask() {
        return new RestResponse(HttpStatus.OK);
    }
}
