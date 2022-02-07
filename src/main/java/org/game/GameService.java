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
};
