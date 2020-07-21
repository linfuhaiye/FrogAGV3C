package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.furongsoft.base.entities.SelectItem;
import com.furongsoft.base.file.entities.Attachment;
import com.furongsoft.base.misc.StringUtils;
import com.furongsoft.base.rbac.entities.Role;
import com.furongsoft.base.rbac.entities.User;
import com.furongsoft.base.rbac.models.UserInfo;
import org.apache.ibatis.annotations.*;
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
public interface UserDao extends BaseMapper<User> {

    /**
     * 通过id集合查找用户信息集合
     *
     * @param ids id集合
     * @return 用户信息集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUserListByIds")
    List<UserInfo> selectUserListByIds(@Param("ids") List<Serializable> ids);

    /**
     * 根据资源编码查询用户列表
     *
     * @param path 资源编码
     * @return 拥有该编码权限的用户
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUserByResourcePath")
    List<UserInfo> selectUserByResourcePath(@Param("path") String path);

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    @SelectProvider(type = DaoProvider.class, method = "findByUsername")
    User findByUsername(@Param("username") String username);

    /**
     * 获取所有用户
     *
     * @param page 分页信息
     * @param name 名字
     * @return 用户列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUserListWithParams")
    List<User> selectUserList(Pagination page, @Param("name") String name, @Param("userId") long userId);

    /**
     * 获取所有用户
     *
     * @param page 分页信息
     * @param name 名字
     * @return 用户列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectAllUserListWithParams")
    List<User> selectAllUserListWithParams(Pagination page, @Param("name") String name);

    /**
     * 根据索引获取用户
     *
     * @param id 索引
     * @return 资源
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUserById")
    User selectUser(@Param("id") Serializable id);

    /**
     * 检查用户名或用户编号是否存在
     *
     * @param username 用户名称
     * @param code     用户编码
     * @param id       用户ID
     * @return 用户信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectForUpdate")
    User selectUserInfo(@Param("username") String username, @Param("code") String code, @Param("id") Serializable id);

    @SelectProvider(type = DaoProvider.class, method = "selectUserByName")
    User selectUserByName(@Param("name") String name);

    /**
     * 手机号码是否存在
     *
     * @param mobile 手机号码
     * @return 用户信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectMobileForUpdate")
    User selectMobile(@Param("mobile") String mobile);

    /**
     * 获取权限相关资源
     *
     * @param id 用户ID
     * @return 选项列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUserRole")
    List<SelectItem> selectUserRole(@Param("id") Serializable id);

    @SelectProvider(type = DaoProvider.class, method = "selectDataAuths")
    List<SelectItem> selectDataAuths(@Param("id") Serializable id);

    @SelectProvider(type = DaoProvider.class, method = "selectUserDatas")
    List<SelectItem> selectUserDatas(@Param("id") Serializable id);

    /**
     * 根据权限ID 删除权限资源关系
     *
     * @param id 用户索引
     */
    @DeleteProvider(type = DaoProvider.class, method = "deleteUserRole")
    void deleteUserRole(@Param("id") Serializable id);

    @DeleteProvider(type = DaoProvider.class, method = "deleteUserAuth")
    void deleteUserAuth(@Param("id") Serializable id);

    /**
     * 修改密码
     *
     * @param id          用户ID
     * @param password    新密码
     * @param oldPassword 旧密码
     * @param salt        盐
     */
    @UpdateProvider(type = DaoProvider.class, method = "updateUserPassword")
    void updateUserPassword(@Param("id") Serializable id, @Param("password") String password, @Param("oldPassword") String oldPassword, @Param("salt") String salt);

    /**
     * 通过id集合查找用户姓名集合
     *
     * @param ids id集合
     * @return 用户姓名集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectNamesByIds")
    List<String> selectNamesByIds(@Param("ids") List<Long> ids);

    /**
     * 根据用户ID查询角色列表
     *
     * @param id 用户ID
     * @return 角色列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUserRoleByUserId")
    List<Role> selectUserRoleByUserId(@Param("id") Serializable id);

    @UpdateProvider(type = DaoProvider.class, method = "updateOpenIdById")
    void updateOpenIdById(@Param("id") Serializable id, @Param("openid") String openid);

    /**
     * 更新vip
     *
     * @param userID
     */
    @SelectProvider(type = DaoProvider.class, method = "updateVIP")
    void updateVIP(@Param("id") long userID, @Param("vipType") int type, @Param("monthNum") int monthNum);

    /**
     * 更新头像
     *
     * @param userId 用户ID
     * @param url    头像地址
     */
    @SelectProvider(type = DaoProvider.class, method = "updateHeader")
    void updateHeader(@Param("userId") long userId, @Param("url") String url);

    @SelectProvider(type = DaoProvider.class, method = "getUserByOpenid")
    User getUserByOpenid(@Param("openid") String openid);

    @SelectProvider(type = DaoProvider.class, method = "getUserByMobile")
    User getUserByMobile(@Param("mobile") String mobile);

    /**
     * 更新vip
     *
     * @param userID
     */
    @SelectProvider(type = DaoProvider.class, method = "renewVIP")
    void renewVIP(@Param("id") long userID, @Param("renewType") int type, @Param("monthNum") int monthNum, @Param("discount") int discount);

    class DaoProvider {
        private final static String USER_TABLE_NAME = User.class.getAnnotation(TableName.class).value();
        private final static String ROLE_TABLE_NAME = Role.class.getAnnotation(TableName.class).value();
        private final static String USER_ROLE_TABLE_NAME = "t_sys_user_role";
        private final static String USER = User.class.getAnnotation(TableName.class).value();
        private final static String ATTACHMENT_TABLE_NAME = Attachment.class.getAnnotation(TableName.class).value();


        public String getUserByOpenid() {
            return new SQL() {
                {
                    SELECT("t1.*");
                    FROM("t_sys_user t1");
                    WHERE("openid = #{openid} ");
                    ORDER_BY("id LIMIT 1");
                }
            }.toString();
        }

        public String getUserByMobile() {
            return new SQL() {
                {
                    SELECT("t1.*");
                    FROM("t_sys_user t1");
                    WHERE("mobile = #{mobile}");
                    ORDER_BY("id LIMIT 1");
                }
            }.toString();
        }

        /**
         * 根据用户名称查找用户
         *
         * @return SQL语句
         */
        public String findByUsername() {
            return new SQL() {
                {
                    SELECT("su.id,su.username,su.code,su.password,su.salt,CONCAT(sa.url,sa.name) AS pictureUrl,su.city,su.name,su.icon_url,su.is_enable,su.expire_date,real_name");
                    FROM(USER_TABLE_NAME + " su");
                    LEFT_OUTER_JOIN(ATTACHMENT_TABLE_NAME + " sa ON su.picture_url = sa.id");
                    WHERE("username = #{username}");
                }
            }.toString();
        }

        public String updateOpenIdById() {
            return new SQL() {
                {
                    UPDATE(USER_TABLE_NAME);
                    SET("openid = #{openid}");
                    WHERE("id = #{id}");
                }
            }.toString();
        }

        /**
         * 获取所有用户
         *
         * @param param 参数列表
         * @return SQL语句
         */
        public String selectUserListWithParams(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.name,t1.code,t1.username,t1.mobile,t1.birthday,t1.email,t1.department,t1.sex,t1.remark,t1.icon_url,t1.province,t1.city,t1.town");
                    FROM(USER_TABLE_NAME + " t1,t_sys_data_auth t2 ");
                    WHERE("t1.state = 0 and t1.enable = 1");
                    WHERE("t2.user_id = #{userId} AND t2.target_user_id = t1.id");
                    if (!StringUtils.isNullOrEmpty(param.get("name"))) {
                        WHERE("t1.name LIKE CONCAT('%', #{name},'%')");
                    }
                }
            }.toString();
        }

        public String selectAllUserListWithParams(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.name,t1.code,t1.username,t1.mobile,t1.birthday,t1.email,t1.department,t1.sex,t1.remark,t1.icon_url,t1.province,t1.city,t1.town,real_name");
                    FROM(USER_TABLE_NAME + " t1");
                    WHERE("t1.state = 0 and t1.enable = 1");
                    if (!StringUtils.isNullOrEmpty(param.get("name"))) {
                        WHERE("t1.name LIKE CONCAT('%', #{name},'%')");
                    }
                    ORDER_BY("create_time DESC");
                }
            }.toString();
        }

        /**
         * 根据用户ID查询用户角色
         *
         * @return 用户角色信息
         */
        public String selectUserRoleByUserId() {
            return "SELECT t3.id,t3.code,t3.name FROM t_sys_user t1,t_sys_user_role t2,t_sys_role t3 WHERE t1.id = t2.user_id AND t2.role_id = t3.id AND t3.state = 0 AND t1.id = #{id}";

        }

        /**
         * 根据索引获取用户信息
         *
         * @param param 参数列表
         * @return SQL语句
         */
        public String selectUserById(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("id, create_time, create_user, last_modified_time, last_modified_user, address, address2," +
                            "        age, birthday, business_address, business_address2, city, company, company2, company3," +
                            "        country, email, email2, email3, icon_url AS iconUrl, identification, identification2, identification3," +
                            "        mobile, mobile2, name,real_name, password, picture_url, province, remark, salt, sex, sns_account," +
                            "        sns_account2, sns_account3, state, street, telephone,department, telephone2, title, title2," +
                            "        title3, town, username, web, web2, web3, zip,code,home,layout,alipay_no,vip,expire_date,wechat_no,real_name,vip_level");
                    FROM(USER_TABLE_NAME);
                    WHERE("state = 0");
                    if (param.get("id") != null) {
                        WHERE("id = #{id}");
                    }
                }
            }.toString();
        }

        /**
         * 检查用户名是否存在
         *
         * @param param 参数列表
         * @return SQL语句
         */
        public String selectForUpdate(final Map<String, Object> param) {
            String sql = "SELECT id,username,password,recommender,name,real_name FROM " + USER_TABLE_NAME + " WHERE state=0";
            if (param.get("id") != null) {
                sql += " AND id != #{id}";
            }
            if (param.get("username") != null) {
                sql += " AND username = #{username} ";
            }
            return sql;
        }

        public String selectUserByName() {
            return "SELECT id,name,username,password,real_name FROM t_sys_user where state=0 and name = #{name}";
        }

        /**
         * 检查手机号是否存在
         *
         * @param param 参数列表
         * @return SQL语句
         */
        public String selectMobileForUpdate(final Map<String, Object> param) {
            String sql = "SELECT id,name,username,password,recommender,real_name FROM " + USER_TABLE_NAME + " WHERE state=0";
            if (param.get("mobile") != null) {
                sql += " AND mobile = #{mobile} ";
            }
            return sql;
        }

        /**
         * 获取用户角色相关资源
         *
         * @return SQL语句
         */
        public String selectUserRole() {
            return new SQL() {
                {
                    SELECT("sr.name, sr.id AS `value`,CASE WHEN sar.role_id IS NULL THEN FALSE ELSE TRUE END AS selected");
                    FROM(ROLE_TABLE_NAME + " sr");
                    LEFT_OUTER_JOIN(USER_ROLE_TABLE_NAME + " sar ON sr.id = sar.role_id AND sar.user_id = #{id}");
                    WHERE("sr.state = 0 AND sr.enable = 1");
                }
            }.toString();
        }

        public String selectDataAuths() {
            return new SQL() {
                {
                    SELECT("IFNULL(sr.real_name,sr.name) AS NAME, sr.id AS `value`,CASE WHEN sar.target_user_id IS NULL THEN FALSE ELSE TRUE END AS selected");
                    FROM(" t_sys_user sr LEFT JOIN t_sys_data_auth sar ON sr.id = sar.target_user_id AND sar.user_id = #{id}");
                }
            }.toString();
        }

        public String selectUserDatas() {
            return new SQL() {
                {
                    SELECT("t1.name,t1.id AS `value`,CASE WHEN t2.target_user_id IS NULL THEN FALSE ELSE TRUE END AS selected");
                    FROM(" t_sys_user t1");
                    LEFT_OUTER_JOIN(" t_sys_data_auth t2 ON t1.id = t2.target_user_id AND t2.user_id =  #{id}");
                    WHERE(" t1.state = 0 AND t1.enable = 1");
                }
            }.toString();
        }

        /**
         * 根据用户ID 删除用户角色关系
         *
         * @return SQL语句
         */
        public String deleteUserRole() {
            return new SQL() {
                {
                    DELETE_FROM(USER_ROLE_TABLE_NAME);
                    WHERE("user_id = #{id}");
                }
            }.toString();
        }

        public String deleteUserAuth() {
            return new SQL() {
                {
                    DELETE_FROM("t_sys_data_auth");
                    WHERE("user_id = #{id}");
                }
            }.toString();
        }

        /**
         * 修改用户密码
         *
         * @return SQL语句
         */
        public String updateUserPassword() {
            return new SQL() {
                {
                    UPDATE(USER);
                    SET("password = #{password},salt = #{salt}");
                    WHERE("id = #{id} AND password = #{oldPassword}");
                }
            }.toString();
        }

        /**
         * 通过id集合获取用户信息集合
         *
         * @return sql
         */
        public String selectUsersByIds(final Map<String, Object> param) {
            StringBuffer sql = new StringBuffer();
            List<Long> ids = (List<Long>) param.get("ids");
            sql.append("SELECT id, name, email FROM ");
            sql.append(USER);
            sql.append(" WHERE state = 0 AND id in (");
            if (ids != null) {
                sql.append(org.apache.commons.lang3.StringUtils.join(ids, ","));
            } else {
                sql.append("0");
            }
            sql.append(")");
            return sql.toString();
        }

        /**
         * 通过id集合获取用户姓名集合
         *
         * @return sql
         */
        public String selectNamesByIds(final Map<String, Object> param) {
            StringBuffer sql = new StringBuffer();
            List<Long> ids = (List<Long>) param.get("ids");
            sql.append("SELECT name FROM ");
            sql.append(USER);
            sql.append(" WHERE state = 0 AND id in (");
            if (ids != null) {
                sql.append(org.apache.commons.lang3.StringUtils.join(ids, ","));
                sql.append(")");
            }
            return sql.toString();
        }

        /**
         * 根据资源编码查询用户列表
         *
         * @return SQL语句
         */
        public String selectUserByResourcePath() {
            return new SQL() {
                {
                    SELECT("t5.id,t5.email,t5.name,t5.username,t6.name AS deparmentName,t6.code AS deparmentCode,t5.code");
                    FROM("t_sys_resource t1,t_sys_permission_resource t2,t_sys_role_permission t3,t_sys_user_role t4,t_sys_user t5,t_sys_organization t6");
                    WHERE("t1.id = t2.resource_id AND t2.permission_id = t3.permission_id AND t3.role_id = t4.role_id");
                    WHERE("t5.id = t4.user_id AND t6.id = t5.department AND t1.path = #{path}");
                }
            }.toString();
        }

        /**
         * 根据索引查询用户信息
         *
         * @param param 参数信息
         * @return SQL语句
         */
        public String selectUserListByIds(final Map<String, Object> param) {
            StringBuffer sql = new StringBuffer();
            List<Serializable> ids = (List<Serializable>) param.get("ids");
            sql.append("SELECT t1.id, t1.name, t1.username, t1.code, t1.email, t2.name AS deparmentName,t2.code AS deparmentCode FROM ");
            sql.append(USER + " t1 ");
            sql.append("LEFT JOIN t_sys_organization t2 ON t1.department = t2.id ");
            sql.append(" WHERE t1.state = 0 AND t1.id in (");
            if (ids != null) {
                sql.append(org.apache.commons.lang3.StringUtils.join(ids, ","));
                sql.append(")");
            }
            return sql.toString();
        }

        /**
         * 更新vip
         *
         * @return
         */
        public String updateVIP(final Map<String, Object> param) {
            return new SQL() {
                {
                    UPDATE(USER_TABLE_NAME);
                    SET("vip = 1", "vip_level = #{vipType}");
                    if (!"0".equals(param.get("monthNum").toString())) {
                        SET("expire_date = DATE_ADD(now(),INTERVAL #{monthNum} MONTH)");
                    }
                    WHERE("id = #{id}");
                }
            }.toString();
        }

        /**
         * 更新头像
         *
         * @return
         */
        public String updateHeader() {
            return new SQL() {
                {
                    UPDATE(USER_TABLE_NAME);
                    SET("icon_url = #{url}");
                    WHERE("id = #{userId}");
                }
            }.toString();
        }

        /**
         * 更新vip
         *
         * @return
         */
        public String renewVIP(final Map<String, Object> param) {
            return new SQL() {
                {
                    UPDATE(USER_TABLE_NAME);
                    if ("1".equals(param.get("renewType").toString())) {
                        SET("expire_date = DATE_ADD(expire_date,INTERVAL #{monthNum} MONTH)");
                    } else {
                        SET("expire_date = DATE_ADD(now(),INTERVAL #{monthNum} MONTH)");
                    }
                    if ("1".equals(param.get("discount").toString())) {
                        SET("discount = #{discount}");
                        WHERE("discount = 0");
                    }
                    WHERE("id = #{id}");

                }
            }.toString();
        }
    }
}
