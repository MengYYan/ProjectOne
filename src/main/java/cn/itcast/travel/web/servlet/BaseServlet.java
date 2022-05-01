package cn.itcast.travel.web.servlet; /**
 * @author YuHan
 * @date 2022/4/28
 * @apiNote
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String uri = req.getRequestURI();
        //System.out.println("请求uri" + uri);
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        //System.out.println(methodName);
        try {
            //忽略访问权限修饰符 私有的也可以获取
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //暴力反射
            //method.setAccessible(true);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接将传入的对象序列化json  并且写回客户端
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(response.getOutputStream(), obj);
    }

    /**
     * 将传入的对象序列化为json 返回给调用者
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
