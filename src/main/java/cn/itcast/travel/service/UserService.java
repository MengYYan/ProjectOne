package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

/**
 * @author YuHan
 * @date 2022/4/27
 * @apiNote
 */
public interface UserService {

    /**
     * 注册用户
     * */
    boolean regist(User user);

    boolean active(String code);

    User login(User user);
}
