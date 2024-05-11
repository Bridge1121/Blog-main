package com.twx.controller;

import com.twx.domain.ResponseResult;
import com.twx.domain.dto.AddArticleDto;
import com.twx.domain.entity.Article;
import com.twx.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@Api(tags = "文章",description = "文章相关接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

//    @GetMapping("/list")
//    public List<Article> test(){
//        return articleService.list();
//    }
    @GetMapping("/hotArticleList")
    @ApiOperation(value = "热门文章查询",notes = "查询热门文章列表信息")
    public ResponseResult hotArticleList(){
        //查询热门文章，封装成ResponseResult返回
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    @GetMapping("/articleList")
    @ApiOperation(value = "分页查询某个类型文章列表",notes = "查询某类文章列表信息")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/draftList")
    @ApiOperation(value = "分页查询当前用户草稿列表",notes = "查询当前用户草稿列表")
    public ResponseResult draftArticleList(Integer pageNum,Integer pageSize,Long userId){
        return articleService.draftArticleList(pageNum,pageSize,userId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }

    @PostMapping("/add")
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }

    @DeleteMapping("/deleteArticle/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }

    @PutMapping("/updateArticle")
    public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @GetMapping("/searchArticle")
    public ResponseResult searchArticle(Integer pageNum,Integer pageSize,String content){
        return articleService.searchArticle(pageNum,pageSize,content);
    }

}
