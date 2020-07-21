package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.MaterialBox;
import com.furongsoft.agv.models.MaterialBoxModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

/**
 * 料框表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface MaterialBoxDao extends BaseMapper<MaterialBox> {

    /**
     * 通过料框ID获取料框信息
     *
     * @param id 料框ID
     * @return 料框信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectMaterialBoxById")
    MaterialBoxModel selectMaterialBoxById(@Param("id") Long id);

    /**
     * 通过料框ID修改状态为指定值
     *
     * @param materialBoxId 料框ID
     * @param state         状态
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "updateMaterialBoxState")
    boolean updateMaterialBoxState(@Param("materialBoxId") long materialBoxId, @Param("state") int state);

    /**
     * 通过二维码查询料框信息
     *
     * @param qrCode 二维码
     * @return 料框信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectMaterialBoxModelByQrCode")
    MaterialBoxModel selectMaterialBoxModelByQrCode(@Param("qrCode") String qrCode);

    class DaoProvider {
        private static final String MATERIAL_BOX_TABLE_NAME = MaterialBox.class.getAnnotation(TableName.class).value();

        /**
         * 通过料框ID获取料框信息
         *
         * @return sql
         */
        public String selectMaterialBoxById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.qr_code,t1.code,t1.name,t1.state");
                    FROM(MATERIAL_BOX_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 根据料框ID修改为指定状态
         *
         * @return sql
         */
        public String updateMaterialBoxState() {
            return new SQL() {
                {
                    UPDATE(MATERIAL_BOX_TABLE_NAME);
                    SET("state=#{state}");
                    WHERE("id=#{materialBoxId}");
                }
            }.toString();
        }

        /**
         * 通过二维码查询料框信息
         *
         * @return sql
         */
        public String selectMaterialBoxModelByQrCode() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.qr_code,t1.code,t1.name,t1.state");
                    FROM(MATERIAL_BOX_TABLE_NAME + " t1");
                    WHERE("t1.qr_code = #{qrCode} AND t1.enabled=1");
                }
            }.toString();
        }
    }
}
