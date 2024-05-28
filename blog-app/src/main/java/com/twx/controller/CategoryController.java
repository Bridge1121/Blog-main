package com.twx.controller;

import com.twx.domain.ResponseResult;
import com.twx.domain.vo.CategoryVo;
import com.twx.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@Api(tags = "分类",description = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @ApiOperation(value = "获取当前用户创建的所有文章分类")
    public ResponseResult getCategoryList(Long userId){
        return categoryService.getCategoryList(userId);
    }

    @GetMapping("/addCategory")
    @ApiOperation(value = "新增文章分类")
    public ResponseResult addCategory(String name,Long userId,String description){
        return categoryService.addCategory(name,userId,description);
    }


}
