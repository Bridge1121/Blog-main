package com.twx.controller;

import com.twx.domain.ResponseResult;
import com.twx.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "上传",description = "上传相关接口")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    @ApiOperation(value = "上传图片",notes = "上传相关图片")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }

    @PostMapping("/uploadImages")
    @ApiOperation(value = "上传多张图片",notes = "上传相关图片")
    public ResponseResult uploadImages(MultipartFile[] imgs){
        return uploadService.uploadImages(imgs);
    }
}
