package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.file.entities.Attachment;
import com.furongsoft.base.rbac.entities.Permission;
import com.furongsoft.base.rbac.entities.Resource;
import com.furongsoft.base.rbac.entities.Role;
import com.furongsoft.base.rbac.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 资源表操作对象
 *
 * @author Alex
 */
@Mapper
@Component
public interface ResourceDao extends BaseMapper<Resource> {
    /**
     * 获取所有资源
     *
     * @return 资源列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectResourceList")
    List<Resource> selectResourceList();

    /**
     * 根据用户ID获取相关权限
     *
     * @param id 资源ID
     * @return 资源列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectResourcesByUserId")
    List<Resource> selectResourcesByUserId(@Param("id") Serializable id);

    /**
     * 检查资源名称是否存在
     *
     * @param name 资源名称
     * @param id   索引
     * @return 资源信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectForUpdate")
    Resource selectForUpdate(@Param("name") String name, @Param("id") Serializable id);

    class DaoProvider {
        private final String USER = User.class.getAnnotation(TableName.class).value();
        private final String USER_ROLE = "t_sys_user_role";
        private final String ROLE = Role.class.getAnnotation(TableName.class).value();
        private final String ROLE_PERMISSION = "t_sys_role_permission";
        private final String PERMISSION = Permission.class.getAnnotation(TableName.class).value();
        private final String PERMISSION_RESOURCE = "t_sys_permission_resource";
        private final String RESOURCE = Resource.class.getAnnotation(TableName.class).value();
        private final String ATTACHMENT = Attachment.class.getAnnotation(TableName.class).value();

        /**
         * 获取所有资源
         *
         * @return SQL语句
         */
        public String selectResourceList() {
            return new SQL() {{
                SELECT("t1.*, t2.name AS iconPath");
                FROM(RESOURCE + " t1");
                LEFT_OUTER_JOIN(ATTACHMENT + " t2 ON t1.icon = t2.id");
                WHERE(" t1.state = 0");
            }}.toString();
        }

        /**
         * 根据用户ID获取相关权限
         *
         * @return SQL语句
         */
        public String selectResourcesByUserId() {
            return new SQL() {{
                SELECT("DISTINCT t5.path,t5.name,t5.id,t5.parent_id AS parentId,t5.type,t5.priority");
                FROM(USER + " t1", USER_ROLE + " t2", ROLE_PERMISSION + " t3",
                        PERMISSION_RESOURCE + " t4", RESOURCE + " t5");
                WHERE("t1.id = t2.user_id AND t2.role_id = t3.role_id AND t3.permission_id = t4.permission_id AND t4.resource_id = t5.id",
                        " t5.state = 0",
                        " t1.id = #{id}");
                ORDER_BY(" t5.priority,t5.id ASC");
            }}.toString();
        }

        /**
         * 检查资源名称是否存在
         *
         * @return SQL语句
         */
        public String selectForUpdate(final Map<String, Object> param) {
            String sql = "SELECT id FROM " + RESOURCE + " WHERE 1=1";
            if (param.get("id") != null) {
                sql += " AND id != #{id}";
            }
            sql += " AND name = #{name} FOR UPDATE";
            return sql;
        }
    }
}
