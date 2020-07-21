package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.Acceptance;
import com.furongsoft.agv.models.AcceptanceModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

/**
 * 验收表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface AcceptanceDao extends BaseMapper<Acceptance> {

    @SelectProvider(type = DaoProvider.class, method = "selectAcceptanceById")
    AcceptanceModel selectAcceptanceById(@Param("id") Long id);

    class DaoProvider {
        private static final String ACCEPTANCE_TABLE_NAME = Acceptance.class.getAnnotation(TableName.class).value();

        public String selectAcceptanceById() {
            return new SQL() {
                {
                    SELECT("t1.call_material_id,t1.count,t1.acceptance_time,t1.team_id,t1.team_name,t1.area_id AS productId,t1.delivery_task_id");
                    FROM(ACCEPTANCE_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }


    }
}
