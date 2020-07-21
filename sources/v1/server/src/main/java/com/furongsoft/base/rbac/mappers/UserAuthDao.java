package com.furongsoft.base.rbac.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.furongsoft.base.rbac.entities.DataAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthDao extends BaseMapper<DataAuth> {
}
