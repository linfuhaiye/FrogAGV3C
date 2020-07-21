package com.furongsoft.base.file;

import com.furongsoft.base.entities.UploadFileResponse;
import com.furongsoft.base.file.entities.Attachment;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.base.restful.entities.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件控制器
 *
 * @author Alex
 */
@Controller
@RequestMapping("/api/v1/file")
public class FileController {
    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload")
    @ResponseBody
    public RestResponse upload(@NonNull @RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return new RestResponse(HttpStatus.BAD_REQUEST);
        }

        Attachment attachment = storageService.uploadFile(file);
        Tracker.file("upload: " + attachment.getId());

        return new UploadFileResponse(HttpStatus.OK, attachment);
    }

    /**
     * 根据索引查询文件全路径
     *
     * @param id 索引
     * @return 全路径
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public RestResponse getUser(@NonNull @PathVariable String id) {
        return new RestResponse(HttpStatus.OK, null, storageService.getFullPath(id));
    }
}
