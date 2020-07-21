package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.*;
import com.furongsoft.agv.models.CallMaterialModel;
import com.furongsoft.base.misc.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 叫料表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface CallMaterialDao extends BaseMapper<CallMaterial> {
    /**
     * 通过ID获取叫料信息
     *
     * @param id 叫料ID
     * @return 叫料信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectCallMaterialById")
    CallMaterialModel selectCallMaterialById(@Param("id") Long id);

    /**
     * 通过ID获取未配送的叫料信息
     *
     * @param id 叫料ID
     * @return 叫料信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUnDeliveryCallMaterialById")
    CallMaterialModel selectUnDeliveryCallMaterialById(@Param("id") Long id);

    /**
     * 根据条件获取叫料列表（默认获取未完成的）
     *
     * @param type   叫料类型[1：灌装区；2：包装区；3：消毒间；4：拆包间]
     * @param state  状态[1：未配送；2：配送中；3：已完成；4：已取消]
     * @param teamId 班组唯一标识
     * @param areaId 区域ID（产线ID）
     * @return 叫料列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectCallMaterialsByConditions")
    List<CallMaterialModel> selectCallMaterialsByConditions(@Param("type") int type, @Param("state") Integer state, @Param("teamId") String teamId, @Param("areaId") Long areaId, @Param("siteId") Long siteId);

    /**
     * 通过波次详情编码以及区域类型获取叫料信息
     *
     * @param waveDetailCode 波次详情编码
     * @param areaType       叫料区域类型[1：灌装区；2：包装区；3：消毒间；4：拆包间]
     * @return 叫料信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectCallMaterialByWaveDetailCodeAndAreaType")
    CallMaterialModel selectCallMaterialByWaveDetailCodeAndAreaType(@Param("waveDetailCode") String waveDetailCode, @Param("areaType") int areaType);

    /**
     * 通过波次编码以及区域类型获取叫料列表
     *
     * @param waveCode 波次编码
     * @param areaType 叫料区域类型[1：灌装区；2：包装区；3：消毒间；4：拆包间]
     * @return 叫料列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectCallMaterialByWaveCodeAndAreaType")
    List<CallMaterialModel> selectCallMaterialByWaveCodeAndAreaType(@Param("waveCode") String waveCode, @Param("areaType") int areaType, @Param("state") Integer state);

    /**
     * 通过ID对叫料信息进行伪删除
     *
     * @param id 叫料ID
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "deleteCallMaterial")
    boolean deleteCallMaterial(@Param("id") long id);

    /**
     * 通过波次详情取消叫料
     *
     * @param waveDetailCode 波次详情编号
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "deleteCallMaterialByCode")
    boolean deleteCallMaterialByCode(@Param("waveDetailCode") String waveDetailCode);

    /**
     * 更新叫料状态
     *
     * @param id    叫料ID
     * @param state 状态
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "updateCallMaterialState")
    boolean updateCallMaterialState(@Param("id") long id, int state);

    /**
     * 通过波次编号查找未完成的叫料列表
     *
     * @param waveCode 波次编号
     * @return 未完成的叫料列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUnFinishCallsByWaveCode")
    List<CallMaterialModel> selectUnFinishCallsByWaveCode(@Param("waveCode") String waveCode);

    /**
     * 通过波次以及叫料类型更新叫料状态
     *
     * @param waveCode 波次
     * @param type     类型
     * @param state    状态
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "updateCallMaterialStateByWaveCode")
    boolean updateCallMaterialStateByWaveCode(@Param("waveCode") String waveCode, @Param("type") int type, @Param("state") int state);

    /**
     * 通过站点ID和状态查找叫料列表
     *
     * @param siteId 站点ID
     * @param state  状态
     * @return 叫料列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectCallMaterialBySiteAndState")
    List<CallMaterialModel> selectCallMaterialBySiteAndState(long siteId, int state);

    class DaoProvider {
        private static final String CALL_MATERIAL_TABLE_NAME = CallMaterial.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_TABLE_NAME = Material.class.getAnnotation(TableName.class).value();
        private static final String WAVE_TABLE_NAME = Wave.class.getAnnotation(TableName.class).value();
        private static final String WAVE_DETAIL_TABLE_NAME = WaveDetail.class.getAnnotation(TableName.class).value();
        private static final String AGV_AREA_TABLE_NAME = AgvArea.class.getAnnotation(TableName.class).value();

        /**
         * 通过ID获取叫料信息
         *
         * @return sql
         */
        public String selectCallMaterialById() {
            return new SQL() {
                {
                    SELECT("t1.material_id,t1.count,t1.acceptance_count,t1.state,t1.call_time,t1.wave_detail_code,t1.type,t1.cancel_reason,t1.area_id,t1.team_id,t1.site_id");
                    FROM(CALL_MATERIAL_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 通过ID获取未配送的叫料信息
         *
         * @return sql
         */
        public String selectUnDeliveryCallMaterialById() {
            return new SQL() {
                {
                    SELECT("t1.material_id,t1.count,t1.acceptance_count,t1.state,t1.call_time,t1.wave_detail_code,t1.type,t1.cancel_reason,t1.area_id,t1.team_id,t1.site_id");
                    FROM(CALL_MATERIAL_TABLE_NAME + " t1");
                    WHERE("t1.enabled=1 AND t1.id = #{id} AND t1.state=1");
                }
            }.toString();
        }

        /**
         * 通过类型获取叫料列表(默认获取到未完成的叫料列表)
         *
         * @return sql
         */
        public String selectCallMaterialsByConditions(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.material_id,t1.count,t1.acceptance_count,t1.state,t1.type,t1.call_time,t1.wave_detail_code,t1.cancel_reason,t1.area_id,t1.team_id, t1.site_id, " +
                            "t2.name AS materialName, t2.code AS materialCode, t2.uuid AS materialUuid, t2.specs AS materialSpecs, t2.unit AS materialUnit, " +
                            "t2.batch AS materialBatch, t4.code AS waveCode, t4.material_id AS productId, t5.name AS productName, t5.uuid AS productUuid, t6.code AS productLineCode");
                    FROM(CALL_MATERIAL_TABLE_NAME + " t1 ");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id ");
                    LEFT_OUTER_JOIN(WAVE_DETAIL_TABLE_NAME + " t3 ON t1.wave_detail_code = t3.code");
                    LEFT_OUTER_JOIN(WAVE_TABLE_NAME + " t4 ON t4.code = t3.wave_code");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t5 ON t4.material_id = t5.id ");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t6 ON t1.area_id = t6.id");
                    WHERE("t1.enabled = 1 and t1.type=#{type} and t3.enabled=1 and t4.enabled=1 and t4.state <> 2");
                    if (null != param.get("state") && (int) param.get("state") == 0) {
                        WHERE("t1.state <> 3 AND t1.state <>4");
                    } else if (null != param.get("state")) {
                        WHERE("t1.state = #{state}");
                    }
                    if (!StringUtils.isNullOrEmpty(param.get("teamId"))) {
                        WHERE("t1.team_id = #{teamId}");
                    }
                    if (null != param.get("areaId")) {
                        WHERE("t1.area_id = #{areaId}");
                    }
                    if (null != param.get("siteId")) {
                        WHERE("t1.site_id=#{siteId}");
                    }
                }
            }.toString();
        }

        /**
         * 通过ID对叫料信息进行伪删除
         *
         * @return sql
         */
        public String deleteCallMaterial() {
            return new SQL() {
                {
                    UPDATE(CALL_MATERIAL_TABLE_NAME);
                    SET("enabled=0");
                    WHERE("id=#{id}");
                }
            }.toString();
        }

        /**
         * 通过波次详情编号取消叫料
         *
         * @return sql
         */
        public String deleteCallMaterialByCode() {
            return new SQL() {
                {
                    UPDATE(CALL_MATERIAL_TABLE_NAME);
                    SET("enabled=0");
                    WHERE("wave_detail_code=#{waveDetailCode}");
                }
            }.toString();
        }

        /**
         * 通过波次详情编码以及区域类型获取叫料信息
         *
         * @return sql
         */
        public String selectCallMaterialByWaveDetailCodeAndAreaType() {
            return new SQL() {
                {
                    SELECT("t1.material_id,t1.count,t1.acceptance_count,t1.state,t1.call_time,t1.wave_detail_code,t1.type,t1.cancel_reason,t1.area_id,t1.team_id,t1.site_id");
                    FROM(CALL_MATERIAL_TABLE_NAME + " t1");
                    WHERE("t1.wave_detail_code = #{waveDetailCode} AND t1.type = #{areaType} AND t1.enabled = 1");
                }
            }.toString();
        }

        /**
         * 通过波次编码以及区域类型获取叫料列表  TODO sql优化。过滤已完成的波次
         *
         * @return sql
         */
        public String selectCallMaterialByWaveCodeAndAreaType(final Map<String, Object> params) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.material_id,t1.count,t1.acceptance_count,t1.state,t1.call_time,t1.wave_detail_code,t1.type,t1.cancel_reason, " +
                            "t3.code AS waveCode,t1.area_id,t1.team_id,t1.site_id,t4.code AS productLineCode");
                    FROM(CALL_MATERIAL_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(WAVE_DETAIL_TABLE_NAME + " t2 ON t1.wave_detail_code = t2.code");
                    LEFT_OUTER_JOIN(WAVE_TABLE_NAME + " t3 ON t2.wave_code = t3.code");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t4 ON t1.area_id = t4.id");
                    WHERE("t1.type = #{areaType} AND t1.enabled = 1 AND t2.enabled=1 AND t3.enabled=1 AND t3.state <> 2");
                    if (!StringUtils.isNullOrEmpty(params.get("waveCode"))) {
                        WHERE("t3.code= #{waveCode}");
                    }
                    if (null != params.get("state")) {
                        WHERE("t1.state=#{state}");
                    }
                }
            }.toString();
        }


        /**
         * 更新叫料状态
         *
         * @return sql
         */
        public String updateCallMaterialState() {
            return new SQL() {
                {
                    UPDATE(CALL_MATERIAL_TABLE_NAME);
                    SET("state=#{state}");
                    WHERE("id=#{id}");
                }
            }.toString();
        }

        /**
         * 通过波次查找未完成的叫料列表
         *
         * @return sql
         */
        public String selectUnFinishCallsByWaveCode() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.material_id,t1.count,t1.acceptance_count,t1.state,t1.call_time,t1.wave_detail_code,t1.type,t1.cancel_reason, " +
                            "t3.code AS waveCode,t1.area_id,t1.team_id,t1.site_id");
                    FROM(CALL_MATERIAL_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(WAVE_DETAIL_TABLE_NAME + " t2 ON t1.wave_detail_code = t2.code");
                    LEFT_OUTER_JOIN(WAVE_TABLE_NAME + " t3 ON t2.wave_code = t3.code");
                    WHERE("t3.code= #{waveCode} AND t1.enabled = 1 AND t2.enabled=1 AND t3.enabled=1 AND t1.state<>3");
                }
            }.toString();
        }

        /**
         * 通过波次以及叫料类型更新叫料状态
         *
         * @return sql
         */
        public String updateCallMaterialStateByWaveCode() {
            return new SQL() {
                {
                    UPDATE(CALL_MATERIAL_TABLE_NAME + " t1");
                    SET("t1.state=#{state}");
                    WHERE(
                            "t1.type=#{type} AND t1.wave_detail_code in (" + new SQL() {
                                {
                                    SELECT("t2.code");
                                    FROM(WAVE_DETAIL_TABLE_NAME + " t2");
                                    WHERE("t2.wave_code=#{waveCode} AND t2.enabled=1");
                                }
                            }.toString() + ")"
                    );
                }
            }.toString();
        }

        /**
         * 通过站点和状态查找叫料列表
         *
         * @return sql
         */
        public String selectCallMaterialBySiteAndState() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.material_id,t1.count,t1.acceptance_count,t1.state,t1.call_time,t1.wave_detail_code,t1.type,t1.cancel_reason, " +
                            "t1.area_id,t1.team_id,t1.site_id");
                    FROM(CALL_MATERIAL_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(WAVE_DETAIL_TABLE_NAME + " t2 ON t1.wave_detail_code = t2.code");
                    LEFT_OUTER_JOIN(WAVE_TABLE_NAME + " t3 ON t2.wave_code = t3.code");
                    WHERE("t1.enabled= 1 AND t1.site_id=#{siteId} AND t1.state=#{state} AND t2.enabled=1 AND t3.enabled = 1");
                }
            }.toString();
        }
    }
}
