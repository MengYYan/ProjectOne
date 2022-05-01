package cn.itcast.travel.web.servlet; /**
 * @author YuHan
 * @date 2022/4/28
 * @apiNote
 */

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码校验
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //保证验证码的一次性
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //ObjectMapper objectMapper = new ObjectMapper();
            //String json = objectMapper.writeValueAsString(info);
            String json = writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }


        //获取数据
        Map<String, String[]> map = request.getParameterMap();
        //封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service完成注册
        //UserService service = new UserServiceImpl();
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();
        //响应结果
        if (flag) {
            //注册成功
            info.setFlag(true);
        } else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败!");
        }
        //将info序列化为json  并且写回客户端
        //ObjectMapper objectMapper = new ObjectMapper();
        //String json = objectMapper.writeValueAsString(info);
        String json = writeValueAsString(info);
        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }


    /**
     * 登录功能
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //UserService service = new UserServiceImpl();
        User u = service.login(user);

        ResultInfo info = new ResultInfo();
        //判断用户对象是否为null
        if (u == null) {
            //用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //判断用户是否激活
        if (u != null && !"Y".equals(u.getStatus())) {
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请登录邮箱激活");
        }
        //登录成功判断
        if (u != null && "Y".equals(u.getStatus())) {
            request.getSession().setAttribute("user", u);//登录成功标记
            info.setFlag(true);
        }

        //响应数据
        //ObjectMapper objectMapper = new ObjectMapper();
        //response.setContentType("application/json;charset=utf-8");
        //objectMapper.writeValue(response.getOutputStream(), info);
        writeValue(info, response);
    }

    /**
     * 查找单个对象
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端
        //ObjectMapper mapper = new ObjectMapper();
        //response.setContentType("application/json;charset=utf-8");
        //mapper.writeValue(response.getOutputStream(), user);
        writeValue(user, response);
    }

    /**
     * 退出功能
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //销毁session
        request.getSession().invalidate();
        //跳转
        response.sendRedirect(request.getContextPath() + "/login.html");
    }


    /**
     * 激活功能
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            //UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            String msg = null;
            if (flag) {
                msg = "激活成功，请<a href='http://localhost/travel/login.html'>登录</a>";
            } else {
                msg = "激活失败，请联系管理员";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
}
