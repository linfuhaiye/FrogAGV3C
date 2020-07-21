package com.furongsoft.agv.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.entities.AgvArea;
import com.furongsoft.agv.entities.Site;
import com.furongsoft.agv.entities.SiteDetail;
import com.furongsoft.agv.models.AgvAreaModel;
import com.furongsoft.agv.models.SiteDetailModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 区域表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface AgvAreaDao extends BaseMapper<AgvArea> {
    /**
     * 通过ID获取区域信息
     *
     * @param id 主键
     * @return 区域信息
     */
    @SelectProvider(type = AgvAreaDao.DaoProvider.class, method = "selectAgvAreaById")
    AgvAreaModel selectAgvAreaById(@Param("id") Long id);

    /**
     * 通过类型获取区域信息
     *
     * @param code 编码
     * @return 区域信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectAgvAreaByCodeAndParent")
    AgvAreaModel selectAgvAreaByCodeAndParent(@Param("code") String code, @Param("parentId") long parentId);

    /**
     * 通过区域编号获取站点详情信息
     *
     * @param areaCode  区域编号
     * @param siteState 站点状态
     * @return 站点详情信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectSiteDetailsByAreaCode")
    List<SiteDetailModel> selectSiteDetailsByAreaCode(@Param("areaCode") String areaCode, @Param("siteState") Integer siteState);

    /**
     * 通过父级区域获取站点集合
     *
     * @param parentId 父级区域ID
     * @param type     站点类型
     * @return 站点集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectAreaByParentId")
    List<AgvAreaModel> selectAreaByParentId(@Param("parentId") long parentId, @Param("type") Integer type);

    /**
     * 通过ID查找父级区域信息
     *
     * @param id 区域ID
     * @return 父级区域信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectParentAreaById")
    AgvAreaModel selectParentAreaById(@Param("id") Long id);

    /**
     * 通过编号查找区域
     *
     * @param areaCode 区域编号
     * @return 区域
     */
    @SelectProvider(type = DaoProvider.class, method = "selectAgvAreaByCode")
    AgvArea selectAgvAreaByCode(@Param("areaCode") String areaCode);

    /**
     * 通过站点ID获取区域信息
     *
     * @param siteId 站点ID
     * @return 区域信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectAgvAreaBySiteId")
    AgvArea selectAgvAreaBySiteId(@Param("siteId") long siteId);

    class DaoProvider {
        private static final String AGV_AREA_TABLE_NAME = AgvArea.class.getAnnotation(TableName.class).value();
        private static final String SITE_DETAIL_TABLE_NAME = SiteDetail.class.getAnnotation(TableName.class).value();
        private static final String SITE_TABLE_NAME = Site.class.getAnnotation(TableName.class).value();
        private static final String AREA_SITE_TABLE_NAME = "t_agv_area_site";

        /**
         * 通过ID获取区域信息
         *
         * @return sql
         */
        public String selectAgvAreaById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.parent_id,t1.type,t1.code,t1.name");
                    FROM(AGV_AREA_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 通过编码和父级获取区域信息
         *
         * @return sql
         */
        public String selectAgvAreaByCodeAndParent(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.parent_id,t1.type,t1.code,t1.name");
                    FROM(AGV_AREA_TABLE_NAME + " t1");
                    WHERE("t1.code = #{code} AND t1.parent_id = #{parentId}");
                }
            }.toString();
        }

        /**
         * 通过区域编码查找站点详情
         *
         * @param param
         * @return
         */
        public String selectSiteDetailsByAreaCode(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t3.id,t3.site_id,t3.material_box_id,t3.delivery_task_id,t3.state,t1.id,t1.parent_id AS parentAreaId,t1.type AS areaType,t1.code AS areaCode,t1.name AS areaName");
                    FROM(AGV_AREA_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(AREA_SITE_TABLE_NAME + " t2 ON t1.id = t2.area_id");
                    LEFT_OUTER_JOIN(SITE_DETAIL_TABLE_NAME + " t3 ON t3.site_id=t2.site_id");
                    WHERE("t1.code = #{code} AND t3.enabled=1");
                    if (null != param.get("siteState")) {
                        WHERE("t3.state=#{siteState}");
                    }
                }
            }.toString();
        }

        /**
         * 通过父级区域获取站点集合
         *
         * @param param 查询参数
         * @return sql
         */
        public String selectAreaByParentId(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id, t1.parent_id, t1.type, t1.name, t1.code");
                    FROM(AGV_AREA_TABLE_NAME + " t1");
                    WHERE("t1.parent_id = #{parentId} AND t1.enabled=1");
                    if (null != param.get("type")) {
                        WHERE("t1.type = #{type}");
                    }
                }
            }.toString();
        }

        /**
         * 通过ID查找父级区域
         *
         * @return sql
         */
        public String selectParentAreaById() {
            return new SQL() {
                {
                    SELECT("t2.id,t2.parent_id,t2.type,t2.code,t2.name");
                    FROM(AGV_AREA_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t2 ON t1.parent_id = t2.id");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 通过编号查找区域
         *
         * @return sql
         */
        public String selectAgvAreaByCode() {
            return new SQL() {
                {
                    SELECT("t1.id, t1.parent_id, t1.type, t1.name, t1.code");
                    FROM(AGV_AREA_TABLE_NAME + " t1");
                    WHERE("t1.code = #{areaCode} AND t1.enabled=1");
                }
            }.toString();
        }

        /**
         * 通过站点ID获取区域信息
         *
         * @return sql
         */
        public String selectAgvAreaBySiteId() {
            return new SQL() {
                {
                    SELECT("t1.id, t1.parent_id, t1.type, t1.name, t1.code");
                    FROM(AGV_AREA_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(AREA_SITE_TABLE_NAME + " t2 ON t2.area_id = t1.id");
                    LEFT_OUTER_JOIN(SITE_TABLE_NAME + " t3 ON t2.site_id = t3.id");
                    WHERE("t3.id=#{siteId} AND t3.enabled=1 AND t1.enabled=1");
                }
            }.toString();
        }

    }
}
