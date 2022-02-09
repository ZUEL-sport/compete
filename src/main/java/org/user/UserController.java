package org.user;

import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.jfinal.annotation.Param;
import cn.fabrice.jfinal.annotation.ValidateParam;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import org.common.interceptor.CorsInterceptor;
import org.common.module.Cookie;
import org.common.module.User;

import java.math.BigInteger;
import java.util.List;

import static org.settings.Setting.COOKIE_LIFE;

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
        }
        else{
            Cookie cookie = new Cookie();
            cookie.setAccessToken("登录");
            cookie.setExpiresIn(BigInteger.valueOf(COOKIE_LIFE));
            cookie.setExpiresTime(BigInteger.valueOf(COOKIE_LIFE + System.currentTimeMillis()));
            cookie.setUuno(getPara("user_no"));
            cookie.save();
            setCookie("user_no",user.getUserNo(),(int)COOKIE_LIFE,true);
            renderJson(BaseResult.ok("用户登录成功"));
        }
    }

    @Param(name = "user_no",required = true)
    @Param(name = "school_no",required = true)
    @Param(name = "name",required = true)
    @Param(name = "password",required = true)
    @Param(name = "phone",required = true)
    @Param(name = "sex",required = true)
    public void register(){
        User user = userService.getByUserTel(getPara("phone"));
        if(StrKit.notNull(user)){
            renderJson(BaseResult.fail("该手机号已被注册！"));
            return;
        }
        user = userService.getByUserNo(getPara("user_no"));
        if(StrKit.notNull(user)){
            renderJson(BaseResult.fail("该学号已被注册"));
        }
        else{
            user = new User();
            user.setUserNo(getPara("user_no"));
            user.setSchoolNo(getPara("school_no"));
            user.setName(getPara("name"));
            user.setPassword(getPara("password"));
            user.setPhone(getPara("phone"));
            user.setSex("男".equals(getPara("sex")) ?0:1);
            renderJson(user.save()?BaseResult.ok("注册成功！"):BaseResult.fail("注册失败"));
        }
    }

    public void resetPassword(){//说实话虽然我改了cookie，但是我没看懂这个函数在干什么，请再返工打磨一下
//        User user = userService.getByUserNo(getPara("user_no"));
        User user = userService.getByUserNo(getCookie("user_no"));
        if(getCookie("user_no").equals(user.getUserNo())){
            user.setPassword(getPara("password"));
            user.update();
        }
        renderJson(BaseResult.ok("重置密码成功"));
    }

    /**
     * 查询个人信息
     * cookie中对应用户的个人信息
     */
    public void getUser(){
        Record record = userService.getUserRecByUserNo(getCookie("user_no"));
        if(StrKit.notNull(record)){
            renderJson(record);
        }
        else {
            renderJson(BaseResult.fail("未查询到个人信息"));
        }
    }

    /**
     * 查询团队信息
     * cookie中对应用户的团队信息
     */
    public void getTeamByUserNo(){
        List<Record> records = userService.getTeamRecByUserNo(getCookie("user_no"));
        if(StrKit.notNull(records) && records.size() > 0){
            renderJson(records);
        }
        else {
            renderJson(BaseResult.fail("您当前未加入任何队伍"));
        }
    }
}
