package com.twx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.constants.SystemConstants;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.Article;
import com.twx.domain.entity.Category;
import com.twx.domain.vo.CategoryVo;
import com.twx.enums.AppHttpCodeEnum;
import com.twx.exception.SystemException;
import com.twx.mapper.CategoryMapper;
import com.twx.service.ArticleService;
import com.twx.service.CategoryService;
import com.twx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2024-05-03 16:03:14
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult getCategoryList(Long userId) {
//        //查询当前用户文章表状态为已发布的文章
//        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//        articleLambdaQueryWrapper.eq(Article::getCreateBy,userId);
//        List<Article> articleList = articleService.list(articleLambdaQueryWrapper);
//        //获取文章的分类id，去重
//        Set<Long> categoryIds = articleList.stream()
//                .map(article -> article.getCategoryId())
//                .collect(Collectors.toSet());
//
//        //查询分类表
//        List<Category> categories = listByIds(categoryIds);
//
//        categories.stream().filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
//                .collect(Collectors.toList());
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getCreateBy,userId);
        queryWrapper.eq(Category::getDelFlag,SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Category> categories = categoryService.list(queryWrapper);
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult addCategory(String name, Long userId, String description) {
        Category category = new Category(name,description,userId,userId);
        if (categoryNameExist(name)){
            throw new SystemException(AppHttpCodeEnum.CATEGORYNAME_EXIST);
        }
        categoryService.save(category);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName,name);
        List<Category> categories = categoryService.list(queryWrapper);
        return ResponseResult.okResult(categories.get(0).getId());
    }

    private boolean categoryNameExist(String categoryName) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName,categoryName);

        return count(queryWrapper)>0;
    }



}

