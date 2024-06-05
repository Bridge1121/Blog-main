package com.twx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.SearchContent;
import com.twx.domain.vo.PageVo;
import com.twx.mapper.SearchContentMapper;
import com.twx.service.SearchContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户搜索内容表(SearchContent)表服务实现类
 *
 * @author makejava
 * @since 2024-06-05 10:56:26
 */
@Service("searchContentService")
public class SearchContentServiceImpl extends ServiceImpl<SearchContentMapper, SearchContent> implements SearchContentService {


    @Autowired
    private SearchContentService searchContentService;

    @Override
    public ResponseResult getHotList(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SearchContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SearchContent::getCount);//按查询次数倒序排序
        Page<SearchContent> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }
}

