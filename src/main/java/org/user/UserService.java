package org.user;

import cn.fabrice.jfinal.service.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.common.module.User;

import java.util.List;

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

    public User getByUserTel(String tel){
        Kv cond = Kv.by("phone",tel);
        return get(cond,"getByUserTel");
    }

    public Record getUserRecByUserNo(String userNo){
        Kv cond = Kv.by("user_no",userNo);
        return Db.findFirst("getByUserNo",userNo);
    }

    public List<Record> getTeamRecByUserNo(String userNo){
        Kv cond = Kv.by("user_no",userNo);
        return Db.find("getTeamByUserNo",cond);
    }
}
