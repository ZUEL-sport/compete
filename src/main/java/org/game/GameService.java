package org.game;

import cn.fabrice.jfinal.service.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
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

    /**
     * 根据game_no获取game信息
     * @param game_no
     * @return
     */
    public Record getGame(String game_no){
        Kv cond = Kv.by("game_no",game_no);
        SqlPara sqlPara = Db.getSqlPara("game.getGame",cond);
        return Db.findFirst(sqlPara);
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
     * 在成绩表中查找game_no和turn_no
     * @param no
     * @return
     */
    public List<Record> getMyGame(String no){
        Kv cond = Kv.by("no",no);
        SqlPara sqlPara=Db.getSqlPara("game.getMyGame",cond);
        return Db.find(sqlPara);
    }

    /***
     * 在成绩表中查找有成绩的game_no,turn_no,grade,ranking
     * @param no
     * @return
     */
    public List<Record> getMyGrade(String no){
        Kv cond = Kv.by("no",no);
        SqlPara sqlPara=Db.getSqlPara("game.getMyGrade",cond);
        return Db.find(sqlPara);
    }
};
