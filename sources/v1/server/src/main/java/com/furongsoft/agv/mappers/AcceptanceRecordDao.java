package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.AcceptanceRecord;
import com.furongsoft.agv.models.AcceptanceRecordModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

/**
 * 验收记录表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface AcceptanceRecordDao extends BaseMapper<AcceptanceRecord> {

    @SelectProvider(type = DaoProvider.class, method = "selectAcceptanceRecordById")
    AcceptanceRecordModel selectAcceptanceRecordById(@Param("id") Long id);

    class DaoProvider {
        private static final String ACCEPTANCE_RECORD_TABLE_NAME = AcceptanceRecord.class.getAnnotation(TableName.class).value();

        public String selectAcceptanceRecordById() {
            return new SQL() {
                {
                    SELECT("t1.operation_time,t1.team_id,t1.team_name,t1.source,t1.results,t1.type,t1.area_id AS productId");
                    FROM(ACCEPTANCE_RECORD_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }


    }
}
