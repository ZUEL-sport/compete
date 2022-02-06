package org.user;

import cn.fabrice.jfinal.service.BaseService;
import com.jfinal.kit.Kv;
import org.common.module.User;

import java.util.regex.Pattern;

/**
 * 用户基础功能
 * @author Administrator
 */
public class UserService  extends BaseService<User> {

    public UserService(){
        super("user.",User.class,"user");
    }

    public User getByUserNo(String no){
        Kv cond = Kv.by("user_no",no);
        return get(cond,"getByUserNo");
    }
}