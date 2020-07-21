package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.rbac.entities.Config;
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
public interface ConfigDao extends BaseMapper<Config> {

    /**
     * 根据主键获取配置信息
     *
     * @param id 主键
     * @return 配置信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectConfigById")
    Config selectConfigById(@Param("id") Serializable id);

    class DaoProvider {
        /**
         * 根据主键获取配置信息
         *
         * @return SQL
         */
        public String selectConfigById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.level,t1.threshold,attachment_id AS attachmentId,t2.url AS logo,t1.attachment_server");
                    FROM("t_sys_config t1 LEFT JOIN t_sys_attachment t2 ON t1.attachment_id = t2.id");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }
    }
}
