package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

/**
 * @author YuHan
 * @date 2022/4/30
 * @apiNote
 */
public interface RouteImgDao {

    /**
     * 根据线路的id查询图片
     */
    List<RouteImg> findByRid(int rid);
}
