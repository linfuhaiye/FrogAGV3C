package com.furongsoft.base.rbac.controllers.api.v1;

import com.furongsoft.base.entities.PageRequest;
import com.furongsoft.base.entities.PageResponse;
import com.furongsoft.base.rbac.entities.DictionaryGroup;
import com.furongsoft.base.rbac.services.DictionaryGroupService;
import com.furongsoft.base.restful.entities.RestResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 字典管理控制
 *
 * @author chenfuqian
 */
@RestController
@RequestMapping("/api/v1/system")
@RequiresPermissions("/system/dictgroup")
public class DictionaryGroupController {

    private final DictionaryGroupService dictionaryGroupService;

    @Autowired
    public DictionaryGroupController(DictionaryGroupService dictionaryGroupService) {
        this.dictionaryGroupService = dictionaryGroupService;
    }

    /**
     * 获取字典列表
     *
     * @return 响应内容
     */
    @GetMapping("/dicts/group")
    @RequiresAuthentication
    public PageResponse getDicts(
            PageRequest pageRequest, @RequestParam(required = false) String name) {
        return new PageResponse<>(HttpStatus.OK, dictionaryGroupService.selectMapsPage(pageRequest.getPage(), name));
    }

    @GetMapping("/dicts/groups")
    @RequiresAuthentication
    public RestResponse getDictsGroups() {
        return new RestResponse(HttpStatus.OK, null, dictionaryGroupService.selectAll());
    }

    /**
     * 添加业务字典
     *
     * @param dictionaryGroup 业务字典
     * @return 响应内容
     */
    @PostMapping("/dicts/group")
    @RequiresAuthentication
    public RestResponse addDicts(@NonNull @Validated DictionaryGroup dictionaryGroup) {
        dictionaryGroupService.insertDictGroup(dictionaryGroup);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 更新业务字典
     *
     * @param id   业务字典索引
     * @param dict 业务字典
     * @return 响应内容
     */
    @PutMapping("/dicts/group/{id}")
    @RequiresAuthentication
    public RestResponse editDict(@NonNull @PathVariable Long id, @NonNull @Validated DictionaryGroup dict) {
        dict.setId(id);
        dictionaryGroupService.editDictGroup(dict);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除业务字典
     *
     * @param id 索引
     * @return 响应内容
     */
    @DeleteMapping("/dicts/group/{id}")
    @RequiresAuthentication
    public RestResponse delResource(@NonNull @PathVariable Long id) {
        dictionaryGroupService.del(id);
        return new RestResponse(HttpStatus.OK);
    }
}
