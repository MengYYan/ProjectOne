package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

/**
 * @author YuHan
 * @date 2022/4/27
 * @apiNote
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册用户
     */
    @Override
    public boolean regist(User user) {
        //1.根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());
        if (u != null) {
            //用户名存在，注册失败
            return false;
        }
        //2.保存用户信息
        //设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");
        userDao.save(user);

        //3.激活邮件发送，邮箱正文
        String content = "<a href='http://localhost/travel/user/active?code=" + user.getCode() + "'>点击激活</a>";
        MailUtils.sendMail(user.getEmail(), content, "激活邮件");
        return true;
    }

    /**
     * 激活用户
     */
    @Override
    public boolean active(String code) {
        //根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if (user != null) {
            userDao.updateStatus(user);
            return true;
        }
        return false;
    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
