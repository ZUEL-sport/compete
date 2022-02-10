package org.game;

import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.jfinal.annotation.Param;
import cn.fabrice.jfinal.annotation.ValidateParam;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import org.common.interceptor.CorsInterceptor;
import org.common.module.Game;

import java.util.List;

/**
 * @author Administrator
 */
@Before(CorsInterceptor.class)
@ValidateParam
public class GameController extends Controller {
    @Inject
    GameService gameService;
    /**
     * 查看项目
     * 从数据库中取出所有process不是关闭的项目
     */
    public void listGame(){
        String name="关闭";
        List<Record> list= gameService.listByNoProcess(name);
        if(list==null){
            renderJson(BaseResult.fail("当前暂无比赛"));
        }
        else{
            renderJson(list);
        }
    }

    /**
     * 查看项目详情
     * 从数据库中取出指定项目的详情
     */
    @Param(name = "game_no",required = true)
    public void gameDetail(){
        Record record = gameService.getGame(getPara("game_no"));
        if(record != null){
            renderJson(record);
        }
        else{
            renderJson(BaseResult.fail("未查询到比赛信息"));
        }
    }

    public void listGameResults(){

        List<Record> list= gameService.listByGrade(getPara("game_no"),getPara("turn_no"));
        if(list==null){
            renderJson(BaseResult.fail("当前暂无比赛结果"));
        }
        else{
            renderJson(list);
        }
        return;
    }
}
