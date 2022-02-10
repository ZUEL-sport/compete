package org.user;

import cn.fabrice.jfinal.service.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import org.common.module.User;

import java.util.List;
import java.util.Objects;

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

    /***
     *查找成员member_no是否在team_no团队中,如果是,返回true,否则返回false
     * @param member_no
     * @return
     */
    public boolean selectMember(String member_no,String team_no){
        Kv cond = Kv.by("team_no",team_no);
        SqlPara sqlPara= Db.getSqlPara("user.selectMember",cond);
        List<Record> member=Db.find(sqlPara);
        for(Record record:member){
            /*说明该成员已经在team_no团队中*/
            if(Objects.equals(record.getStr("mate_no"), member_no)){
                return true;
            }
        }
        return false;
    }

    /***
     *查找team_no中的所有成员
     * @param team_no
     * @return
     */
    public List<Record> findMember(String team_no){
        Kv cond = Kv.by("team_no",team_no);
        SqlPara sqlPara= Db.getSqlPara("user.selectMember",cond);
        return Db.find(sqlPara);
    }

    /***
     *根据member_no和team_no查找团队成员的记录,将is_deleted置为1
     * @param member_no
     * @return
     */
    public int deleteMember(String member_no,String team_no){
        Kv cond = Kv.by("member_no",member_no).set("team_no",team_no);
        SqlPara sqlPara= Db.getSqlPara("user.deleteMember",cond);
        return Db.update(sqlPara);
    }

    /***
     *根据team_no在团队表中查找团队记录,将is_deleted置为1
     * @param team_no
     * @return
     */
    public int deleteTeam(String team_no){
        Kv cond = Kv.by("team_no",team_no);
        SqlPara sqlPara= Db.getSqlPara("user.deleteTeam",cond);
        return Db.update(sqlPara);
    }
}

