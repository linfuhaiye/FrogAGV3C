package com.furongsoft.agv.schedulers.test;

import com.furongsoft.agv.entities.Site;
import com.furongsoft.agv.schedulers.IScheduler;
import com.furongsoft.agv.schedulers.entities.Task;
import com.furongsoft.agv.schedulers.entities.Task.Status;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.Assert.assertEquals;

/**
 * 调度器测试用例
 *
 * @author Alex
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/agv/test")
public class SchedulerTests implements InitializingBean {
    private final Site[] sites = new Site[35];
    private final String containerId1 = "PA000001";
    private final String containerId2 = "PA000002";
    private final String containerId3 = "PA000003";
    @Autowired
    private IScheduler scheduler;

    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = 0; i < sites.length; ++i) {
            sites[i] = new Site();
            sites[i].setCode(String.valueOf(i));
        }
    }

    /**
     * 获取任务信息
     *
     * @return 任务信息
     */
    @GetMapping("/tasks")
    public String getTasks() {
        StringBuffer sb = new StringBuffer();
        scheduler.getTasks().forEach(task -> sb.append(task.toString()).append("\n"));
        return sb.toString();
    }

    /**
     * 下发一个A点到B点的任务后,AGV小车把货架取走后,下发一个B点到A点的任务</br>
     * 前置条件: A点上无容器,B点上无容器</br>
     * 测试步骤:
     * 清空A、B两点容器.在A点容器入场->下发一个A点到B点的任务->AGV小车取走A点的货架->在B点容器入场->下发一个B点到A点的任务</br>
     * 预计结果: A点容器入场成功,A到B任务下发成功,B点容器入场失败,B到A任务下发失败</br>
     *
     * @throws Exception
     */
    @GetMapping("/test4")
    public void test4() throws Exception {
        scheduler.removeAllContainers();

        boolean result = scheduler.addContainer(containerId1, sites[2].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[2], sites[5], null);
        assertEquals(true, task1 != null);

        // 等待AGV小车取走A点的货架
        while (task1.getStatus() != Status.Moving) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        result = scheduler.addContainer(containerId2, sites[2].getCode());
        assertEquals(false, result);

        Task task2 = scheduler.addTask(sites[5], sites[2], null);
        assertEquals(false, task2 != null);
    }

    /**
     * 下发一个A点到B点的任务后,AGV小车把货架取走后,下发一个C点到A点的任务</br>
     * 前置条件: A点上无容器,B点上无容器,C点无容器</br>
     * 测试步骤:
     * 清空A、B、C三点容器.在A点容器入场->下发一个A点到B点的任务->AGV小车取走A点的货架->C点容器入场->下发一个C到A的任务</br>
     * 预计结果: A点容器入场成功、A到B任务下发成功,C点容器入场成功,C到A下发任务成功</br>
     *
     * @throws Exception
     */
    @GetMapping("/test6")
    public void test6() throws Exception {
        boolean result = scheduler.addContainer(containerId1, sites[2].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[2], sites[5], null);
        assertEquals(true, task1 != null);

        // 等待AGV小车取走A点的货架
        while (task1.getStatus() != Status.Moving) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        result = scheduler.addContainer(containerId3, sites[4].getCode());
        assertEquals(true, result);

        Task task2 = scheduler.addTask(sites[4], sites[2], null);
        assertEquals(true, task2 != null);
    }

    /**
     * 下发一个A点到B点的任务后,AGV小车把货架取走后,取消任务</br>
     * 前置条件: A点上无容器,B点上无容器</br>
     * 测试步骤: 清空A、B两点容器.在A点容器入场->下发一个A点到B点的任务->AGV小车取走A点的货架->取消任务</br>
     * 预计结果: A点容器入场成功,A到B任务下发成功,取消任务成功</br>
     *
     * @throws Exception
     */
    @GetMapping("/test10")
    public void test10() throws Exception {
        scheduler.removeAllContainers();

        boolean result = scheduler.addContainer(containerId1, sites[2].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[2], sites[5], null);
        assertEquals(true, task1 != null);

        // 等待AGV小车取走A点的货架
        while (task1.getStatus() != Status.Moving) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        result = scheduler.cancel(task1);
        assertEquals(true, result);
    }

    /**
     * 包装区逐个站点搬运任务测试
     *
     * @throws Exception
     */
    @GetMapping("/test11")
    public void test11() throws Exception {
        scheduler.removeAllContainers();

        boolean result = scheduler.addContainer(containerId1, sites[8].getCode());
        assertEquals(true, result);

        Task task = scheduler.addTask(sites[8], sites[14], null);
        assertEquals(true, task != null);

        for (int i = 14; i < 21; ++i) {
            // 等待AGV小车取走A点的货架
            while (task.getStatus() != Status.Arrived) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            task = scheduler.addTask(sites[i], sites[i + 1], null);
            assertEquals(true, task != null);
        }
    }

    /**
     * 拆包间逐个站点搬运任务测试
     *
     * @throws Exception
     */
    @GetMapping("/test12")
    public void test12() throws Exception {
        scheduler.removeAllContainers();

        boolean result = scheduler.addContainer(containerId1, sites[21].getCode());
        assertEquals(true, result);

        Task task = scheduler.addTask(sites[21], sites[8], null);
        assertEquals(true, task != null);

        int[] dests = new int[]{8, 9, 11, 12, 13};
        for (int i = 0; i < dests.length - 1; ++i) {
            // 等待AGV小车取走A点的货架
            while (task.getStatus() != Status.Arrived) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            task = scheduler.addTask(sites[dests[i]], sites[dests[i + 1]], null);
            assertEquals(true, task != null);
        }
    }

    /**
     * 消毒间逐个站点搬运任务测试
     *
     * @throws Exception
     */
    @GetMapping("/test13")
    void test13() throws Exception {
        scheduler.removeAllContainers();

        boolean result = scheduler.addContainer(containerId1, sites[22].getCode());
        assertEquals(true, result);

        for (int i = 22; i < 26; ++i) {
            Task task = scheduler.addTask(sites[i], sites[i + 1], null);
            assertEquals(true, task != null);

            // 等待AGV小车取走A点的货架
            while (task.getStatus() != Status.Arrived) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * 灌装区逐个站点搬运任务测试
     *
     * @throws Exception
     */
    @GetMapping("/test14")
    void test14() throws Exception {
        scheduler.removeAllContainers();

        boolean result = scheduler.addContainer(containerId1, sites[26].getCode());
        assertEquals(true, result);

        Task task = scheduler.addTask(sites[26], sites[27], null);
        assertEquals(true, task != null);

        for (int i = 27; i < 34; ++i) {
            // 等待AGV小车取走A点的货架
            while (task.getStatus() != Status.Arrived) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            task = scheduler.addTask(sites[i], sites[i + 1], null);
            assertEquals(true, task != null);
        }
    }

    /**
     * AGV避让测试
     *
     * @throws Exception
     */
    @GetMapping("/test15")
    void test15() throws Exception {
        scheduler.removeAllContainers();

        boolean result = scheduler.addContainer(containerId1, sites[4].getCode());
        assertEquals(true, result);

        // 仓库-包装
        Task task1 = scheduler.addTask(sites[4], sites[20], null);
        assertEquals(true, task1 != null);

        // 等待AGV小车取走A点的货架
        while (task1.getStatus() != Status.Moving) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        boolean result2 = scheduler.addContainer(containerId2, sites[20].getCode());
        assertEquals(true, result2);

        // 包装-仓库
        Task task2 = scheduler.addTask(sites[20], sites[4], null);
        assertEquals(true, task2 != null);

        boolean result3 = scheduler.addContainer(containerId3, sites[9].getCode());
        assertEquals(true, result3);

        // 包材-仓库
        Task task3 = scheduler.addTask(sites[9], sites[5], null);
        assertEquals(true, task3 != null);
    }

    /**
     * 点到点搬运，搬走后取消
     *
     * @param startCode      起始站点
     * @param endCode        目标站点
     * @param containerCode1 容器1编号
     * @param containerCode2 容器2编号
     * @throws Exception
     */
    @GetMapping("/test16")
    public void test16(int startCode, int endCode, String containerCode1, String containerCode2) throws Exception {
        scheduler.removeAllContainers();

        boolean result = scheduler.addContainer(containerCode1, sites[startCode].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[startCode], sites[endCode], null);
        assertEquals(true, task1 != null);

        // 等待AGV小车取走A点的货架
        while (task1.getStatus() != Status.Moving) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        result = scheduler.cancel(task1);
        assertEquals(true, result);
    }

    /**
     * 点到点搬运
     *
     * @param startCode      起始站点
     * @param endCode        目标站点
     * @param containerCode1 容器1编号
     * @param containerCode2 容器2编号
     * @throws Exception
     */
    @GetMapping("/test17")
    public void test17(int startCode, int endCode, String containerCode1, String containerCode2) throws Exception {
        scheduler.removeAllContainers();

        boolean result = scheduler.addContainer(containerCode1, sites[startCode].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[startCode], sites[endCode], null);
        assertEquals(true, task1 != null);
    }

}
