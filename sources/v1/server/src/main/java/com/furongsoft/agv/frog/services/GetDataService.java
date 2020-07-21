package com.furongsoft.agv.frog.services;

import com.alibaba.fastjson.JSONObject;
import com.furongsoft.agv.frog.entities.BaseRequestMsg;
import com.furongsoft.agv.frog.entities.GetBzRequestMsg;
import com.furongsoft.agv.frog.entities.GetBzResponseMsg;
import com.furongsoft.base.misc.DateUtils;
import com.furongsoft.base.misc.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取数据服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GetDataService {
    @Value("${qwwz.url}")
    private String url;

    @Value("${qwwz.format}")
    private String format;

    @Value("${qwwz.app_key}")
    private String appKey;

    @Value("${qwwz.simplify}")
    private boolean simplify;

    @Value("${qwwz.version}")
    private String version;

    @Value("${qwwz.pkCorp}")
    private String pkCorp;

    /**
     * 获取生产班组（B区和C区的班组）
     *
     * @return 生产班组集合
     */
    public List<GetBzResponseMsg.DataEntity> getProductTeams() {
        GetBzResponseMsg getBzResponseMsg = getTeams();
        List<GetBzResponseMsg.DataEntity> allTeams = getBzResponseMsg.getErp_basedoc_bz_get_response().getData();
        List<GetBzResponseMsg.DataEntity> backTeams = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allTeams)) {
            allTeams.forEach(team -> {
                if (team.getDoccode().indexOf("B") > -1 || team.getDoccode().indexOf("C") > -1) {
                    backTeams.add(team);
                }
            });
        }
        // 根据doccode对list进行排序
        backTeams.sort(Comparator.comparing(GetBzResponseMsg.DataEntity::getDoccode));
        return backTeams;
    }

    /**
     * 获取所有班组
     *
     * @return 获取班组返回参数
     */
    public GetBzResponseMsg getTeams() {
        Map<String, String> dateMap = DateUtils.getYesterdayTodayTomorrowString("yyyy-MM-dd HH:mm:ss");
        // 需要查询的字段
        String fields = "pk_defdoc,doccode,docname";
        GetBzRequestMsg getBzRequestMsg = new GetBzRequestMsg();
        getBzRequestMsg.setFields(fields);
        getBzRequestMsg.setCreated_start("2000-01-01 00:00:00");
        getBzRequestMsg.setCreated_end(dateMap.get("today"));
        getBzRequestMsg.setPage_no(1);
        getBzRequestMsg.setPage_size(200);
        getBzRequestMsg.setPage_use_has_next(false);
        BaseRequestMsg baseRequestMsg = new BaseRequestMsg("erp.basedoc.bz.get", appKey, getCurrentTimestamp(), format, version, simplify, null, JSONObject.toJSONString(getBzRequestMsg));
        return HttpUtils.getJson(url, null, baseRequestMsg.toParameter(), GetBzResponseMsg.class);
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss 格式
     *
     * @return
     */
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


}
