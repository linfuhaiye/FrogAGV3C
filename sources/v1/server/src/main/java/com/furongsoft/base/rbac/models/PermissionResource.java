package com.furongsoft.base.rbac.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PermissionResource implements Serializable {

    private long permissionId;

    private List<Long> auths;
}
