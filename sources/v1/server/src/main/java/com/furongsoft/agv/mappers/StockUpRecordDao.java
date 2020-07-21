package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.*;
import com.furongsoft.agv.models.StockUpRecordModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

/**
 * 物料表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface StockUpRecordDao extends BaseMapper<StockUpRecord> {

    /**
     * 通过备货ID获取备货信息
     *
     * @param id 备货ID
     * @return 备货信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectStockUpRecordById")
    StockUpRecordModel selectStockUpRecordById(@Param("id") Long id);

    /**
     * 通过编号删除备货信息
     *
     * @param code 备货编号
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "deleteStockUpRecordByCode")
    boolean deleteStockUpRecordByCode(@Param("code") String code);

    class DaoProvider {
        private static final String STOCK_UP_RECORD_TABLE_NAME = StockUpRecord.class.getAnnotation(TableName.class).value();
        private static final String STOCK_UP_RECORD_DETAIL_TABLE_NAME = StockUpRecordDetail.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_TABLE_NAME = Material.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_BOX_TABLE_NAME = MaterialBox.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_BOX_MATERIAL_TABLE_NAME = MaterialBoxMaterial.class.getAnnotation(TableName.class).value();
        private static final String DELIVERY_TASK_TABLE_NAME = DeliveryTask.class.getAnnotation(TableName.class).value();

        /**
         * 通过备货ID获取备货信息
         *
         * @return sql
         */
        public String selectStockUpRecordById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.site_id,t1.material_box_id,t1.type,t1.state,t1.delivery_task_id,t2.name AS materialBoxName, t2.qr_code AS materialBoxQrCode, " +
                            "t2.state AS materialBoxState, t3.task_no AS deliveryTaskNo");
                    FROM(STOCK_UP_RECORD_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_BOX_TABLE_NAME + " t2 ON t1.material_box_id = t2.id");
                    LEFT_OUTER_JOIN(DELIVERY_TASK_TABLE_NAME + " t3 ON t1.delivery_task_id = t3.id");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 根据编号删除备货
         *
         * @return sql
         */
        public String deleteStockUpRecordByCode() {
            return new SQL() {
                {
                    UPDATE(STOCK_UP_RECORD_TABLE_NAME);
                    SET("enabled=0");
                    WHERE("code=#{code}");
                }
            }.toString();
        }
    }
}
