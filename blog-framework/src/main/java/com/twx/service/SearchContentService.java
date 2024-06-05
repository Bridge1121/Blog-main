package com.twx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.SearchContent;


/**
 * 用户搜索内容表(SearchContent)表服务接口
 *
 * @author makejava
 * @since 2024-06-05 10:56:26
 */
public interface SearchContentService extends IService<SearchContent> {

    ResponseResult getHotList(Integer pageNum, Integer pageSize);
}
