package cn.itcast.travel.web.servlet; /**
 * @author YuHan
 * @date 2022/4/29
 * @apiNote
 */

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受参数
        String currentPagestr = request.getParameter("currentPage");
        String pageSizestr = request.getParameter("pageSize");
        String cidstr = request.getParameter("cid");

        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        int cid = 0;
        if (cidstr != null && cidstr.length() > 0 && !"null".equals(cidstr)) {//类别id
            cid = Integer.parseInt(cidstr);
        }
        int currentPage = 0;
        if (currentPagestr != null && currentPagestr.length() > 0) {//当前的页码 如果不传递 默认为第一页
            currentPage = Integer.parseInt(currentPagestr);
        } else {
            currentPage = 1;
        }
        int pageSize = 0;
        if (pageSizestr != null && pageSizestr.length() > 0) {//每页显示的条数 
            pageSize = Integer.parseInt(pageSizestr);
        } else {
            pageSize = 5;
        }
        //调用service查询 pageBea对象
        PageBean<Route> pb = routeService.pegeQuery(cid, currentPage, pageSize, rname);
        //将pageBean序列化为json并返回
        writeValue(pb, response);
    }

    /**
     * 根据id查询一个旅游线路的详细信息
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        Route route = routeService.findOne(rid);
        writeValue(route, response);
    }


    /**
     * 判断当前登录用户是否收藏过改线路
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");

        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //用户尚未登录
            uid = 0;
        } else {
            uid = user.getUid();
        }
        boolean flag = favoriteService.isFavorite(rid, uid);
        writeValue(flag, response);
    }


    /**
     * 添加收藏
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        //获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //用户尚未登录
            return;
        } else {
            uid = user.getUid();
        }
        //调用service添加
        favoriteService.add(rid, uid);
    }
}
