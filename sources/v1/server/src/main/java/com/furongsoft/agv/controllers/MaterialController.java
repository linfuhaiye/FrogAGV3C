package com.furongsoft.agv.controllers;

import com.furongsoft.agv.services.MaterialService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 存货控制层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/agv")
public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

//    /**
//     * 分页获取商品信息
//     *
//     * @param pageRequest
//     * @return
//     */
//    @GetMapping("/materials")
//    public PageResponse materials(PageRequest pageRequest) {
//        Page<MaterialsInfo> page = materialService.getMaterialssList(pageRequest.getPage());
//        return new PageResponse<>(HttpStatus.OK, page);
//    }

//    /**
//     * 根据商品ID获取商品信息
//     *
//     * @param id
//     * @return
//     */
//    @GetMapping("/materials/{id}")
//    public RestResponse getMaterials(@NonNull @PathVariable Long id, String code, String openid) {
//        return new RestResponse(HttpStatus.OK, null, materialService.getMaterialsInfoById(id, code, openid));
//    }

//    /**
//     * 新增商品信息
//     *
//     * @param material
//     * @return
//     */
//    @PostMapping("/materials")
//    @RequiresAuthentication
//    public RestResponse addMaterialss(@NonNull @Validated @RequestBody Materials material) {
//        materialService.insertMaterials(material);
//        return new RestResponse(HttpStatus.OK);
//    }

//    /**
//     * 修改商品信息
//     *
//     * @param id
//     * @param material
//     * @return
//     */
//    @PutMapping("/materials/{id}")
//    @RequiresAuthentication
//    public RestResponse editMaterialss(@NonNull @PathVariable Long id, @NonNull @Validated @RequestBody Materials material) {
//        material.setId(id);
//        materialService.updateMaterials(material);
//        return new RestResponse(HttpStatus.OK);
//    }

//    /**
//     * 删除商品
//     *
//     * @param id
//     * @return
//     */
//    @DeleteMapping("/materials/{id}")
//    @RequiresAuthentication
//    public RestResponse deleteMaterialss(@NonNull @PathVariable Long id) {
//        materialService.deleteMaterials(id);
//        return new RestResponse(HttpStatus.OK);
//    }
}
