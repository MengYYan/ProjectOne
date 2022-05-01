package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;

import java.util.List;

/**
 * @author YuHan
 * @date 2022/4/29
 * @apiNote
 */
public interface CategoryDao {
    List<Category> findAll();
}
