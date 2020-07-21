package com.furongsoft.frogagv;

import com.furongsoft.agv.devices.model.CallButtonModel;
import com.furongsoft.agv.frog.entities.GetBillFullInfoResponseMsg;
import com.furongsoft.agv.frog.entities.GetBomInfoResponseMsg;
import com.furongsoft.agv.frog.entities.GetMoResponseMsg;
import com.furongsoft.agv.frog.schedulers.ProductionPlanScheduler;
import com.furongsoft.agv.models.DeliveryTaskModel;
import com.furongsoft.agv.services.CallMaterialService;
import com.furongsoft.agv.services.DeliveryTaskService;
import com.furongsoft.base.misc.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FrogTests {

    @Autowired
    private ProductionPlanScheduler productionPlanScheduler;
    @Autowired
    private CallMaterialService callMaterialService;
    @Autowired
    private DeliveryTaskService deliveryTaskService;

    @Test
    public void GetMoTest() {
        GetMoResponseMsg getMoResponseMsg = productionPlanScheduler.getMo(ProductionPlanScheduler.DayType.today);
        List<GetMoResponseMsg.DataEntity> moEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(getMoResponseMsg.getErp_mm_mo_get_response().getData())) {
            getMoResponseMsg.getErp_mm_mo_get_response().getData().forEach(moEntity -> {
                if ((!StringUtils.isNullOrEmpty(moEntity.getProductlinename())) && (moEntity.getProductlinename().indexOf("3B") >= 0)) {
                    moEntities.add(moEntity);
                }
            });
        }
        System.out.println(getMoResponseMsg.toString());
    }

    @Test
    public void GetBomTest() {
        GetMoResponseMsg getMoResponseMsg = productionPlanScheduler.getMo(ProductionPlanScheduler.DayType.today);
        GetBomInfoResponseMsg getBomInfoResponseMsg = productionPlanScheduler.getBomInfo(getMoResponseMsg.getErp_mm_mo_get_response().getData().get(0));

        System.out.println(getBomInfoResponseMsg.toString());
    }

    @Test
    public void GetFullInfoList() {
        GetMoResponseMsg getMoResponseMsg = productionPlanScheduler.getMo(ProductionPlanScheduler.DayType.today);
        GetBillFullInfoResponseMsg getBillFullInfo = productionPlanScheduler.getBillFullInfo(getMoResponseMsg.getErp_mm_mo_get_response().getData());
        System.out.println(getBillFullInfo.toString());
    }

    @Test
    public void getProductionPlan() {
        productionPlanScheduler.getProductionPlan();
        System.out.println("**********");
    }

    @Test
    public void callTest() {
        CallButtonModel callButtonModel = new CallButtonModel();
        callButtonModel.setSiteId(52l);
        callButtonModel.setAreaId(23l);
        callMaterialService.callMaterial(callButtonModel);
    }

    @Test
    public void addDeliveryTaskTest() {
        DeliveryTaskModel deliveryTaskModel = new DeliveryTaskModel();
        deliveryTaskModel.setType(5);
        deliveryTaskModel.setProductLine("L12");
        deliveryTaskModel.setStartSiteId(37);
        try {
            deliveryTaskService.addDeliveryTask(deliveryTaskModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sqlTest() {
        callMaterialService.updateCallMaterialStateByWaveCode("3c275f1cebe241d49f201dbf9b0070ad", 4, 3);
    }

}
