package com.furongsoft.base.rbac.mappers;


import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.rbac.entities.Organization;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.Map;

/**
 * 组织管理操作对象
 *
 * @author chenfuqian
 */
public interface OrganizationDao extends BaseMapper<Organization> {
    /**
     * 检查组织编号是否存在
     *
     * @param code 用户编码
     * @param id   用户ID
     * @return 用户信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectForUpdate")
    Organization selectForUpdate(@Param("code") String code, @Param("id") Serializable id);

    class DaoProvider {
        private final static String ORGANIZATION = Organization.class.getAnnotation(TableName.class).value();


        /**
         * 检查部门编号是否存在
         *
         * @param param 参数列表
         * @return SQL语句
         */
        public String selectForUpdate(final Map<String, Object> param) {
            String sql = "SELECT id FROM " + ORGANIZATION + " WHERE state=0";
            if (param.get("id") != null) {
                sql += " AND id != #{id}";
            }
            sql += " AND code = #{code} FOR UPDATE";
            return sql;
        }

    }


}
