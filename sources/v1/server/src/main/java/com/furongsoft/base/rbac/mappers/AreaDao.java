package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.rbac.entities.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 区域表操作对象
 *
 * @author chenfuqian
 */
@Mapper
@Component
public interface AreaDao extends BaseMapper<Area> {
    /**
     * 获取所有区域
     *
     * @return 资源列表
     */
    @SelectProvider(type = DaoProvider.class, method = "selectAreaList")
    List<Area> selectAreaList();

    class DaoProvider {
        private final String AREA = Area.class.getAnnotation(TableName.class).value();

        /**
         * 查询所有区域列表
         *
         * @return 区域列表信息
         */
        public String selectAreaList() {
            return new SQL() {{
                SELECT("id,parent_id AS parentId,state,code,name,remark,manager");
                FROM(AREA);
            }}.toString();
        }

    }


}
