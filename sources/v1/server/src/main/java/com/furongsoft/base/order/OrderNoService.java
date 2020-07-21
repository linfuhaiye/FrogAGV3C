package com.furongsoft.base.order;

import com.furongsoft.base.order.mappers.OrderNoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单服务
 * 主要用于生成订单号
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderNoService {

    private final OrderNoDao orderNoDao;

    @Autowired
    public OrderNoService(OrderNoDao orderNoDao) {
        this.orderNoDao = orderNoDao;
    }

    /**
     * 根据编码获取当天指定位数号
     *
     * @param code   编码
     * @param length 指定位数
     * @return 最新编号
     */
    public synchronized String getNewOrderNumByCodeDate(String code, int length) {
//        OrderNo orderNo =  orderNoDao.selectOrderNoByCode(code);
//        if ((orderNo == null) || (!orderNo.getFlag())) {
//            orderNoDao.insertOrderNo(code);
//            return String.format("%0" + length + "d", 1);
//        }
//        orderNo.setOrderNo(orderNo.getOrderNo() + 1);
//        orderNoDao.updateOrderNoById(orderNo.getId(),orderNo.getOrderNo());
        Long newOrderNo = orderNoDao.getNewOrderNo(code);
        return String.format("%0" + length + "d", newOrderNo);
    }
}
