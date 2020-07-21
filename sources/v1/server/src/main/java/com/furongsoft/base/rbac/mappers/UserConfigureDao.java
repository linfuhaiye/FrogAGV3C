package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.rbac.entities.Dictionary;
import com.furongsoft.base.rbac.entities.User;
import com.furongsoft.base.rbac.entities.UserConfigure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;


/**
 * 用户个人配置操作对象
 *
 * @author linyehai
 */
@Mapper
@Component
public interface UserConfigureDao extends BaseMapper<UserConfigure> {
    /**
     * 通过用户ID获取个人配置信息
     *
     * @param userId 用户ID
     * @return 个人配置信息
     */
    @SelectProvider(type = DaoProvider.class, method = "getUserConfigureByUserId")
    com.furongsoft.base.rbac.models.UserConfigure getUserConfigureByUserId(@Param("userId") Long userId);

    class DaoProvider {
        private final static String USER_CONFIGURE = UserConfigure.class.getAnnotation(TableName.class).value();
        private final static String USER = User.class.getAnnotation(TableName.class).value();
        private final static String DICT = Dictionary.class.getAnnotation(TableName.class).value();

        /**
         * 通过用户ID获取个人配置信息
         *
         * @return sql
         */
        public String getUserConfigureByUserId() {
            return new SQL() {
                {
                    SELECT("t1.id, user_id, report_type, d1.name AS reportTypeName");
                    FROM(USER_CONFIGURE + " t1");
                    LEFT_OUTER_JOIN(DICT + " d1 ON t1.report_type = d1.id");
                    WHERE("t1.user_id = #{userId}");
                }
            }.toString();
        }
    }
}
