package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.rbac.entities.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

/**
 * 公司信息操作对象
 *
 * @author linyehai
 */
@Mapper
public interface CompanyDao extends BaseMapper<Company> {
    /**
     * 根据用户ID 获取公司信息
     *
     * @param userId 用户ID
     * @return 公司信息
     */
    @SelectProvider(type = CompanyDao.DaoProvider.class, method = "getCompanyByUserId")
    Company getCompanyByUserId(@Param("userId") String userId);

    class DaoProvider {
        private final String COMPANY = Company.class.getAnnotation(TableName.class).value();

        public String getCompanyByUserId() {
            return new SQL() {
                {
                    SELECT("*");
                    FROM(COMPANY);
                    WHERE("user_id = #{userId}");
                }
            }.toString();
        }

    }
}
