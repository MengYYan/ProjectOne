package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

/**
 * @author YuHan
 * @date 2022/4/27
 * @apiNote
 */
public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * */
    User findByUsername(String username);

    void save(User user);

    User findByCode(String code);

    void updateStatus(User user);

    User findByUsernameAndPassword(String username, String password);
}
