package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.AgvArea;
import com.furongsoft.agv.entities.MaterialBox;
import com.furongsoft.agv.entities.Site;
import com.furongsoft.agv.entities.SiteDetail;
import com.furongsoft.agv.models.MaterialBoxModel;
import com.furongsoft.agv.models.SiteDetailModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * 库位详情表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface SiteDetailDao extends BaseMapper<SiteDetail> {

    /**
     * 通过ID获取站点详情信息
     *
     * @param id 站点详情ID
     * @return 站点详情信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectSiteDetailById")
    SiteDetailModel selectSiteDetailById(@Param("id") Long id);

    /**
     * 通过站点ID获取站点详情
     *
     * @param siteId 站点ID
     * @return 站点详情
     */
    @SelectProvider(type = DaoProvider.class, method = "selectSiteDetailBySiteId")
    SiteDetailModel selectSiteDetailBySiteId(@Param("siteId") Long siteId);

    /**
     * 通过区域编号查找空闲站点
     *
     * @param areaCode 区域编号
     * @return 空闲站点集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectIdleSiteByAreaCode")
    List<SiteDetailModel> selectIdleSiteByAreaCode(@Param("areaCode") String areaCode);

    /**
     * 通过站点ID和配送任务ID，绑定站点的当前配送任务
     *
     * @param siteId         站点ID
     * @param deliveryTaskId 配送任务ID
     */
    @UpdateProvider(type = DaoProvider.class, method = "addDeliveryTask")
    boolean addDeliveryTask(@Param("siteId") long siteId, @Param("deliveryTaskId") long deliveryTaskId);

    /**
     * 通过站点ID移除配送任务
     *
     * @param siteId 站点ID
     */
    @UpdateProvider(type = DaoProvider.class, method = "removeDeliveryTask")
    boolean removeDeliveryTask(@Param("siteId") long siteId);

    /**
     * 通过站点ID获取料框信息
     *
     * @param siteId 站点ID
     * @return 料框信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectMaterialBoxBySiteId")
    MaterialBoxModel selectMaterialBoxBySiteId(@Param("siteId") long siteId);

    /**
     * 通过二维码查询站点详情
     *
     * @param qrCode 二维码
     * @return 站点详情
     */
    @SelectProvider(type = DaoProvider.class, method = "selectSiteDetailModelByQrCode")
    SiteDetailModel selectSiteDetailModelByQrCode(@Param("qrCode") String qrCode);

    /**
     * 通过站点添加料框,并将站点设置为有货状态
     *
     * @param siteId        站点ID
     * @param materialBoxId 料框ID
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "addMaterialBoxBySiteId")
    boolean addMaterialBoxBySiteId(@Param("siteId") long siteId, @Param("materialBoxId") long materialBoxId);

    /**
     * 通过站点ID移除料框，并设置为空闲
     *
     * @param siteId 站点ID
     * @return 是否成功
     */
    @UpdateProvider(type = DaoProvider.class, method = "removeMaterialBox")
    boolean removeMaterialBox(@Param("siteId") long siteId);

    class DaoProvider {
        private static final String SITE_DETAIL_TABLE_NAME = SiteDetail.class.getAnnotation(TableName.class).value();
        private static final String AREA_SITE_TABLE_NAME = "t_agv_area_site";
        private static final String AGV_AREA_TABLE_NAME = AgvArea.class.getAnnotation(TableName.class).value();
        private static final String MATERIAL_BOX_TABLE_NAME = MaterialBox.class.getAnnotation(TableName.class).value();
        private static final String SITE_TABLE_NAME = Site.class.getAnnotation(TableName.class).value();

        /**
         * 通过波次ID获取波次信息
         *
         * @return sql
         */
        public String selectSiteDetailById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.site_id,t1.material_box_id,t1.state,t1.delivery_task_id,t1.enabled");
                    FROM(SITE_DETAIL_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 通过站点ID获取站点详情
         *
         * @return sql
         */
        public String selectSiteDetailBySiteId() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.site_id,t1.material_box_id,t1.state,t1.delivery_task_id,t1.enabled");
                    FROM(SITE_DETAIL_TABLE_NAME + " t1");
                    WHERE("t1.site_id = #{siteId}");
                }
            }.toString();
        }

        /**
         * 通过区域编号查找空闲站点
         *
         * @return sql
         */
        public String selectIdleSiteByAreaCode() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.site_id,t1.material_box_id,t1.state,t1.delivery_task_id,t1.enabled");
                    FROM(SITE_DETAIL_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(AREA_SITE_TABLE_NAME + " t2 ON t1.site_id = t2.site_id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t2.area_id = t3.id");
                    WHERE("t1.enabled = 1 AND t1.state = 0 AND t3.code = #{areaCode}");
                }
            }.toString();
        }

        /**
         * 通过站点ID和配送任务ID，绑定站点的当前配送任务
         *
         * @return sql
         */
        public String addDeliveryTask() {
            return new SQL() {
                {
                    UPDATE(SITE_DETAIL_TABLE_NAME);
                    SET("delivery_task_id=#{deliveryTaskId}");
                    WHERE("site_id=#{siteId}");
                }
            }.toString();
        }

        /**
         * 通过站点ID移除配送任务
         *
         * @return sql
         */
        public String removeDeliveryTask() {
            return new SQL() {
                {
                    UPDATE(SITE_DETAIL_TABLE_NAME);
                    SET("delivery_task_id=null");
                    WHERE("site_id=#{siteId}");
                }
            }.toString();
        }

        /**
         * 通过站点ID，查找站点上料框的信息
         *
         * @return sql
         */
        public String selectMaterialBoxBySiteId() {
            return new SQL() {
                {
                    SELECT("t2.id, t2.qr_code, t2.code, t2.name, t2.state, t2.enabled");
                    FROM(SITE_DETAIL_TABLE_NAME + " t1 ");
                    INNER_JOIN(MATERIAL_BOX_TABLE_NAME + " t2 ON t1.material_box_id = t2.id");
                    WHERE("t1.site_id=#{siteId}");
                }
            }.toString();
        }

        /**
         * 通过二维码查询站点详情
         *
         * @return sql
         */
        public String selectSiteDetailModelByQrCode() {
            return new SQL() {
                {
                    SELECT("t2.id, t2.site_id, t2.material_box_id, t2.delivery_task_id, t2.state, t2.enabled, t1.qr_code, t1.name," +
                            "t1.code,t1.type AS siteType");
                    FROM(SITE_TABLE_NAME + " t1 ");
                    INNER_JOIN(SITE_DETAIL_TABLE_NAME + " t2 ON t1.id = t2.site_id");
                    WHERE("t1.qr_code=#{qrCode} AND t1.enabled=1");
                }
            }.toString();
        }

        /**
         * 通过站点添加料框
         *
         * @return sql
         */
        public String addMaterialBoxBySiteId() {
            return new SQL() {
                {
                    UPDATE(SITE_DETAIL_TABLE_NAME);
                    SET("material_box_id=#{materialBoxId}", "state=2");
                    WHERE("site_id=#{siteId}");
                }
            }.toString();
        }

        /**
         * 通过站点ID移除料框，并设置为空闲
         *
         * @return sql
         */
        public String removeMaterialBox() {
            return new SQL() {
                {
                    UPDATE(SITE_DETAIL_TABLE_NAME);
                    SET("material_box_id=null", "state=0");
                    WHERE("site_id=#{siteId}");
                }
            }.toString();
        }
    }
}
