package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.CallMaterial;
import com.furongsoft.agv.entities.Material;
import com.furongsoft.agv.entities.WaveDetail;
import com.furongsoft.agv.models.WaveDetailModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 物料表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface WaveDetailDao extends BaseMapper<WaveDetail> {

    /**
     * 通过波次ID获取波次信息
     *
     * @param id 波次ID
     * @return 波次信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveDetailById")
    WaveDetailModel selectWaveDetailById(@Param("id") Long id);

    /**
     * 通过ID删除波次详情
     *
     * @param ids 波次详情ID集合
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "deleteWaveDetailsByIds")
    boolean deleteWaveDetailsByIds(@Param("ids") List<Long> ids);

    /**
     * 根据波次编号删除波次详情
     *
     * @param waveCode 波次编号
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "deleteWaveDetailsByWaveCode")
    boolean deleteWaveDetailsByWaveCode(@Param("waveCode") String waveCode);

    /**
     * 通过波次详情ID删除波次详情
     *
     * @param id 波次详情ID
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "deleteWaveDetailById")
    boolean deleteWaveDetailById(@Param("id") Long id);

    /**
     * 通过波次编码获取波次详情信息
     *
     * @param waveCode 波次编码
     * @return 波次详情列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveDetails")
    List<WaveDetailModel> selectWaveDetails(@Param("waveCode") String waveCode);

    /**
     * 通过波次编码以及区域类型获取波次详情信息
     *
     * @param waveCode 波次编码
     * @param type     区域类型
     * @return 波次详情集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveDetailsByWaveCodeAndAreaType")
    List<WaveDetailModel> selectWaveDetailsByWaveCodeAndAreaType(@Param("waveCode") String waveCode, @Param("areaType") int type);

    /**
     * 通过波次编码以及区域ID获取波次详情信息
     *
     * @param waveCode 波次编码
     * @param areaId   区域ID
     * @return 波次详情集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveDetailsByWaveCodeAndAreaId")
    List<WaveDetailModel> selectWaveDetailsByWaveCodeAndAreaId(@Param("waveCode") String waveCode, @Param("areaId") long areaId);


    /**
     * 通过ID修改波次详情
     *
     * @param waveDetailModel 波次详情
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "updateWaveDetailById")
    boolean updateWaveDetailById(@Param("waveDetailModel") WaveDetailModel waveDetailModel);

    class DaoProvider {
        private static final String WAVE_DETAIL_TABLE_NAME = WaveDetail.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_TABLE_NAME = Material.class.getAnnotation(TableName.class).value();
        private static final String CALL_MATERIAL_TABLE_NAME = CallMaterial.class.getAnnotation(TableName.class).value();

        /**
         * 通过波次ID获取波次信息
         *
         * @return sql
         */
        public String selectWaveDetailById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.wave_code,t1.material_id,t1.count,t2.name AS materialName,t2.uuid AS materialCode");
                    FROM(WAVE_DETAIL_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 通过波次编码查询波次详情
         *
         * @return sql
         */
        public String selectWaveDetails() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.wave_code,t1.material_id,t1.count,t2.name AS materialName,t2.uuid AS materialCode");
                    FROM(WAVE_DETAIL_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    WHERE("t1.wave_code = #{waveCode} AND t1.enabled = 1");
                }
            }.toString();
        }

        /**
         * 通过波次编号以及区域类型查找波次详情
         *
         * @return sql
         */
        public String selectWaveDetailsByWaveCodeAndAreaType() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.wave_code,t1.material_id,t1.count,t2.name AS materialName,t2.uuid AS materialCode,t3.id AS callId, t3.state AS callState");
                    FROM(WAVE_DETAIL_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    LEFT_OUTER_JOIN(CALL_MATERIAL_TABLE_NAME + " t3 ON t1.code = t3.wave_detail_code");
                    WHERE("t1.wave_code = #{waveCode} AND t1.enabled = 1 AND t3.type=#{areaType} AND t3.enabled=1");
                }
            }.toString();
        }

        /**
         * 通过波次编号以及区域ID查找波次详情
         *
         * @return sql
         */
        public String selectWaveDetailsByWaveCodeAndAreaId() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.wave_code,t1.material_id,t1.count,t2.name AS materialName,t2.uuid AS materialCode,t3.id AS callId");
                    FROM(WAVE_DETAIL_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    LEFT_OUTER_JOIN(CALL_MATERIAL_TABLE_NAME + " t3 ON t1.code = t3.wave_detail_code");
                    WHERE("t1.wave_code = #{waveCode} AND t1.enabled = 1 AND t3.area_id=#{areaId} AND t3.enabled=1");
                }
            }.toString();
        }

        /**
         * 通过ID集合删除波次详情
         *
         * @param param 参数
         * @return sql
         */
        public String deleteWaveDetailsByIds(final Map<String, Object> param) {
            return new SQL() {
                {
                    UPDATE(WAVE_DETAIL_TABLE_NAME);
                    SET("enabled = 0");
                    StringBuffer sql = new StringBuffer();
                    List<Long> ids = (List<Long>) param.get("ids");
                    sql.append(" id in (");
                    sql.append(org.apache.commons.lang3.StringUtils.join(ids, ","));
                    sql.append(")");
                    WHERE(sql.toString());
                }
            }.toString();
        }

        /**
         * 通过波次编号删除波次详情
         *
         * @return sql
         */
        public String deleteWaveDetailsByWaveCode() {
            return new SQL() {
                {
                    UPDATE(WAVE_DETAIL_TABLE_NAME);
                    SET("enabled = 0");
                    WHERE("wave_code = #{waveCode} AND enabled = 1");
                }
            }.toString();
        }

        /**
         * 根据波次详情ID删除波次详情
         *
         * @return sql
         */
        public String deleteWaveDetailById() {
            return new SQL() {
                {
                    UPDATE(WAVE_DETAIL_TABLE_NAME);
                    SET("enabled = 0");
                    WHERE("id = #{id} AND enabled = 1");
                }
            }.toString();
        }

        /**
         * 根据ID更新波次详情
         *
         * @param param 修改参数
         * @return sql
         */
        public String updateWaveDetailById(final Map<String, Object> param) {
            return new SQL() {
                {
                    WaveDetailModel waveDetailModel = (WaveDetailModel) param.get("waveDetailModel");
                    UPDATE(WAVE_DETAIL_TABLE_NAME);
                    SET("count=" + waveDetailModel.getCount());
                    WHERE("id=" + waveDetailModel.getId());
                }
            }.toString();
        }
    }
}
