package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.AgvArea;
import com.furongsoft.agv.entities.Material;
import com.furongsoft.agv.entities.Wave;
import com.furongsoft.agv.models.WaveModel;
import com.furongsoft.base.misc.StringUtils;
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
public interface WaveDao extends BaseMapper<Wave> {

    /**
     * 通过波次ID获取波次信息
     *
     * @param id 波次ID
     * @return 波次信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveById")
    WaveModel selectWaveById(@Param("id") Long id);

    /**
     * 通过波次编号获取波次详情
     *
     * @param code 波次编号
     * @return 波次详情
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveByCode")
    WaveModel selectWaveByCode(@Param("code") String code);

    /**
     * 查找波次信息
     *
     * @param type   类型[1：灌装区；2：包装区]
     * @param teamId 班组ID
     * @param state  状态[0：未配送；1：配送中；2：已完成]
     * @return 波次信息集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveModels")
    List<WaveModel> selectWaveModels(@Param("type") int type, @Param("teamId") String teamId, @Param("state") Integer state);

    /**
     * 通过日期查询波次信息
     *
     * @param type      类型[1：灌装区；2：包装区]
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 波次信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveModelsByDate")
    List<WaveModel> selectWaveModelsByDate(@Param("type") Integer type, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 通过时间，查找这个时间点之前未完成配送的波次
     *
     * @param type      波次类型
     * @param startDate 查询时间
     * @return 查询时间之前，未完成配送的波次列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectUnFinishedByDate")
    List<WaveModel> selectUnFinishedByDate(@Param("type") Integer type, @Param("startDate") String startDate);

    /**
     * 通过区域ID查找波次列表
     *
     * @param areaId 区域ID
     * @return 波次信息集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveModelsByAreaId")
    List<WaveModel> selectWaveModelsByAreaId(@Param("areaId") Long areaId, @Param("state") Integer state);

    /**
     * 通过ID集合删除波次
     *
     * @param ids id集合
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "deleteWavesByIds")
    int deleteWavesByIds(@Param("ids") List<Long> ids);

    /**
     * 通过编号删除波次信息
     *
     * @param code 波次编号
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "deleteWaveByCode")
    boolean deleteWaveByCode(@Param("code") String code);

    /**
     * 通过生产订单号获取波次集合
     *
     * @param productionOrderNo 生产订单号
     * @return 波次集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveModelByProductionOrderNo")
    List<WaveModel> selectWaveModelByProductionOrderNo(@Param("productionOrderNo") String productionOrderNo);

    /**
     * 查找昨天与今天新建的波次
     *
     * @param yesterday 昨天 yyyy-MM-dd 00:00:00
     * @param today     今天 yyyy-MM-dd 23:59:59
     * @return 波次列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectWaveModelFromYesterdayToToday")
    List<WaveModel> selectWaveModelFromYesterdayToToday(@Param("yesterday") String yesterday, @Param("today") String today);

    /**
     * 通过波次编号更新波次状态
     *
     * @param waveCode 波次编号
     * @param state    波次状态
     * @return 是否更新成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "updateStateByWaveCode")
    boolean updateStateByWaveCode(@Param("waveCode") String waveCode, @Param("state") int state);

    /**
     * 通过生产订单号（MO号）更新产线
     *
     * @param productOrderNo 生产订单号
     * @return 更新数量
     */
    @UpdateProvider(type = DaoProvider.class, method = "changeLine")
    int changeLine(@Param("productionOrderNo") String productOrderNo, @Param("areaId") long areaId, @Param("type") int type);

    class DaoProvider {
        private static final String WAVE_TABLE_NAME = Wave.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_TABLE_NAME = Material.class.getAnnotation(TableName.class).value();
        private static final String AGV_AREA_TABLE_NAME = AgvArea.class.getAnnotation(TableName.class).value();

        /**
         * 通过波次ID获取波次信息
         *
         * @return sql
         */
        public String selectWaveById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.team_id,t1.team_name,t1.area_id,t1.material_id,t1.execution_time,t1.finish_time,t1.state,t1.type,t1.production_order_no");
                    FROM(WAVE_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 通过编号获取波次详情
         *
         * @return sql
         */
        public String selectWaveByCode() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.team_id,t1.team_name,t1.area_id,t1.material_id,t1.execution_time,t1.finish_time,t1.state,t1.type," +
                            "t1.production_order_no,t2.name AS materialName,t2.uuid AS materialCode,t3.name AS productLineName,t3.code AS productLineCode");
                    FROM(WAVE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t1.area_id = t3.id");
                    WHERE("t1.code=#{code} AND t1.enabled=1");
                }
            }.toString();
        }

        /**
         * 查找波次信息
         *
         * @return sql
         */
        public String selectWaveModels(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.team_id,t1.team_name,t1.area_id,t1.material_id,t1.execution_time,t1.finish_time,t1.state,t1.type," +
                            "t1.production_order_no,t2.name AS materialName,t2.uuid AS materialCode,t3.name AS productLineName,t3.code AS productLineCode");
                    FROM(WAVE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t1.area_id = t3.id");
                    WHERE("t1.enabled=1 and t1.type = #{type} ");
                    if (!StringUtils.isNullOrEmpty(param.get("teamId"))) {
                        WHERE("t1.team_id = #{teamId}");
                    }
                    if (null != param.get("state") && (int) param.get("state") == 0) {
                        WHERE("t1.state <> 2");
                    } else if (null != param.get("state")) {
                        WHERE("t1.state = #{state}");
                    }
                }
            }.toString();
        }

        /**
         * 通过区域ID获取波次信息
         *
         * @return sql
         */
        public String selectWaveModelsByAreaId(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.team_id,t1.team_name,t1.area_id,t1.material_id,t1.execution_time,t1.finish_time,t1.state,t1.type," +
                            "t1.production_order_no,t2.name AS materialName,t2.uuid AS materialCode,t3.name AS productLineName,t3.code AS productLineCode");
                    FROM(WAVE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t1.area_id = t3.id");
                    WHERE("t1.enabled=1 and t1.area_id = #{areaId}");
                    if (null != param.get("state")) {
                        WHERE("t1.state=#{state}");
                    }
                }
            }.toString();
        }

        /**
         * 通过ID集合删除波次
         *
         * @param param 参数
         * @return sql
         */
        public String deleteWavesByIds(final Map<String, Object> param) {
            return new SQL() {
                {
                    UPDATE(WAVE_TABLE_NAME);
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
         * 根据编号删除波次
         *
         * @return sql
         */
        public String deleteWaveByCode() {
            return new SQL() {
                {
                    UPDATE(WAVE_TABLE_NAME);
                    SET("enabled=0");
                    WHERE("code=#{code}");
                }
            }.toString();
        }

        /**
         * 通过日期查询波次信息
         *
         * @param param 查询参数
         * @return sql
         */
        public String selectWaveModelsByDate(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.team_id,t1.team_name,t1.area_id,t1.material_id,t1.execution_time,t1.finish_time,t1.state,t1.type," +
                            "t1.production_order_no,t2.name AS materialName,t2.uuid AS materialCode,t3.name AS productLineName,t3.code AS productLineCode");
                    FROM(WAVE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t1.area_id = t3.id");
                    WHERE("t1.enabled=1 ");
                    if (!StringUtils.isNullOrEmpty(param.get("startDate"))) {
                        WHERE("t1.execution_time > #{startDate}");
                    }
                    if (!StringUtils.isNullOrEmpty(param.get("endDate"))) {
                        WHERE("t1.execution_time < #{endDate}");
                    }
                    if (null != param.get("type")) {
                        WHERE("t1.type=#{type}");
                    }
                }
            }.toString();
        }

        /**
         * 通过时间，查找这个时间点之前未完成配送的波次
         *
         * @param param 参数
         * @return sql
         */
        public String selectUnFinishedByDate(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.team_id,t1.team_name,t1.area_id,t1.material_id,t1.execution_time,t1.finish_time,t1.state,t1.type," +
                            "t1.production_order_no,t2.name AS materialName,t2.uuid AS materialCode,t3.name AS productLineName,t3.code AS productLineCode");
                    FROM(WAVE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t1.area_id = t3.id");
                    WHERE("t1.enabled=1 AND t1.state=0");
                    if (!StringUtils.isNullOrEmpty(param.get("startDate"))) {
                        WHERE("t1.execution_time < #{startDate}");
                    }
                    if (null != param.get("type")) {
                        WHERE("t1.type=#{type}");
                    }
                }
            }.toString();
        }

        /**
         * 通过生产订单号获取波次集合
         *
         * @return sql
         */
        public String selectWaveModelByProductionOrderNo() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.team_id,t1.team_name,t1.area_id,t1.material_id,t1.execution_time,t1.finish_time,t1.state,t1.type," +
                            "t1.production_order_no,t2.name AS materialName,t2.uuid AS materialCode,t3.name AS productLineName,t3.code AS productLineCode");
                    FROM(WAVE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t1.area_id = t3.id");
                    WHERE("t1.enabled=1 and t1.production_order_no = #{productionOrderNo} ");
                }
            }.toString();
        }

        /**
         * 查找昨天与今天新建的波次
         *
         * @return sql
         */
        public String selectWaveModelFromYesterdayToToday() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.code,t1.team_id,t1.team_name,t1.area_id,t1.material_id,t1.execution_time,t1.finish_time,t1.state,t1.type," +
                            "t1.production_order_no,t2.name AS materialName,t2.uuid AS materialCode,t3.name AS productLineName,t3.code AS productLineCode");
                    FROM(WAVE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(MATERIAL_TABLE_NAME + " t2 ON t1.material_id = t2.id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t1.area_id = t3.id");
                    WHERE("t1.enabled=1 and t1.create_time >= #{yesterday} and t1.create_time <= #{today}");
                }
            }.toString();
        }

        /**
         * 通过波次编号更新波次状态
         *
         * @return sql
         */
        public String updateStateByWaveCode() {
            return new SQL() {
                {
                    UPDATE(WAVE_TABLE_NAME);
                    SET("state=#{state}");
                    WHERE("code=#{waveCode}");
                }
            }.toString();
        }

        /**
         * 更换产线
         *
         * @return sql
         */
        public String changeLine() {
            return new SQL() {
                {
                    UPDATE(WAVE_TABLE_NAME);
                    SET("area_id=#{areaId}");
                    WHERE("enabled=1 AND state <> 2 AND production_order_no=#{productionOrderNo} AND type=#{type}");
                }
            }.toString();
        }
    }
}
