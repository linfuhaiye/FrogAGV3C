package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.rbac.entities.DictionaryGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

/**
 * 业务字典组操作对象
 *
 * @author chenfuqian
 */
@Mapper
public interface DictionaryGroupDao extends BaseMapper<DictionaryGroup> {
    /**
     * 根据名称查询字典组信息
     *
     * @param name 编码
     * @return 字典组信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectDictByName")
    DictionaryGroup selectDictByName(@Param("name") String name);

    /**
     * 根据编码查询字典组信息
     *
     * @param code 编码
     * @return 字典组信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectDictByCode")
    DictionaryGroup selectDictByCode(@Param("code") String code);

    class DaoProvider {
        /**
         * 根据Code查询字典信息
         *
         * @return SQL语句
         */
        public String selectDictByCode() {
            return new SQL() {
                {
                    SELECT("id,name,code");
                    FROM("t_sys_dict_group");
                    WHERE("code = #{code}");
                }
            }.toString();
        }

        /**
         * 根据Name查询字典信息
         *
         * @return SQL语句
         */
        public String selectDictByName() {
            return new SQL() {
                {
                    SELECT("id,name,code");
                    FROM("t_sys_dict_group");
                    WHERE(" name = #{name}");
                }
            }.toString();
        }
    }
}
