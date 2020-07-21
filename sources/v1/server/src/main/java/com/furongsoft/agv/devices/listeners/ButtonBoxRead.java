package com.furongsoft.agv.devices.listeners;

import com.furongsoft.agv.devices.model.ButtonBoxModel;
import com.furongsoft.agv.devices.model.TaskModel;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.communication.modbusTcp.ModbusTcp;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;

import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

/**
 * 按钮盒子读取监听
 *
 * @author linyehai
 */
public class ButtonBoxRead implements Runnable {
    private final ButtonBoxModel buttonBoxModel;
    int[] registersOffset = {3, 8, 13, 18}; // 闪开寄存器偏移

    public ButtonBoxRead(ButtonBoxModel buttonBoxModel) {
        this.buttonBoxModel = buttonBoxModel;
    }

    @Override
    public void run() {
        boolean runState = true;
        try {
            while (runState) {
                synchronized (this) {
                    try {
                        Thread.sleep(500);
                        // 未连接则创建连接,已连接则执行读取
                        ModbusMaster currentMaster = buttonBoxModel.getModbusMaster();
                        if (null != currentMaster) {
                            // 执行读取
                            short[] data = ModbusTcp.readInputRegisters(currentMaster, 254, 1000 + 4, 1);
                            if (null == data) {
                                return;
                            }
                            ByteBuffer byteBuffer = ByteBuffer.allocate(data.length * Short.SIZE / Byte.SIZE);
                            for (short s : data) {
                                byteBuffer.putShort(s);
                            }
                            boolean[] result = ModbusTcp.convert(byteBuffer);
                            for (int i = 0; i < buttonBoxModel.getCallButtonModels().size(); ++i) {
                                if (result[i]) {
                                    addTask(currentMaster, i);
                                    Tracker.agv("ButtonBoxRead-addTask:" + buttonBoxModel.getCallButtonModelByButtonCode(i).getName());
                                }
                            }
                        } else {
                            ModbusMaster modbusMaster = ModbusTcp.createMaster(buttonBoxModel.getIpAddress(), buttonBoxModel.getPort());
                            if (null != modbusMaster) {
                                Tracker.agv("连接成功");
                                buttonBoxModel.setModbusMaster(modbusMaster);
                            }
                        }
                    } catch (Exception e) {
                        Tracker.error("========读取线程异常===========");
                        Tracker.agv(String.format("按钮盒子连接异常。IP: %s", buttonBoxModel.getIpAddress()));
                        if (e instanceof SocketTimeoutException) {
                            Tracker.agv(String.format("按钮盒子断开连接。IP: %s", buttonBoxModel.getIpAddress()));
                            ModbusMaster currentMaster = buttonBoxModel.getModbusMaster();
                            currentMaster.destroy();
                            buttonBoxModel.setModbusMaster(null);
                        }
                        Tracker.error(e.getCause());
                        Tracker.error("===========********==============");
                    }
                }
            }
        } catch (Exception exception) {
            Tracker.error("读取线程异常 ==》 跳出循环");
            Tracker.error(exception);
        }
    }

    /**
     * 添加任务
     *
     * @param codeNo 按钮编号
     */
    public void addTask(ModbusMaster currentMaster, int codeNo) throws ModbusTransportException {
        TaskModel taskModel = new TaskModel(codeNo, buttonBoxModel.getIpAddress(), null);
        synchronized (buttonBoxModel.getCallTaskModels()) {
            if (buttonBoxModel.getCallTaskModels().contains(taskModel)) {
                Tracker.error("不添加呼叫任务");
                return;
            } else {
                // 亮灯1分钟
                ModbusTcp.writeRegisters(currentMaster, 254, registersOffset[codeNo], new short[]{0x0004, 0x0258});
                Tracker.agv("添加一个呼叫任务");
                buttonBoxModel.getCallTaskModels().add(taskModel);
            }
        }
    }
}
