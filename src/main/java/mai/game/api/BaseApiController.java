package mai.game.api;

import mai.game.core.bean.Response;
import mai.game.core.exception.ApiAssert;
import mai.game.entity.po.User;
import mai.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/*
* @Name:BaseApiController
* @Description:这个是基础的API控制器
* @Date：2020
* @Author:麦发强
* */

public class BaseApiController {

    @Autowired
    private UserService userService;

    /**
     * @Name:success()
     * @Description:这个是返回成功的信息
     * @param :T(泛型)
     */

    protected <T> Response<T> success(int code,T result) {
        //实例化封装好的信息类
        Response<T> restResponse = Response.newInstance();
        //设置状态码
        restResponse.setCode(code);
        restResponse.setData(result);
        return restResponse;
    }

    protected <T> Response<T> fail(int code,T result) {
        //实例化封装好的信息类
        Response<T> restResponse = Response.newInstance();
        restResponse.setCode(code);
        restResponse.setData(result);
        return restResponse;
    }


    /**
     * @Name:success()
     * @Description:这个是返回成功的信息
     * @param :T(泛型)
     */
    protected <T> Response<T> success(String msg ,int code,int count,T data) {
        //实例化封装好的信息类
        Response<T> response = Response.newInstance();
        //设置提示信息
        response.setMsg(msg);
        //设置状态码
        response.setCode(code);
        //设置返回结果
        response.setCount(count);
        //设置返回结果
        response.setData(data);
        return response;
    }

    /**
     * @Name:success()
     * @Description:这个是返回成功的信息
     * @param :T(泛型)
     */
    protected <T> Response<T> success(String msg ,int code,int count,T data,int[] checked) {
        //实例化封装好的信息类
        Response<T> response = Response.newInstance();
        //设置提示信息
        response.setMsg(msg);
        //设置状态码
        response.setCode(code);
        //设置返回结果
        response.setCount(count);
        //设置返回结果
        response.setData(data);
        //设置返回数组
        response.setChecked(checked);
        return response;
    }

    /**
     * @Name:success()
     * @Description:这个是返回成功的信息
     * @param :T(泛型)
     */
    protected <T> Response<T> success(T data) {
        //实例化封装好的信息类
        Response<T> response = Response.newInstance();
        //设置返回结果
        response.setData(data);
        return response;
    }


    protected <T> Response<T> success(String msg ,int code,T data) {
        //实例化封装好的信息类
        Response<T> response = Response.newInstance();
        //设置提示信息
        response.setMsg(msg);
        //设置状态码
        response.setCode(code);
        //设置返回结果
        response.setData(data);
        return response;
    }



    /*
     * @Name:fail()
     * @Description:这个是返回失败的信息
     * @Param:T(泛型)
     * */
    protected <T> Response<T> fail(String msg,int code,int count,T result){
        //实例化封装好的信息类
        Response<T> response = Response.newInstance();
        //设置提示信息
        response.setMsg(msg);
        //设置返回结果
        response.setCount(count);
        //设置状态码
        response.setCode(code);
        //设置返回结果
        response.setData(result);
        return response;
    }

    /*
     * @Name:fail()
     * @Description:这个是返回失败的信息
     * @Param:T(泛型)
     * */
    protected <T> Response<T> fail(String msg,int code,T result){
        //实例化封装好的信息类
        Response<T> response = Response.newInstance();
        //设置提示信息
        response.setMsg(msg);
        //设置状态码
        response.setCode(code);
        //设置返回结果
        response.setData(result);
        return response;
    }

    // 因为大部分地方用到的这个方法都是token必须要传且正确的，所以这里重载一下getApiUser方法，默认传true
    protected User getApiUser() {
        return getApiUser(true);
    }

    // 接口路由从request里拿token，通过请求UserService获取用户的信息
    // required: boolean 判断是否必须要token，因为有的接口token是非必须的，但如果传了token就可以多返回一些信息
    protected User getApiUser(boolean required) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        String token = request.getHeader("token");
        System.out.println("获取的token是"+token);
        //    String token = request.getParameter("token");
        if (required) { // token必须要
            // 判断token是否存在，不存在要抛异常
            ApiAssert.notEmpty(token, "token不能为空");
            // 用token查用户信息，查不到要抛异常
            //User user = userService.selectByToken(token);
            User user = new User();
            ApiAssert.notNull(user, "token不正确，请在网站上登录自己的帐号，然后进入个人设置页面扫描二维码获取token");
            return user;
        } else { // token非必须
            // 先判断token存在不，不存在直接返回null
            if (StringUtils.isEmpty(token)) return null;
            // 如果token存在，直接查询用户信息，不管查到查不到，都直接返回
            //return userService.selectByToken(token);
            return null;
        }
    }
}
