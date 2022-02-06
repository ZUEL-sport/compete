package org.user;

import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.jfinal.annotation.Param;
import cn.fabrice.jfinal.annotation.ValidateParam;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import org.common.interceptor.CorsInterceptor;
import org.common.module.User;

/**
 * 具体用户接参响应方法
 * @author Administrator
 * ---@Before(CorsInterceptor.class)跨域拦截器
 *
 */
@Before(CorsInterceptor.class)
@ValidateParam
public class UserController extends Controller {
    @Inject
    UserService userService;
    /**
     * 登录响应
     * 学号+密码实现登录
     */
    @Param(name = "user_no",required = true)
    @Param(name = "password",required = true)
    public void login(){
        User user = userService.getByUserNo(getPara("user_no"));
        if(!StrKit.notNull(user)){
            renderJson(BaseResult.fail("用户不存在"));
            return;
        }
        if(!getPara("password").equals(user.getPassword())){
            renderJson(BaseResult.fail("密码错误"));
            return;
        }
        else{
            renderJson(BaseResult.ok("用户登录成功"));
            return;
        }
    }
}
