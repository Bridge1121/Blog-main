package com.twx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-05-04 08:28:27
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}
