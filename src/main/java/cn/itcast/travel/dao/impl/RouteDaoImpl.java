package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YuHan
 * @date 2022/4/29
 * @apiNote
 */
public class RouteDaoImpl implements RouteDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid, String rname) {
        //String sql = "select count(*) from tab_route where cid = ?";
        //定义sql模板
        String sql = "select count(*) from tab_route where 1=1";
        StringBuilder sb = new StringBuilder(sql);
        List paroams = new ArrayList();//条件们
        //判断参数是否有值
        if (cid != 0) {
            sb.append(" and cid = ?");
            paroams.add(cid);
        }
        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ?");
            paroams.add("%" + rname + "%");
        }
        sql = sb.toString();
        return template.queryForObject(sql, Integer.class, paroams.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        //String sql = "select * from tab_route where cid = ? limit ? , ?";
        String sql = "select * from tab_route where 1=1";
        StringBuilder sb = new StringBuilder(sql);
        List paroams = new ArrayList();//条件们
        //判断参数是否有值
        if (cid != 0) {
            sb.append(" and cid = ?");
            paroams.add(cid);
        }
        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ?");
            paroams.add("%" + rname + "%");
        }
        sb.append(" limit ? , ? ");
        sql = sb.toString();
        paroams.add(start);
        paroams.add(pageSize);
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), paroams.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(Route.class), rid);
    }
}
