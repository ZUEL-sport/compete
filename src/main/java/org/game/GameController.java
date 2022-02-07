package org.game;

import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.jfinal.annotation.ValidateParam;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import org.common.interceptor.CorsInterceptor;

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
        return;
    }
}
