package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.furongsoft.base.rbac.entities.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 业务字典操作对象
 *
 * @author chenfuqian
 */
@Mapper
public interface DictionaryDao extends BaseMapper<Dictionary> {
    /**
     * 查询业务字典
     *
     * @param page 分页信息
     * @param name 字典名称
     * @return 字典列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectDicts")
    List<Dictionary> selectDicts(Pagination page, @Param("name") String name, @Param("groupId") Serializable groupId);

    /**
     * 根据字典组别代码查询字典信息
     *
     * @param code 字典组别代码
     * @return 字典列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectDictsByCode")
    List<Dictionary> selectDictsByCode(@Param("code") String code);

    /**
     * 根据组别ID\code查询字典信息
     *
     * @param dictGroupId 组别ID
     * @param code        字典编码
     * @return 字典
     */
    @SelectProvider(type = DaoProvider.class, method = "selectDictByCodeGroupId")
    Dictionary selectDictByCodeGroupId(@Param("dictGroupId") Long dictGroupId, @Param("code") String code);

    /**
     * 根据组别ID\Name查询字典信息
     *
     * @param dictGroupId 组别ID
     * @param name        字典名
     * @return 字典
     */
    @SelectProvider(type = DaoProvider.class, method = "selectDictByNameGroupId")
    Dictionary selectDictByNameGroupId(@Param("dictGroupId") Long dictGroupId, @Param("name") String name);

    /**
     * 查询多个字典组代码下数据
     *
     * @param codes
     * @return
     */
    @SelectProvider(type = DaoProvider.class, method = "selectDictByGroupCodes")
    List<Dictionary> selectDictByGroupCodes(@Param("codes") List<Integer> codes);

    class DaoProvider {

        /**
         * 查询业务字典
         *
         * @param param 查询参数
         * @return SQL语句
         */
        public String selectDicts(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.dict_group_id,t2.name AS group_name,t1.name,t1.system,t1.sort,t1.remark,t1.code");
                    FROM("t_sys_dict t1,t_sys_dict_group t2");
                    WHERE("t1.dict_group_id = t2.id");
                    if (!StringUtils.isEmpty(param.get("name"))) {
                        WHERE("t1.name LIKE CONCAT('%', #{name},'%')");
                    }
                    if (param.get("groupId") != null) {
                        WHERE("t2.id = #{groupId}");
                    }
                }
            }.toString();
        }

        /**
         * 查询业务字典组下数据
         *
         * @return SQL词语
         */
        public String selectDictsByCode() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.name,t1.code");
                    FROM("t_sys_dict t1,t_sys_dict_group t2");
                    WHERE("t1.dict_group_id = t2.id AND t2.code = #{code}");
                    ORDER_BY("t1.sort asc");
                }
            }.toString();
        }

        /**
         * 根据组别ID\Code查询字典信息
         *
         * @return SQL语句
         */
        public String selectDictByCodeGroupId(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("id,name,code");
                    FROM("t_sys_dict");
                    WHERE("code = #{code}");
                    if (!StringUtils.isEmpty(param.get("dictGroupId"))) {
                        WHERE("dict_group_id = #{dictGroupId}");
                    }
                }
            }.toString();
        }

        /**
         * 根据组别ID\Name查询字典信息
         *
         * @return SQL语句
         */
        public String selectDictByNameGroupId(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("id,name,code");
                    FROM("t_sys_dict");
                    WHERE(" name = #{name}");
                    if (!StringUtils.isEmpty(param.get("dictGroupId"))) {
                        WHERE("dict_group_id = #{dictGroupId}");
                    }
                }
            }.toString();
        }

        public String selectDictByGroupCodes(final Map<String, Object> param) {
            return new SQL() {
                {
                    List<Integer> codes = (List<Integer>) param.get("codes");
                    SELECT("name,code,dict_group_code");
                    FROM("t_sys_dict");
                    WHERE("dict_group_id IN (" + org.apache.commons.lang3.StringUtils.join(codes, ",") + ")");
                }
            }.toString();
        }
    }

}
