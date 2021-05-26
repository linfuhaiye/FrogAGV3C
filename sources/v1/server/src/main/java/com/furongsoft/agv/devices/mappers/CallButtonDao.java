package com.furongsoft.agv.devices.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.agv.devices.entities.CallButton;
import com.furongsoft.agv.devices.model.CallButtonModel;
import com.furongsoft.agv.entities.AgvArea;
import com.furongsoft.agv.entities.Site;
import com.furongsoft.base.misc.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 叫料按钮表数据库操作
 *
 * @author linyehai
 */
@Mapper
public interface CallButtonDao extends BaseMapper<CallButton> {
    /**
     * 通过ID获取叫料按钮信息
     *
     * @param id 主键
     * @return 叫料按钮信息
     */
    @SelectProvider(type = CallButtonDao.DaoProvider.class, method = "selectCallButtonById")
    CallButtonModel selectCallButtonById(@Param("id") Long id);

    /**
     * 通过编码获取叫料按钮信息
     *
     * @param code 编码
     * @return 叫料按钮信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectCallButtonByCode")
    CallButtonModel selectCallButtonByCode(@Param("code") String code);

    /**
     * 通过IP地址查找按钮所属区域
     *
     * @param ipAddress  ip地址
     * @param deviceKey  设备唯一表示
     * @param buttonCode 按钮编号
     * @return 按钮信息
     */
    @SelectProvider(type = DaoProvider.class, method = "selectCallButtonAreaByIpAddressOrDeviceKey")
    CallButtonModel selectCallButtonAreaByIpAddressOrDeviceKey(@Param("ipAddress") String ipAddress, @Param("deviceKey") String deviceKey, @Param("buttonCode") String buttonCode);

    /**
     * 查找按钮
     *
     * @return 按钮集合
     */
    @SelectProvider(type = DaoProvider.class, method = "selectCallButtons")
    List<CallButtonModel> selectCallButtons();

    class DaoProvider {
        private static final String CALL_BUTTON_TABLE_NAME = CallButton.class.getAnnotation(TableName.class).value();
        private static final String SITE_TABLE_NAME = Site.class.getAnnotation(TableName.class).value();
        private static final String AGV_AREA_TABLE_NAME = AgvArea.class.getAnnotation(TableName.class).value();
        private static final String AREA_SITE_TABLE_NAME = "t_agv_area_site";


        /**
         * 通过ID获取叫料按钮信息
         *
         * @return sql
         */
        public String selectCallButtonById() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.ip_address,t1.device_key,t1.port,t1.button_code,t1.site_id,t1.code,t1.name");
                    FROM(CALL_BUTTON_TABLE_NAME + " t1");
                    WHERE("t1.id = #{id}");
                }
            }.toString();
        }

        /**
         * 通过类型获取叫料按钮信息
         *
         * @return sql
         */
        public String selectCallButtonByCode(final Map<String, Object> param) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.ip_address,t1.device_key,t1.port,t1.button_code,t1.site_id,t1.code,t1.name");
                    FROM(CALL_BUTTON_TABLE_NAME + " t1");
                    WHERE("t1.code = #{code}");
                }
            }.toString();
        }

        /**
         * 通过IP地址查找按钮所属区域信息
         *
         * @return sql
         */
        public String selectCallButtonAreaByIpAddressOrDeviceKey(final Map<String, Object> params) {
            return new SQL() {
                {
                    SELECT("t1.id,t1.ip_address,t1.device_key,t1.port,t1.button_code,t1.site_id,t1.code,t1.name,t3.name AS areaName,t3.id AS areaId, t3.type AS areaType");
                    FROM(CALL_BUTTON_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(AREA_SITE_TABLE_NAME + " t2 ON t2.site_id = t1.site_id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t2.area_id = t3.id");
                    WHERE("t1.button_code = #{buttonCode}");
                    if ((StringUtils.isNullOrEmpty(params.get("ipAddress"))) && (StringUtils.isNullOrEmpty(params.get("deviceKey")))) {
                        WHERE("t1.ip_address = #{ipAddress}");
                    }
                    if (!StringUtils.isNullOrEmpty(params.get("ipAddress"))) {
                        WHERE("t1.ip_address = #{ipAddress}");
                    }
                    if (!StringUtils.isNullOrEmpty(params.get("deviceKey"))) {
                        WHERE("t1.device_key = #{deviceKey}");
                    }
                }
            }.toString();
        }

        /**
         * 获取叫料按钮
         *
         * @return sql
         */
        public String selectCallButtons() {
            return new SQL() {
                {
                    SELECT("t1.id,t1.ip_address,t1.device_key,t1.port,t1.button_code,t1.site_id,t1.code,t1.name,t3.name AS areaName,t3.id AS areaId, t3.type AS areaType");
                    FROM(CALL_BUTTON_TABLE_NAME + " t1");
                    LEFT_OUTER_JOIN(AREA_SITE_TABLE_NAME + " t2 ON t2.site_id = t1.site_id");
                    LEFT_OUTER_JOIN(AGV_AREA_TABLE_NAME + " t3 ON t2.area_id = t3.id");
                    WHERE("t1.enabled = 1");
                }
            }.toString();
        }
    }
}
