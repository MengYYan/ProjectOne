package cn.itcast.travel.service;

/**
 * @author YuHan
 * @date 2022/4/30
 * @apiNote
 */
public interface FavoriteService {

    boolean isFavorite(String rid, int uid);

    /**
     * 添加收藏
     */
    void add(String rid, int uid);
}
