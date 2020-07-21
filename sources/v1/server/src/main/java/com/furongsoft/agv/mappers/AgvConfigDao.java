package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.AgvConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.io.Serializable;

/**
 * 配置信息表操作对象
 *
 * @author chenfuqian
 */
@Mapper
public interface AgvConfigDao extends BaseMapper<AgvConfig> {

    /**
     * 根据主键获取配置信息
     *
     * @param id 主键
     * @return 配置信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectAgvConfigById")
    AgvConfig selectAgvConfigById(@Param("id") Serializable id);

    class DaoProvider {
        private static final String AGV_CONFIG_TABLE_NAME = AgvConfig.class.getAnnotation(TableName.class).value();

        /**
         * 根据主键获取配置信息
         *
         * @return SQL
         */
        public String selectAgvConfigById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.site_code,t1.material_car_code,t1.enabled");
                    FROM(AGV_CONFIG_TABLE_NAME + " t1 ");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }
    }
}
