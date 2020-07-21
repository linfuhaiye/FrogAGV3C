package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.furongsoft.base.entities.SelectItem;
import com.furongsoft.base.misc.StringUtils;
import com.furongsoft.base.rbac.entities.Permission;
import com.furongsoft.base.rbac.entities.Resource;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 权限表操作对象
 *
 * @author Alex
 */
@Mapper
@Component
public interface PermissionDao extends BaseMapper<Permission> {
    /**
     * 获取所有用户
     *
     * @param page 分页信息
     * @param name 名字
     * @return 权限列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectPermissionListWithParams")
    List<Permission> selectPermissionList(Pagination page, @Param("name") String name);

    /**
     * 根据索引获取用户
     *
     * @param id 索引
     * @return 权限
     */
    @SelectProvider(type = DaoProvider.class, method = "selectPermissionById")
    Permission selectPermission(@Param("id") Serializable id);

    /**
     * 检查权限是否存在
     *
     * @param name 权限名称
     * @param id   索引
     * @return 权限
     */
    @SelectProvider(type = DaoProvider.class, method = "selectForUpdate")
    Permission selectForUpdate(@Param("name") String name, @Param("id") Serializable id);

    /**
     * 获取权限相关资源
     *
     * @param id 索引
     * @return 选项列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectPermissionsResources")
    List<SelectItem> selectPermissionsResources(@Param("id") Serializable id);

    /**
     * 根据权限ID 删除权限资源关系
     *
     * @param id 权限ID
     */
    @DeleteProvider(type = DaoProvider.class, method = "deletePermissionsResources")
    void deletePermissionsResources(@Param("id") Serializable id);

    class DaoProvider {
        private static final String PERMISSION = Permission.class.getAnnotation(TableName.class).value();
        private static final String RESOURCE = Resource.class.getAnnotation(TableName.class).value();
        private static final String PERMISSION_RESOURCE = "t_sys_permission_resource";

        /**
         * 获取所有权限
         *
         * @param param 参数列表
         * @return SQL语句
         */
        public String selectPermissionListWithParams(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("id, name,remark,state, create_time, create_user");
                    FROM(PERMISSION);
                    WHERE("state=0");
                    if (!StringUtils.isNullOrEmpty(param.get("name"))) {
                        WHERE("name LIKE CONCAT('%', #{name},'%')");
                    }
                    ORDER_BY("id");
                }
            }.toString();
        }

        /**
         * 根据索引获取权限信息
         *
         * @param param 参数列表
         * @return SQL语句
         */
        public String selectPermissionById(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("id, name,state, create_time, create_user");
                    FROM(PERMISSION);
                    WHERE("state = 0");
                    if (param.get("id") != null) {
                        WHERE("id = #{id}");
                    }
                }
            }.toString();
        }

        /**
         * 检查权限名称是否存在
         *
         * @return SQL语句
         */
        public String selectForUpdate(final Map<String, Object> param) {
            String sql = "SELECT id FROM " + PERMISSION + " WHERE state = 0";
            if (param.get("id") != null) {
                sql += " AND id != #{id}";
            }
            sql += " AND name = #{name} FOR UPDATE";
            return sql;
        }

        /**
         * 获取权限相关资源
         *
         * @return SQL语句
         */
        public String selectPermissionsResources() {
            return new SQL() {
                {
                    SELECT("sr.name, sr.id AS `value`,CASE WHEN sar.resource_id IS NULL THEN FALSE ELSE TRUE END AS selected");
                    FROM(RESOURCE + " sr");
                    LEFT_OUTER_JOIN(PERMISSION_RESOURCE + " sar ON sr.id = sar.resource_id AND sar.permission_id = #{id}");
                    WHERE("sr.state = 0");
                }
            }.toString();
        }

        /**
         * 根据权限ID 删除权限资源关系
         *
         * @return SQL语句
         */
        public String deletePermissionsResources() {
            return new SQL() {
                {
                    DELETE_FROM(PERMISSION_RESOURCE);
                    WHERE("permission_id = #{id}");
                }
            }.toString();
        }
    }
}
