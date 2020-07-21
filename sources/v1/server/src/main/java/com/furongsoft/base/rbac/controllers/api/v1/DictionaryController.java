package com.furongsoft.base.rbac.controllers.api.v1;

import com.furongsoft.base.entities.PageRequest;
import com.furongsoft.base.entities.PageResponse;
import com.furongsoft.base.rbac.entities.Dictionary;
import com.furongsoft.base.rbac.services.DictionaryService;
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
@RequiresPermissions("/system/dict")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    /**
     * 获取字典列表
     *
     * @return 响应内容
     */
    @GetMapping("/dicts")
    @RequiresAuthentication
    public PageResponse getDicts(
            PageRequest pageRequest, @RequestParam(required = false) String name, @RequestParam(required = false) Long groupId) {
        return new PageResponse<>(HttpStatus.OK, dictionaryService.selectMapsPage(pageRequest.getPage(), name, groupId));
    }

    /**
     * 根据字典组别代码查询业务字典
     *
     * @param code 字典代码
     * @return 字典列表
     */
    @GetMapping("/dicts/{code}")
    @RequiresAuthentication
    public RestResponse editDict(@NonNull @PathVariable String code) {
        return new RestResponse(HttpStatus.OK, null, dictionaryService.selectDictsByCode(code));
    }

    /**
     * 添加业务字典
     *
     * @param dictionary 业务字典
     * @return 响应内容
     */
    @PostMapping("/dicts")
    @RequiresAuthentication
    public RestResponse addDicts(@NonNull @Validated Dictionary dictionary) {
        dictionaryService.add(dictionary);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 更新业务字典
     *
     * @param id   业务字典索引
     * @param dict 业务字典
     * @return 响应内容
     */
    @PutMapping("/dicts/{id}")
    @RequiresAuthentication
    public RestResponse editDict(@NonNull @PathVariable Long id, @NonNull @Validated Dictionary dict) {
        dict.setId(id);
        dictionaryService.edit(dict);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除业务字典
     *
     * @param id 索引
     * @return 响应内容
     */
    @DeleteMapping("/dicts/{id}")
    @RequiresAuthentication
    public RestResponse delResource(@NonNull @PathVariable Long id) {
        dictionaryService.del(id);
        return new RestResponse(HttpStatus.OK);
    }
}
