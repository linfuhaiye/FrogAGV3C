package com.furongsoft.frogagv;

import com.furongsoft.agv.entities.Site;
import com.furongsoft.agv.schedulers.IScheduler;
import com.furongsoft.agv.schedulers.entities.Task;
import com.furongsoft.agv.schedulers.entities.Task.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GeekPlusTests {
    private final Site[] sites = new Site[35];
    private final String containerId1 = "PA000001";
    private final String containerId2 = "PA000002";
    private final String containerId3 = "PA000003";
    @Autowired
    private IScheduler scheduler;

    /**
     * 初始化
     */
    @BeforeEach
    void initialize() {
        scheduler.removeAllContainers();

        for (int i = 0; i < sites.length; ++i) {
            sites[i] = new Site();
            sites[i].setCode(String.valueOf(i));
        }
    }

    @Test
    public void Test0() {
//        ModbusTcp.test();
    }

    /**
     * 下发一个A点到B点的任务</br>
     * 前置条件: A点上无容器,B点上无容器</br>
     * 测试步骤: 在A点执行容器入场->下发一个A点到B点的任务</br>
     * 预计结果: A点容器入场成功,下发任务成功</br>
     *
     * @throws Exception
     */
    @Test
    void test1() throws Exception {
        boolean result = scheduler.addContainer(containerId1, sites[2].getCode());
        assertEquals(true, result);

        Task task = scheduler.addTask(sites[2], sites[4], null);
        assertEquals(true, task != null);
    }

    /**
     * 下发一个A点到B点的任务</br>
     * 前置条件: A点上无容器,B点上有容器</br>
     * 测试步骤: 清空A、B两点容器,在B上入场一个容器,在A点执行容器入场->下发一个A点到B点的任务</br>
     * 预计结果: B点容器入场成功,A点容器入场成功,下发任务失败</br>
     *
     * @throws Exception
     */
    @Test
    void test2() throws Exception {
        boolean result = scheduler.addContainer(containerId1, sites[2].getCode());
        assertEquals(true, result);

        result = scheduler.addContainer(containerId2, sites[5].getCode());
        assertEquals(true, result);

        Task task = scheduler.addTask(sites[2], sites[5], null);
        assertEquals(false, task != null);
    }

    /**
     * 下发一个A点到B点的任务后,再下发B点到A点的任务</br>
     * 前置条件: A点上无容器,B点上无容器</br>
     * 测试步骤: 清空A、B两点容器.在A点容器入场->下发一个A点到B点的任务->B点容器入场->下发一个B点到A点的任务</br>
     * 预计结果: A点容器入场成功,B点容器入场失败,A到B任务下发成功,B到A任务下发失败</br>
     *
     * @throws Exception
     */
    @Test
    void test3() throws Exception {
        boolean result = scheduler.addContainer(containerId1, sites[2].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[2], sites[5], null);
        assertEquals(true, task1 != null);

        result = scheduler.addContainer(containerId2, sites[5].getCode());
        assertEquals(false, result);

        Task task2 = scheduler.addTask(sites[5], sites[2], null);
        assertEquals(false, task2 != null);
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
    @Test
    void test4() throws Exception {
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

        result = scheduler.addContainer(containerId2, sites[5].getCode());
        assertEquals(false, result);

        Task task2 = scheduler.addTask(sites[5], sites[2], null);
        assertEquals(false, task2 != null);
    }

    /**
     * 下发一个A点到B点的任务后,下发一个C点到A点的任务</br>
     * 前置条件: A点上无容器,B点上无容器,C点无容器</br>
     * 测试步骤: 清空A、B、C三点容器.在A点容器入场->下发一个A点到B点的任务->在C点容器入场->下发一个C到A的任务</br>
     * 预计结果: A点容器入场成功、A到B任务下发成功,C点容器入场成功,C到A下发任务失败</br>
     *
     * @throws Exception
     */
    @Test
    void test5() throws Exception {
        boolean result = scheduler.addContainer(containerId1, sites[4].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[4], sites[2], null);
        assertEquals(true, task1 != null);

        result = scheduler.addContainer(containerId3, sites[5].getCode());
        assertEquals(true, result);

        Task task2 = scheduler.addTask(sites[5], sites[4], null);
        assertEquals(false, task2 != null);
    }

    /**
     * 下发一个A点到B点的任务后,下发一个B点到D点的任务</br>
     * 前置条件: A点上无容器,B点上无容器,D点无容器</br>
     * 测试步骤: 清空A、B、D三点容器.在A点容器入场->下发一个A点到B点的任务->B点容器入场->下发一个B点到D点的任务</br>
     * 预计结果: A点容器入场成功、A到B任务下发成功,B点容器入场失败,下发B点到D点的任务失败</br>
     *
     * @throws Exception
     */
    @Test
    void test7() throws Exception {
        boolean result = scheduler.addContainer(containerId1, sites[2].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[2], sites[5], null);
        assertEquals(true, task1 != null);

        result = scheduler.addContainer(containerId2, sites[5].getCode());
        assertEquals(false, result);

        Task task2 = scheduler.addTask(sites[5], sites[4], null);
        assertEquals(false, task2 != null);
    }

    /**
     * 下发一个A点到B点的任务后,下发一个C点到D点的任务</br>
     * 前置条件: A点上无容器,B点上无容器,C点无容器,D点无容器</br>
     * 测试步骤: 清空A、B、C、D四点容器.在A点容器入场->下发一个A点到B点的任务->在C点容器入场->下发一个C点到D点的任务</br>
     * 预计结果: A点容器入场成功、A到B任务下发成功.C点容器入场成功、C到D任务下发成功</br>
     *
     * @throws Exception
     */
    @Test
    void test8() throws Exception {
        boolean result = scheduler.addContainer(containerId1, sites[2].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[2], sites[5], null);
        assertEquals(true, task1 != null);

        result = scheduler.addContainer(containerId3, sites[4].getCode());
        assertEquals(true, result);

        Task task2 = scheduler.addTask(sites[4], sites[7], null);
        assertEquals(true, task2 != null);
    }

    /**
     * 下发一个B点到A点的任务后,下发一个C点到A点的任务</br>
     * 前置条件: A点上无容器,B点上无容器,C点无容器</br>
     * 测试步骤: 清空A、B、C三点容器.在B点容器入场->下发一个B点到A点的任务->C点容器入场->下发一个C点到A点的任务</br>
     * 预计结果: B点容器入场成功、B到A任务下发成功.C点容器入场成功、C到A任务下发成功</br>
     *
     * @throws Exception
     */
    @Test
    void test9() throws Exception {
        boolean result = scheduler.addContainer(containerId2, sites[5].getCode());
        assertEquals(true, result);

        Task task1 = scheduler.addTask(sites[5], sites[2], null);
        assertEquals(true, task1 != null);

        result = scheduler.addContainer(containerId3, sites[4].getCode());
        assertEquals(true, result);

        Task task2 = scheduler.addTask(sites[4], sites[2], null);
        assertEquals(true, task2 != null);
    }

    @Test
    void read_remote_button() {
//        com.furongsoft.communication.modbusTcp.ModbusTcp.test();
    }
}
