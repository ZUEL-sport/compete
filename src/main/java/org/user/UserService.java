package org.user;

import cn.fabrice.jfinal.service.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
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

    /***
     *根据user_no在team_mate中查找team_no(可能会属于多个团队),得到team(包括team_no和团队名称)
     * @param user_no
     * @return
     */
    public List<Record> myTeam(String user_no){
        Kv cond = Kv.by("user_no",user_no);
        SqlPara sqlPara= Db.getSqlPara("user.myTeam",cond);
        return Db.find(sqlPara);
    }
    /***
     *按编号查找用户,但返回的数据为record类型
     * @param no
     * @return
     */
    public Record getPeople(String no){
        Kv cond = Kv.by("user_no",no);
        SqlPara sqlPara= Db.getSqlPara("user.getByUserNo",cond);
        return Db.findFirst(sqlPara);
    }
}

