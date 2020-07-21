package com.furongsoft.base.file.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.file.entities.Attachment;
import com.furongsoft.base.misc.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.io.Serializable;

/**
 * 文件表操作对象
 *
 * @author Alex
 */
@Mapper
public interface AttachmentDao extends BaseMapper<Attachment> {
    /**
     * 根据索引查询文件全路径
     *
     * @param id 索引
     * @return 全路径
     */
    @SelectProvider(type = DaoProvider.class, method = "selectFullPath")
    String selectFullPath(Serializable id);

    class DaoProvider {
        /**
         * 根据索引查询文件全路径
         *
         * @return 全路径
         */
        public String selectFullPath() {
            return new SQL() {{
                SELECT(" CONCAT(t2.attachment_server,t1.name) AS url");
                FROM(" t_sys_attachment t1,t_sys_config t2");
                WHERE("t1.id = #{id} AND t2.id = '" + Constants.CONFIG_ID + "'");
            }}.toString();
        }
    }
}
