package org.user;

import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.jfinal.annotation.Param;
import cn.fabrice.jfinal.annotation.ValidateParam;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import org.common.interceptor.CorsInterceptor;
import org.common.module.Game;
import org.common.module.User;
import org.game.GameService;

import java.util.*;

/**
 * 具体用户接参响应方法
 * @author Administrator
 * ---@Before(CorsInterceptor.class)跨域拦截器
 *
 */
@Before(CorsInterceptor.class)
@ValidateParam
public class UserController extends Controller {
    @Inject
    UserService userService;
    @Inject
    GameService gameService;
    /**
     * 登录响应
     * 学号+密码实现登录
     */
    @Param(name = "user_no",required = true)
    @Param(name = "password",required = true)
    public void login(){
        User user = userService.getByUserNo(getPara("user_no"));
        if(!StrKit.notNull(user)){
            renderJson(BaseResult.fail("用户不存在"));
            return;
        }
        if(!getPara("password").equals(user.getPassword())){
            renderJson(BaseResult.fail("密码错误"));
            return;
        }
        else{
            renderJson(BaseResult.ok("用户登录成功"));
            return;
        }
    }

    @Param(name = "user_no",required = true)
    @Param(name = "school_no",required = true)
    @Param(name = "name",required = true)
    @Param(name = "password",required = true)
    @Param(name = "phone",required = true)
    @Param(name = "sex",required = true)
    public void register(){
        User user = userService.getByUserTel(getPara("phone"));
        if(StrKit.notNull(user)){
            renderJson(BaseResult.fail("该手机号已被注册！"));
            return;
        }
        user = userService.getByUserNo(getPara("user_no"));
        if(StrKit.notNull(user)){
            renderJson(BaseResult.fail("该学号已被注册"));
        }
        else{
            user = new User();
            user.setUserNo(getPara("user_no"));
            user.setSchoolNo(getPara("school_no"));
            user.setName(getPara("name"));
            user.setPassword(getPara("password"));
            user.setPhone(getPara("phone"));
            user.setSex("男".equals(getPara("sex")) ?0:1);
            renderJson(user.save()?BaseResult.ok("注册成功！"):BaseResult.fail("注册失败"));
        }
    }


    public void resetPassword(){

        User user = userService.getByUserNo(getPara("user_no"));

        if(getPara("user_no").equals(user.getUserNo())){
            user.setPassword(getPara("password"));
            user.update();
        }
        renderJson(BaseResult.ok("重置密码成功"));
        return;
    }

    /**
     * 我的项目(待比赛)
     * 根据编号(no)从成绩表中找到game_no+turn_no
     * (1)个人项目:从主页传来的user_no
     * (2)团队项目:根据user_no得到的team_no
     */
    @Param(name = "user_no",required = true)
    public void myGame(){
        /*myGame列表中有三个属性:参赛者编号,项目编号,场次编号*/
        /*(1)个人项目*/
        Record user = userService.getPeople(getPara("user_no"));
        List<Record> myGame= gameService.getMyGame(getPara("user_no"));
        /*(2)团队项目:根据编号查找所属团队*/
        List<Record> myTeam= userService.myTeam(getPara("user_no"));
        /*遍历根据myTeam中的team_no获取参赛信息,将参赛信息存入myGame*/
        for(Record record:myTeam){
            myGame.addAll(gameService.getMyGame(record.getStr("team_no")));
        }
        //参赛者
        List<Record> peopleResult=new ArrayList<Record>();
        //项目名称
        List<Record> gameResult = new ArrayList<Record>();
        //场次名称
        List<Record> turnResult = new ArrayList<Record>();
        /*遍历将myGame中的game_no和turn_no对应的game和turn传到前端,前端取出其中的项目名称+场次名称+比赛时间+比赛场地*/
        for(Record record:myGame){
            /*三条列表:参赛者+项目+场次*/
            if(Objects.equals(record.getStr("no"), getPara("user_no"))){
                /*说明该记录为个人参赛*/
                peopleResult.add(user);
            }
            else{
                /*否则该记录为团队参赛,传入团队信息*/
                peopleResult.add(gameService.getTeam(record.getStr("no")));
            }
            Record game=gameService.getGame(record.getStr("game_no"));
            Record turn=gameService.getTurn(record.getStr("turn_no"));
            gameResult.add(game);
            turnResult.add(turn);
        }
        /*根据学号传递参赛信息*/
        Map<String,List<Record>> map=new HashMap<String,List<Record>>();
        /*将list存入map,再renderJson(map)*/
        map.put("people",peopleResult);
        map.put("game",gameResult);
        map.put("turn",turnResult);
        renderJson(map);
        return;
    }

    /**
     * 我的成绩(已完成的比赛)
     * 根据编号(user_no)从成绩表中找到grade
     */
    @Param(name = "user_no",required = true)
    public void myGrade(){
        User user = userService.getByUserNo(getPara("user_no"));
        /*有成绩的参赛信息*/
        List<Record> myGame= gameService.getMyGrade(user.getUserNo());
        //姓名
        List<Record> peopleResult=new ArrayList<Record>();
        //项目名称
        List<Record> gameResult = new ArrayList<Record>();
        //场次名称
        List<Record> turnResult = new ArrayList<Record>();
        //成绩
        List<Record> gradeResult = new ArrayList<Record>();
        for(Record record:myGame){
            Record game=gameService.getGame(record.getStr("game_no"));
            Record turn=gameService.getTurn(record.getStr("turn_no"));
            peopleResult.add(user.toRecord());
            gameResult.add(game);
            turnResult.add(turn);
            gradeResult.add(record);
        }
        /*根据学号传递参赛信息*/
        Map<String,List<Record>> map=new HashMap<String,List<Record>>();
        /*将list存入map,再renderJson(map)*/
        map.put("people",peopleResult);
        map.put("game",gameResult);
        map.put("turn",turnResult);
        map.put("grade",gradeResult);
        renderJson(map);
        return;
    }

    /**
     * 团队成绩
     * 根据编号(user_no)找到团队,用团队编号从成绩表中找到grade
     */
    @Param(name = "user_no",required = true)
    public void teamGrade(){
        List<Record> myTeam= userService.myTeam(getPara("user_no"));
        List<Record> myGame= new ArrayList<Record>();
        /*遍历根据myTeam中的team_no获取成绩信息,将参赛信息存入myGame*/
        for(Record record:myTeam){
            myGame.addAll(gameService.getMyGrade(record.getStr("team_no")));
        }
        /*根据成绩信息中的no,game_no和team_no获取名称*/
        //团队名
        List<Record> peopleResult=new ArrayList<Record>();
        //项目名称
        List<Record> gameResult = new ArrayList<Record>();
        //场次名称
        List<Record> turnResult = new ArrayList<Record>();
        //成绩
        List<Record> gradeResult = new ArrayList<Record>();
        for(Record record:myGame){
            Record game=gameService.getGame(record.getStr("game_no"));
            Record turn=gameService.getTurn(record.getStr("turn_no"));
            Record team=gameService.getTeam(record.getStr("no"));
            gameResult.add(game);
            turnResult.add(turn);
            peopleResult.add(team);
            gradeResult.add(record);
        }
        /*参赛信息*/
        Map<String,List<Record>> map=new HashMap<String,List<Record>>();
        /*将list存入map,再renderJson(map)*/
        map.put("people",peopleResult);
        map.put("game",gameResult);
        map.put("turn",turnResult);
        map.put("grade",gradeResult);
        renderJson(map);
        return;
    }
}
