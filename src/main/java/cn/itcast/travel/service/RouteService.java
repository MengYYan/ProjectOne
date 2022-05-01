package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

/**
 * @author YuHan
 * @date 2022/4/29
 * @apiNote
 */
public interface RouteService {

    PageBean<Route> pegeQuery(int cid, int currentPage, int pageSize, String rname);

    /**
     * 根据id查询
     */
    Route findOne(String rid);
}
