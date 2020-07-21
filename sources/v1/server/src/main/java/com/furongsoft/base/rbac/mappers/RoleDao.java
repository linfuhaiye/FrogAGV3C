package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.furongsoft.base.entities.SelectItem;
import com.furongsoft.base.misc.StringUtils;
import com.furongsoft.base.rbac.entities.Permission;
import com.furongsoft.base.rbac.entities.Role;
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
 * 用户表操作对象
 *
 * @author Alex
 */
@Mapper
@Component
public interface RoleDao extends BaseMapper<Role> {
    /**
     * 获取所有角色
     *
     * @param page 分页信息
     * @param name 名字
     * @return 角色列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectRoleListWithParams")
    List<Role> selectRoleList(Pagination page, @Param("name") String name);

    /**
     * 根据索引获取角色
     *
     * @param id 索引
     * @return 资源
     */
    @SelectProvider(type = DaoProvider.class, method = "selectRoleById")
    Role selectRole(@Param("id") Serializable id);

    /**
     * 获取角色相关权限
     *
     * @param id 索引
     * @return 选项列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectRolePermission")
    List<SelectItem> selectRolePermission(@Param("id") Serializable id);

    /**
     * 根据角色ID 删除角色权限关系
     *
     * @param id 索引
     */
    @DeleteProvider(type = DaoProvider.class, method = "deleteRolePermission")
    void deleteRolePermission(@Param("id") Serializable id);

    /**
     * 根据角色名获取相关用户
     *
     * @param roleName 角色名
     * @return 用户
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUsersByRoleName")
    List<String> selectUsersByRoleName(@Param("roleName") String roleName);

    class DaoProvider {
        private final static String ROLE_TABLE_NAME = Role.class.getAnnotation(TableName.class).value();
        private final static String ROLE_PERMISSION_TABLE_NAME = "t_sys_role_permission";
        private final static String PERMISSION_TABLE_NAME = Permission.class.getAnnotation(TableName.class).value();


        /**
         * 获取所有角色
         *
         * @param param 参数类型
         * @return SQL语句
         */
        public String selectRoleListWithParams(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("id, `name`, `code`, state, create_time, create_user,`system`");
                    FROM(ROLE_TABLE_NAME);
                    WHERE("state = 0");
                    if (!StringUtils.isNullOrEmpty(param.get("name"))) {
                        WHERE("name LIKE CONCAT('%', #{name},'%')");
                    }
                }
            }.toString();
        }

        /**
         * 根据索引获取角色信息
         *
         * @return SQL语句
         */
        public String selectRoleById() {
            return new SQL() {
                {
                    SELECT("id,name");
                    FROM(ROLE_TABLE_NAME);
                    WHERE("state = 0 AND id = #{id}");

                }
            }.toString();
        }

        /**
         * 获取角色权限相关资源
         *
         * @return SQL语句
         */
        public String selectRolePermission() {
            return new SQL() {
                {
                    SELECT("sr.name, sr.id AS `value`,CASE WHEN sar.permission_id IS NULL THEN FALSE ELSE TRUE END AS selected");
                    FROM(PERMISSION_TABLE_NAME + " sr");
                    LEFT_OUTER_JOIN(ROLE_PERMISSION_TABLE_NAME + " sar ON sr.id = sar.permission_id AND sar.role_id = #{id}");
                    WHERE("sr.state = 0");
                }
            }.toString();
        }

        /**
         * 根据角色ID 删除角色权限关系
         *
         * @return SQL语句
         */
        public String deleteRolePermission() {
            return new SQL() {
                {
                    DELETE_FROM(ROLE_PERMISSION_TABLE_NAME);
                    WHERE("role_id = #{id}");
                }
            }.toString();
        }

        /**
         * 根据角色名称获取用户ID列表
         *
         * @return 用户ID列表
         */
        public String selectUsersByRoleName() {
            return "SELECT t1.id" +
                    " FROM t_sys_user t1,t_sys_role t2,t_sys_user_role t3" +
                    " WHERE t1.id = t3.user_id" +
                    " AND t2.id = t3.role_id" +
                    " AND t2.name = #{roleName}";
        }
    }
}
