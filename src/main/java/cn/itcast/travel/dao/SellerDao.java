package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

/**
 * @author YuHan
 * @date 2022/4/30
 * @apiNote
 */
public interface SellerDao {

    Seller findById(int id);
}
