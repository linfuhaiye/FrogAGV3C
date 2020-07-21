package com.furongsoft.base.rbac.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RolePermission implements Serializable {

    private long roleId;

    private List<Long> auths;
}
