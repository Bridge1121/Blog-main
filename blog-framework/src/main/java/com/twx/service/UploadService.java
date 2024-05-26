package com.twx.service;

import com.twx.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);

    ResponseResult uploadImages(MultipartFile[] imgs);
}
