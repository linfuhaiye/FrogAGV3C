package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.AgvArea;
import com.furongsoft.agv.entities.MaterialBox;
import com.furongsoft.agv.entities.Site;
import com.furongsoft.agv.entities.SiteDetail;
import com.furongsoft.agv.models.SiteModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * 站点表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface SiteDao extends BaseMapper<Site> {

    /**
     * 通过站点ID获取站点信息
     *
     * @param id 站点ID
     * @return 站点信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectSiteById")
    SiteModel selectSiteById(@Param("id") Long id);

    /**
     * 通过区域类型获取站点详细信息
     *
     * @param type 区域类型
     * @return 站点详细信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectLocationByAreaType")
    List<SiteModel> selectLocationByAreaType(@Param("type") int type);

    /**
     * 通过区域ID查找库位
     *
     * @param areaId 区域ID
     * @return 库位集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectLocationsByAreaIdWithMaterialBox")
    List<SiteModel> selectLocationsByAreaIdWithMaterialBox(@Param("areaId") Long areaId);

    /**
     * 通过区域ID查找库位
     *
     * @param areaId 区域ID
     * @return 库位集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectLocationsByAreaId")
    List<SiteModel> selectLocationsByAreaId(@Param("areaId") Long areaId);

    /**
     * 通过编码查找站点信息
     *
     * @param code 站点编码
     * @return 站点信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectSiteModelByCode")
    SiteModel selectSiteModelByCode(String code);

    /**
     * 通过类型查找区域
     *
     * @param type 区域类型
     * @return 区域列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectAreaByType")
    List<AgvArea> selectAreaByType(@Param("type") int type);

    /**
     * 通过二维码查找站点信息
     *
     * @param qrCode 二维码
     * @return 站点信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectSiteModeByQrCode")
    SiteModel selectSiteModeByQrCode(@Param("qrCode") String qrCode);

    class DaoProvider {
        private static final String SITE_TABLE_NAME = Site.class.getAnnotation(TableName.class).value();
        private static final String AGV_AREA_TABLE_NAME = AgvArea.class.getAnnotation(TableName.class).value();
        private static final String SITE_DETAIL_TABLE_NAME = SiteDetail.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_BOX_TABLE_NAME = MaterialBox.class.getAnnotation(TableName.class).value();
        private static final String AGV_AREA_SITE_TABLE_NAME = "t_agv_area_site";

        /**
         * 通过站点ID获取站点信息
         *
         * @return sql
         */
        public String selectSiteById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.qr_code,t1.location_x,t1.location_y,t1.location_z,t1.type,t1.name,t1.code,t2.material_box_id,"
                            + "t2.state AS siteDetailState,t2.delivery_task_id, t3.area_id AS areaId");
                    FROM(SITE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(SITE_DETAIL_TABLE_NAME + " t2 ON t1.id = t2.site_id");
                    LEFT_OUTER_JOIN(AGV_AREA_SITE_TABLE_NAME + " t3 ON t3.site_id = t1.id ");
                    WHERE("t1.id = #{id} AND t2.enabled = 1");
                }
            }.toString();
        }

        /**
         * 通过区域类型获取站点详细信息
         *
         * @return sql
         */
        public String selectLocationByAreaType() {
            return new SQL() {
                {
                    SELECT("t3.id AS id,t1.name AS areaName, t1.id AS areaId, t1.code AS areaCode, t1.parent_id AS parentArea, t3.qr_code, t3.location_x, t3.location_y, "
                            + "t3.location_z, t3.type, t3.name, t3.code, t5.material_box_id, t5.state AS siteDetailState, t5.delivery_task_id");
                    FROM(AGV_AREA_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(AGV_AREA_SITE_TABLE_NAME + " t2 ON t1.id = t2.area_id");
                    LEFT_OUTER_JOIN(SITE_TABLE_NAME + " t3 ON t2.site_id = t3.id");
                    LEFT_OUTER_JOIN(SITE_DETAIL_TABLE_NAME + " t5 ON t5.site_id = t3.id");
                    WHERE("t1.parent_id in ((select t4.id from t_agv_area t4 where t4.type=#{type} AND t4.enabled=1)) AND t3.enabled=1 AND t1.enabled=1");
                }
            }.toString();
        }

        /**
         * 通过区域ID查找库位
         *
         * @return sql
         */
        public String selectLocationsByAreaIdWithMaterialBox() {
            return new SQL() {
                {
//                    SELECT("t1.id,t1.qr_code,t1.location_x,t1.location_y,t1.location_z,t1.type,t1.name,t1.code,t5.code AS materialBoxCode");
                    SELECT("t1.id,t1.qr_code,t1.location_x,t1.location_y,t1.location_z,t1.type,t1.name,t1.code");
                    FROM(SITE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(AGV_AREA_SITE_TABLE_NAME + " t2 ON t1.id = t2.site_id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t3.id = t2.area_id");
                    LEFT_OUTER_JOIN(SITE_DETAIL_TABLE_NAME + " t4 ON t4.site_id = t1.id");
//                    RIGHT_OUTER_JOIN(MATERIAL_BOX_TABLE_NAME + " t5 ON t5.id = t4.material_box_id");
                    WHERE("t1.enabled=1 AND t3.id=#{areaId}");
                }
            }.toString();
        }

        /**
         * 通过区域ID查找库位
         *
         * @return sql
         */
        public String selectLocationsByAreaId() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.qr_code,t1.location_x,t1.location_y,t1.location_z,t1.type,t1.name,t1.code");
                    FROM(SITE_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(AGV_AREA_SITE_TABLE_NAME + " t2 ON t1.id = t2.site_id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t3.id = t2.area_id");
                    LEFT_OUTER_JOIN(SITE_DETAIL_TABLE_NAME + " t4 ON t4.site_id = t1.id");
                    WHERE("t1.enabled=1 AND t3.id=#{areaId}");
                }
            }.toString();
        }

        public String selectSiteModelByCode() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.qr_code,t1.location_x,t1.location_y,t1.location_z,t1.type,t1.name,t1.code");
                    FROM(SITE_TABLE_NAME + " t1");
                    WHERE("");
                }
            }.toString();
        }

        /**
         * 通过类型查找区域
         *
         * @return sql
         */
        public String selectAreaByType() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.parent_id,t1.type,t1.name,t1.code");
                    FROM(AGV_AREA_TABLE_NAME + " t1");
                    WHERE("t1.enabled=1 AND t1.type=#{type}");
                }
            }.toString();
        }

        /**
         * 通过二维码找到站点信息
         *
         * @return sql
         */
        public String selectSiteModeByQrCode() {
            return new SQL() {
                {
                    SELECT("id, qr_code, location_x, location_y, location_z, name, type, code");
                    FROM(SITE_TABLE_NAME);
                    WHERE("qr_code=#{qrCode}");
                }
            }.toString();
        }
    }
}
