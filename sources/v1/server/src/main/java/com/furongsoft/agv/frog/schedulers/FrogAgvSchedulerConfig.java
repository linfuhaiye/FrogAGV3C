package com.furongsoft.agv.frog.schedulers;

import com.furongsoft.base.misc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

/**
 * 青蛙AGV系统任务调度配置
 *
 * @author linyehai
 */
@Configuration
@EnableScheduling
@Order(1)
public class FrogAgvSchedulerConfig implements SchedulingConfigurer {

    private final ProductionPlanScheduler productionPlanScheduler;

    @Autowired
    public FrogAgvSchedulerConfig(ProductionPlanScheduler productionPlanScheduler) {
        this.productionPlanScheduler = productionPlanScheduler;
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                productionPlanScheduler::getProductionPlan,
                triggerContext -> {
                    // 每天4点和16点执行一次
                    String cron = "0 0 4,16 * * ?";
                    if (StringUtils.isNullOrEmpty(cron)) {
                        return null;
                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }
}
