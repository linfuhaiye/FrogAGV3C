package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.Material;
import com.furongsoft.agv.entities.MaterialBoxMaterial;
import com.furongsoft.agv.models.MaterialBoxMaterialModel;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * 料框-物料表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface MaterialBoxMaterialDao extends BaseMapper<MaterialBoxMaterial> {

    /**
     * 通过料框-物料ID获取料框-物料信息
     *
     * @param id 料框-物料ID
     * @return 料框-物料信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectMaterialBoxMaterialById")
    MaterialBoxMaterialModel selectMaterialBoxMaterialById(@Param("id") Long id);

    /**
     * 通过料框ID获取料框-物料集合
     *
     * @param materialBoxId 料框ID
     * @return 料框-物料详细信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectMaterialBoxMaterialByMaterialBoxId")
    List<MaterialBoxMaterialModel> selectMaterialBoxMaterialByMaterialBoxId(@Param("materialBoxId") long materialBoxId);

    /**
     * 通过料框ID删除料框-物料
     *
     * @param materialBoxId 料框ID
     * @return 是否成功
     */
    @DeleteProvider(type = DaoProvider.class, method = "deleteMaterialBoxMaterialByMaterialId")
    boolean deleteMaterialBoxMaterialByMaterialId(@Param("materialBoxId") long materialBoxId);

//    /**
//     * 批量新增料框-原料 TODO
//     *
//     * @param materialModels 原料列表
//     * @return 是否成功
//     */
//    @InsertProvider(type = DaoProvider.class, method = "insertBatch")
//    boolean insertBatch(@Param("materialModels") List<MaterialModel> materialModels);

    class DaoProvider {
        private static final String MATERIAL_BOX_MATERIAL_TABLE_NAME = MaterialBoxMaterial.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_TABLE_NAME = Material.class.getAnnotation(TableName.class).value();

        /**
         * 通过料框-物料ID获取料框-物料信息
         *
         * @return sql
         */
        public String selectMaterialBoxMaterialById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.material_box_id,t1.material_id,t1.count,t1.state");
                    FROM(MATERIAL_BOX_MATERIAL_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 通过区域类型获取料框-物料详细信息
         *
         * @return sql
         */
        public String selectMaterialBoxMaterialByMaterialBoxId() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.material_box_id,t1.material_id,t1.count,t1.state,t2.code AS materialCode,t2.name AS materialName," +
                            "t2.uuid AS materialUUID,t2.specs AS materialSpecs,t2.unit AS materialUnit");
                    FROM(MATERIAL_BOX_MATERIAL_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    WHERE("t1.material_box_id = #{materialBoxId} AND t1.enabled=1");
                }
            }.toString();
        }

        /**
         * 通过料车ID删除料车-物料
         *
         * @return sql
         */
        public String deleteMaterialBoxMaterialByMaterialId() {
            return new SQL() {
                {
                    UPDATE(MATERIAL_BOX_MATERIAL_TABLE_NAME);
                    SET("enabled=0");
                    WHERE("material_box_id=#{materialBoxId}");
                }
            }.toString();
        }

    }
}
