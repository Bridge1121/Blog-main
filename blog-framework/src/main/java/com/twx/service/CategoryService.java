package com.twx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-05-03 16:03:12
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
