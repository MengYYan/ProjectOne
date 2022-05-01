package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author YuHan
 * @date 2022/4/29
 * @apiNote
 */
public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //从redis中查询
        Jedis jedis = JedisUtil.getJedis();
        //需要用sortedset排序查询
        //Set<String> categorys = jedis.zrange("category", 0, -1);
        //查询sortedset中分数(cid)和值(cname)
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> cs = null;
        //判断查询的集合是否为空
        //为空 需要从数据库查询 并且存入redis中
        if (categorys == null || categorys.size() == 0) {
            //System.out.println("从数据库查询");
            cs = categoryDao.findAll();
            //将集合数据存储到redis中的名为category的key中
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category", cs.get(i).getCid(), cs.get(i).getCname());
            }
        } else {
            //System.out.println("从redis中查询");
            //不为空,将set的数据存入list 并且返回
            cs = new ArrayList<>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                cs.add(category);
            }
        }
        return cs;
    }
}
