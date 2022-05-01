package cn.itcast.travel.web.servlet; /**
 * @author YuHan
 * @date 2022/4/27
 * @apiNote
 */

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        UserService service = new UserServiceImpl();
        User u = service.login(user);

        ResultInfo info = new ResultInfo();
        //判断用户对象是否为null
        if( u == null) {
            //用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //判断用户是否激活
        if(u != null &&!"Y".equals(u.getStatus())) {
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请登录邮箱激活");
        }
        //登录成功判断
        if(u!=null && "Y".equals(u.getStatus())) {
            request.getSession().setAttribute("user",u);//登录成功标记
            info.setFlag(true);
        }

        //响应数据
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(response.getOutputStream(),info);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
