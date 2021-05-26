package com.furongsoft.agv.frog.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.Material;
import com.furongsoft.agv.frog.entities.Bom;
import com.furongsoft.agv.frog.entities.BomDetail;
import com.furongsoft.agv.frog.models.BomModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Set;

/**
 * BOM表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface BomDao extends BaseMapper<Bom> {

    /**
     * 通过BOM ID获取BOM信息
     *
     * @param id BOM ID
     * @return BOM信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectBomById")
    BomModel selectBomById(@Param("id") Long id);

    /**
     * 通过BOM编号获取BOM详情
     *
     * @param materialCode 物料编号
     * @return BOM详情
     */
    @SelectProvider(type = DaoProvider.class, method = "selectBomByMaterialUuid")
    BomModel selectBomByMaterialUuid(@Param("materialCode") String materialCode);

    /**
     * 查找所有未删除的BOM的物料编号
     *
     * @return BOM的物料编号集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectMaterialCodes")
    Set<String> selectMaterialCodes();

    /**
     * 通过是否更新的状态查找BOM列表
     *
     * @param updateState 是否更新的状态
     * @return BOM列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectBomByUpdateState")
    List<BomModel> selectBomByUpdateState(@Param("updateState") int updateState);

    class DaoProvider {
        private static final String BOM_TABLE_NAME = Bom.class.getAnnotation(TableName.class).value();
        private static final String BOM_DETAIL_TABLE_NAME = BomDetail.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_TABLE_NAME = Material.class.getAnnotation(TableName.class).value();

        /**
         * 通过BOMID获取BOM信息
         *
         * @return sql
         */
        public String selectBomById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.material_code,t1.full_count,t1.version,t1.update_state");
                    FROM(BOM_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 通过物料编号获取BOM信息
         *
         * @return sql
         */
        public String selectBomByMaterialUuid() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.material_code,t1.full_count,t1.version,t1.update_state, t2.name AS materialName");
                    FROM(BOM_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_code = t2.uuid");
                    WHERE("t1.material_code=#{materialCode} AND t1.enabled=1");
                }
            }.toString();
        }

        /**
         * 查找物料编号集合
         *
         * @return sql
         */
        public String selectMaterialCodes() {
            return new SQL() {
                {
                    SELECT("material_code");
                    FROM(BOM_TABLE_NAME);
                    WHERE("enabled=1");
                }
            }.toString();
        }

        /**
         * 通过是否更新的状态查找BOM列表
         *
         * @return sql
         */
        public String selectBomByUpdateState() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.material_code,t1.full_count,t1.version,t1.update_state, t2.name AS materialName");
                    FROM(BOM_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_code = t2.uuid");
                    WHERE("t1.enabled=1 AND t1.update_state=#{updateState}");
                }
            }.toString();
        }
    }
}
