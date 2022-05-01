package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

/**
 * @author YuHan
 * @date 2022/4/30
 * @apiNote
 */
public interface FavoriteDao {

    Favorite findByRidAndUid(int rid, int uid);

    /**
     * 根据线路id查询收藏次数
     */
    int findCountByRid(int rid);

    void add(int rid, int uid);
}
