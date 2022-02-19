package org.user;

import cn.fabrice.jfinal.service.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import org.common.module.User;

import java.util.ArrayList;
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
     * @param userNo 学号/工号
     * @return 团队信息
     */
    public List<Record> myTeam(String userNo){
        Kv cond = Kv.by("user_no",userNo);
        SqlPara sqlPara= Db.getSqlPara("user.myTeam",cond);
        return Db.find(sqlPara);
    }

    /***
     *按编号查找用户,但返回的数据为record类型
     * @param no 学号
     * @return 用户
     */
    public Record getPeople(String no){
        Kv cond = Kv.by("user_no",no);
        SqlPara sqlPara= Db.getSqlPara("user.getByUserNo",cond);
        return Db.findFirst(sqlPara);
    }

    /**
     * 按照学号得到用户的信息，其中学院信息转换成了学院名称
     * @param no 学号
     * @return 用户信息
     */
    public Record getUserDetail(String no){
        Kv cond = Kv.by("user_no", no);
        SqlPara sqlPara = Db.getSqlPara("user.getUserDetailByNo", cond);
        return Db.findFirst(sqlPara);
    }

    /**
     * @param no 学号/工号
     * @return 团队成员等信息
     */
    public List<Record> getMyTeamDetail(String no){
        int sex, ranks;
        Kv cond = Kv.by("user_no", no);
        SqlPara sqlPara = Db.getSqlPara("user.getMyTeam", cond);
        List<Record> teams, myTeam;
        try{
            teams = Db.find(sqlPara);
            myTeam = new ArrayList<>();
            for(Record record : teams){
                cond.clear();
                cond.set("team_no", Integer.valueOf(record.getStr("team_no")));
                sqlPara = Db.getSqlPara("user.getMyTeamDetail", cond);
                myTeam.addAll(Db.find(sqlPara));
                System.out.println("pass");
            }
            for(Record record : myTeam){
                sex = record.getInt("sex");
                ranks = record.getInt("user_ranks");
                record.set("sex", sex == 0 ? "男" : "女");
                record.set("ranks", ranks == 0 ? "队员" : "队长");
            }
            System.out.println(myTeam.size());
            return myTeam;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param no 工号
     * @return 当前能录入成绩的场次信息
     */
    public List<Record> getMyScoreInput(String no){
        Kv cond = Kv.by("user_no", no);
        SqlPara sqlPara = Db.getSqlPara("user.getMyScoreInput", cond);
        return Db.find(sqlPara);
    }

    public List<Record> showInputMembers(String gameNo, String turnNo){
        Kv cond = Kv.by("gameNo", gameNo).set("turnNo", turnNo);
        SqlPara sqlPara = Db.getSqlPara("user.showInputMembers", cond);
        return Db.find(sqlPara);
    }

    /**
     * @param mateNo 运动员学号
     * @param gameNo 比赛编号
     * @param turnNo 场次编号
     * @return 运动员信息
     */
    public Record inputScore(String mateNo, String gameNo, String turnNo){
        Kv cond = Kv.by("mateNo", mateNo).set("gameNo", gameNo).set("turnNo", turnNo).set("gameNo", gameNo);
        SqlPara sqlPara = Db.getSqlPara("user.getInputMember", cond);
        return Db.findFirst(sqlPara);
    }

    /**
     * 待处理的申诉
     * @return 带出来的申诉
     */
    public List<Record> showComplaint(){
        SqlPara sqlPara = Db.getSqlPara("user.showComplaint");
        return Db.find(sqlPara);
    }

    /**
     * @return 已处理的申诉
     */
    public List<Record> showComplaintResult(){
        SqlPara sqlPara = Db.getSqlPara("user.showComplaintResult");
        return Db.find(sqlPara);
    }

    /***
     *查找成员member_no是否在team_no团队中,如果是,返回true,否则返回false
     * @param memberNo 学号/工号
     * @param teamNo 团队编号
     * @return 成功与否
     */
    public boolean selectMember(String memberNo,String teamNo){
        Kv cond = Kv.by("team_no",teamNo);
        SqlPara sqlPara= Db.getSqlPara("user.selectMember",cond);
        List<Record> member=Db.find(sqlPara);
        for(Record record:member){
            /*说明该成员已经在team_no团队中*/
            if(Objects.equals(record.getStr("mate_no"), memberNo)){
                return true;
            }
        }
        return false;
    }

    /***
     *查找team_no中的所有成员
     * @param teamNo 团队编号
     * @return 所有成员
     */
    public List<Record> findMember(String teamNo){
        Kv cond = Kv.by("team_no",teamNo);
        SqlPara sqlPara= Db.getSqlPara("user.selectMember",cond);
        return Db.find(sqlPara);
    }

    /***
     *根据member_no和team_no查找团队成员的记录,将is_deleted置为1
     * @param memberNo 学号/工号
     * @return 成功与否
     */
    public int deleteMember(String memberNo,String teamNo){
        Kv cond = Kv.by("member_no",memberNo).set("team_no",teamNo);
        SqlPara sqlPara= Db.getSqlPara("user.deleteMember",cond);
        return Db.update(sqlPara);
    }

    /***
     *根据team_no在团队表中查找团队记录,将is_deleted置为1
     * @param teamNo 团队编号
     * @return 成功与否
     */
    public int deleteTeam(String teamNo){
        Kv cond = Kv.by("team_no",teamNo);
        SqlPara sqlPara= Db.getSqlPara("user.deleteTeam",cond);
        return Db.update(sqlPara);
    }
}

