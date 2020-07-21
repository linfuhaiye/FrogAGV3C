package com.furongsoft.base.order.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.order.entities.OrderNo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * 订单号数据库操作类
 *
 * @author chenfuqian
 */
@Mapper
public interface OrderNoDao extends BaseMapper<OrderNo> {

    /**
     * 根据业务编号查询当前编号、主键
     *
     * @param orderCode 业务编号
     * @return 最新编号
     */
    @SelectProvider(type = DaoProvider.class, method = "getNewOrderNo")
    Long getNewOrderNo(String orderCode);

    class DaoProvider {

        /**
         * 根据业务编号查询当前编号、主键
         *
         * @return SQL语句
         */
        public String getNewOrderNo() {
            return "CALL getNewOrderNo(#{orderCode})";
        }

    }
}
