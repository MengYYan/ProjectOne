package cn.itcast.travel.service;

import cn.itcast.travel.domain.Category;

import java.util.List;

/**
 * @author YuHan
 * @date 2022/4/29
 * @apiNote
 */
public interface CategoryService {
    List<Category> findAll();
}
