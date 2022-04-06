package org.game;

import cn.fabrice.jfinal.service.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import org.common.module.Complaint;
import org.common.module.Game;

import java.util.List;


/**
 * 项目基础功能
 * @author Administrator
 */
public class GameService extends BaseService<Game> {
    /***
     * 查找比赛流程的名字不是"name"的比赛
     * @param name
     * @return
     */
    public List<Record> listByNoProcess(String name){
        Kv cond=Kv.by("name",name);
        SqlPara sqlPara= Db.getSqlPara("game.listByNoProcess",cond);
        return Db.find(sqlPara);
    }

    /***
     * 查找比赛+场次
     * @return
     */
    public List<Record> showNeedReferee(){
        SqlPara sqlPara= Db.getSqlPara("game.showNeedReferee");
        return Db.find(sqlPara);
    }

    /***
     * 查找比赛流程的名字是"name"的比赛
     * @param name 名称
     * @return 名称
     */
    public List<Record> listByProcess(String name){
        Kv cond=Kv.by("name",name);
        SqlPara sqlPara= Db.getSqlPara("game.listByProcess",cond);
        return Db.find(sqlPara);
    }

    /**
     * 根据game_no获取game信息
     * @param gameNo 项目编号
     * @return game信息和场次信息
     */
    public Record getGame(String gameNo){
        Kv cond = Kv.by("game_no",gameNo);
        SqlPara sqlPara = Db.getSqlPara("game.getGame",cond);
        return Db.findFirst(sqlPara);
    }

    /**
     * 项目详情
     * @param gameNo 项目编号
     * @return 项目详情
     */
    public List<Record> getGameDetail(String gameNo){
        Kv cond = Kv.by("game_no",gameNo);
        SqlPara sqlPara = Db.getSqlPara("game.getGameDetail", cond);
        List<Record> records = Db.find(sqlPara);
        sqlPara.clear();
        sqlPara = Db.getSqlPara("game.getAllTurn", cond);
        records.addAll(Db.find(sqlPara));
        return records;
    }

    /***
     * 根据turn_no获取turn信息(turn_no可以唯一决定某个场次,而不是game_no+turn_no唯一决定场次)
     * @param turn_no
     * @return
     */
    public Record getTurn(String turn_no){
        Kv cond = Kv.by("turn_no",turn_no);
        SqlPara sqlPara=Db.getSqlPara("game.getTurn",cond);
        return Db.findFirst(sqlPara);
    }

    /***
     * 根据team_no获取team信息
     * @param team_no
     * @return
     */
    public Record getTeam(String team_no){
        Kv cond = Kv.by("team_no",team_no);
        SqlPara sqlPara=Db.getSqlPara("game.getTeam",cond);
        return Db.findFirst(sqlPara);
    }


    public List<Record> listByGrade(String game_no,String turn_no){
        Kv cond=Kv.by("game_no",game_no).set("turn_no",turn_no);
        SqlPara sqlPara= Db.getSqlPara("game.listByGrade",cond);
        return Db.find(sqlPara);
    }

    /***
     * 在成绩表中查找参赛者(个人),项目名称,场次名称,比赛时间,比赛场地
     * @param user_no
     * @return
     */
    public List<Record> getMyGame(String user_no){
        Kv cond = Kv.by("user_no",user_no);
        SqlPara sqlPara=Db.getSqlPara("game.getMyGame",cond);
        return Db.find(sqlPara);
    }

    /***
     * 在成绩表中查找参赛者(团队),项目名称,场次名称,比赛时间,比赛场地
     * @param team_no
     * @return
     */
    public List<Record> getTeamGame(String team_no){
        Kv cond = Kv.by("team_no",team_no);
        SqlPara sqlPara=Db.getSqlPara("game.getTeamGame",cond);
        return Db.find(sqlPara);
    }

    /***
     * 在成绩表中查找某项目当前场次的所有参赛者(个人),项目名称,场次名称,成绩,排名,用于在晋级列表中显示
     * 在grade表中找到该turn_no的参赛信息,用no在user中查询得到name
     * @param turn_no
     * @return
     */
    public List<Record> getAllPeopleGame(String turn_no){
        Kv cond = Kv.by("turn_no",turn_no);
        SqlPara sqlPara=Db.getSqlPara("game.getAllPeopleGame",cond);
        return Db.find(sqlPara);
    }

    /***
     * 在成绩表中查找某项目当前场次的所有参赛者(团队),项目名称,场次名称,成绩,排名,用于在晋级列表中显示
     * 在grade表中找到该turn_no的参赛信息,用no在team中查询得到name
     * @param turn_no
     * @return
     */
    public List<Record> getAllTeamGame(String turn_no){
        Kv cond = Kv.by("turn_no",turn_no);
        SqlPara sqlPara=Db.getSqlPara("game.getAllTeamGame",cond);
        return Db.find(sqlPara);
    }


    /***
     * 在成绩表中查找有成绩的姓名,项目名称,场次名称,成绩,排名
     * @param user_no
     * @return
     */
    public List<Record> getMyGrade(String user_no){
        Kv cond = Kv.by("user_no",user_no);
        SqlPara sqlPara=Db.getSqlPara("game.getMyGrade",cond);
        return Db.find(sqlPara);
    }

    /***
     * 在成绩表中查找有成绩的团队名称,项目名称,场次名称,成绩,排名
     * @param team_no
     * @return
     */
    public List<Record> getTeamGrade(String team_no){
        Kv cond = Kv.by("team_no",team_no);
        SqlPara sqlPara=Db.getSqlPara("game.getTeamGrade",cond);
        return Db.find(sqlPara);
    }

    /***
     * 显示所有比赛的名字+当前流程名字
     * @param
     * @return
     */
    public List<Record> listGameProcess(){
        SqlPara sqlPara= Db.getSqlPara("game.listGameProcess");
        return Db.find(sqlPara);
    }

    /***
     *根据team_no在项目表中查找记录,将process_no修改为新传入的process_no
     * @param game_no
     * @return
     */
    public int updateGameProcess(String game_no,String process_no){
        Kv cond = Kv.by("game_no",game_no).set("process_no",process_no);
        SqlPara sqlPara= Db.getSqlPara("game.updateGameProcess",cond);
        return Db.update(sqlPara);
    }

    /***
     *根据game_no在game表中查找game,将now_turn_no修改为新传入的now_turn_no
     * @param game_no
     * @return
     */
    public int updateNowTurnNo(String game_no,String now_turn_no){
        Kv cond = Kv.by("game_no",game_no).set("now_turn_no",now_turn_no);
        SqlPara sqlPara= Db.getSqlPara("game.updateNowTurnNo",cond);
        return Db.update(sqlPara);
    }

    /***
     * 修改成绩
     * @param user_no
     * @return
     */
    public int updateGrade(String user_no,String turn_no,double grade){
        Kv cond = Kv.by("user_no",user_no).set("turn_no",turn_no).set("grade",grade);
        SqlPara sqlPara= Db.getSqlPara("game.updateGrade",cond);
        return Db.update(sqlPara);
    }

//    public Complaint getByState(String user_no){
//        Kv cond = Kv.by("user_no",user_no);
//        return get(cond,"getByState");
//        //return Db.find(sqlPara);
//    }
//
//
//    public List<Record> getComplaintResult(String user_no){
//        Kv cond = Kv.by("user_no",user_no);
//        SqlPara sqlPara=Db.getSqlPara("game.getComplaintResult",cond);
//        return Db.find(sqlPara);
//    }

    /**
     * @return 报名结束的比赛
     */
    public List<Record> showSignedGame(){
        SqlPara sqlPara = Db.getSqlPara("game.showSignedGame");
        return Db.find(sqlPara);
    }

    public Record getSavingMember(String playerNo, String gameNo){
        Kv cond = Kv.by("playerNo", playerNo).set("gameNo", gameNo);
        SqlPara sqlPara = Db.getSqlPara("game.getSavingMember", cond);
        try{
            Record record = Db.findFirst(sqlPara);
            record.set("is_pass", 1);
            return record;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
};
