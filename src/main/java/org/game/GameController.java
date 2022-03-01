package org.game;

import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.common.pojo.DataResult;
import cn.fabrice.jfinal.annotation.Param;
import cn.fabrice.jfinal.annotation.ValidateParam;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.common.interceptor.CorsInterceptor;
import org.common.module.*;
import org.user.UserController;
import org.user.UserService;

import java.util.*;

/**
 * @author Administrator
 */
@Before(CorsInterceptor.class)
@ValidateParam
public class GameController extends Controller {
    @Inject
    GameService gameService;
    @Inject
    UserService userService;
    /**
     * 查看项目
     * 从数据库中取出所有process不是关闭的项目
     */
    public void listGame(){
        List<Record> list= gameService.listByNoProcess("关闭");
        if(list.isEmpty()){
            renderJson(BaseResult.fail("当前暂无比赛"));
        }
        else{
            renderJson(DataResult.data(list));
        }
    }

    /**
     * 查看项目详情
     * 从数据库中取出指定项目的详情
     */
    @Param(name = "game_no",required = true)
    public void gameDetail(){
        List<Record> records = gameService.getGameDetail(getPara("game_no"));
        if(records != null){
            renderJson(DataResult.data(records));
        }
        else{
            renderJson(BaseResult.fail("未查询到比赛信息"));
        }
    }

    /**
     * 显示项目结果
     */
    @Param(name = "game_no",required = true)
    @Param(name = "turn_no",required = true)
    public void listGameResults(){
        List<Record> list= gameService.listByGrade(getPara("game_no"),getPara("turn_no"));
        if(list.isEmpty()){
            renderJson(BaseResult.fail("当前暂无比赛结果"));
        }
        else{
            renderJson(DataResult.data(list));
        }
    }

    /**
     * 显示所有项目对应的流程
     * 给两个名字分别起了别名--game_no和process_no
     */
    public void listGameProcess(){
        List<Record> list= gameService.listGameProcess();
        if(list.isEmpty()){
            renderJson(BaseResult.fail("当前暂无比赛"));
        }
        else{
            renderJson(DataResult.data(list));
        }
    }

    /**
     * 修改项目流程
     * 由点击选择事件传入game_no和process_no
     */
    @Param(name = "game_no",required = true)
    @Param(name = "process_no",required = true)
    public void updateGameProcess(){
        if(gameService.updateGameProcess(getPara("game_no"),getPara("process_no"))>0){
            renderJson(BaseResult.ok("项目流程修改成功!"));
        }
        else{
            renderJson(BaseResult.fail("项目流程修改失败!"));
        }
    }

    /**
     * 显示所有当前流程为"人员晋级"的项目
     */
    public void listPromotionGame(){
        List<Record> list= gameService.listByProcess("人员晋级");
        if(list.isEmpty()){
            renderJson(BaseResult.fail("当前暂无比赛"));
        }
        else{
            renderJson(DataResult.data(list));
        }
    }


    /**
     * 显示所有需要晋级的人员
     */
    @Param(name = "game_no",required = true)
    public void showPromotionPeople(){
        Record game=gameService.getGame(getPara("game_no"));
        //当前场次(在创建比赛的时候设置初始值为第一个场次)
        Record turn=gameService.getTurn(game.getStr("now_turn_no"));
        if(game.getInt("type")==0){
            //0表示个人项目
            List<Record> list=gameService.getAllPeopleGame(game.getStr("now_turn_no"));
            renderJson(DataResult.data(list));
        }
        else if(game.getInt("type")==1){
            //1表示团队项目
            List<Record> list=gameService.getAllTeamGame(game.getStr("now_turn_no"));
            renderJson(DataResult.data(list));
        }
        else{
            renderJson(BaseResult.fail("晋级信息错误!"));
        }
    }

    /**
     * 人员晋级管理
     * 若game的now_turn_no为最后一个场次,即在game_turn表中查到的next_turn_no为0,则在成绩录入阶段后将流程置为比赛结束,而不是人员晋级,跳过该阶段
     * 根据前端传入的choose--1表示晋级,0表示为晋级
     * 将晋级的人员存入下一个赛程的参赛信息表(在game_turn表中找到game的下一个赛程的turn_no)
     * 将game的now_turn_no改成下个场次的编号
     */
    @Param(name = "game_no",required = true)
    @Param(name = "user_no",required = true)
    @Param(name = "choose",required = true)
    public void selectPromotionPeople(){
        Record game=gameService.getGame(getPara("game_no"));
        //当前场次(在创建比赛的时候设置初始值为第一个场次)
        Record turn=gameService.getTurn(game.getStr("now_turn_no"));
        if(Objects.equals(getPara("choose"), "1")){
            Grade grade=new Grade();
            grade.setGameNo(getPara("game_no"));
            grade.setTurnNo(turn.getStr("next_turn_no"));
            grade.setNo(getPara("user_no"));
            renderJson(grade.save()?BaseResult.ok("晋级成功!"):BaseResult.fail("晋级失败!"));
        }
        else{
            renderJson(BaseResult.ok("已淘汰!"));
        }
        //如何以列表的形式传递接收?
        //暂时无法实现人员晋级完成后将game中的now_turn_no进行修改...
    }

    /**
     * 成绩查询
     * 输入编号,查询该编号(学号/工号)的参赛成绩
     */
    @Param(name = "user_no",required = true)
    public void findGrade(){
        User user = userService.getByUserNo(getPara("user_no"));
        List<Record> myGame= gameService.getMyGrade(user.getUserNo());
        renderJson(DataResult.data(myGame));
        return;
    }

    /**
     * 成绩修改
     * 输入编号,显示该编号(学号/工号)的参赛成绩后可以进行修改
     * 点击修改,从前端传入user_no,turn_no,和新的grade,排名要根据在该场次内排名算法重新生成,排名算法是什么?
     */
    @Param(name = "user_no",required = true)
    @Param(name = "turn_no",required = true)
    @Param(name = "grade",required = true)
    public void updateGrade(){
        if(gameService.updateGrade(getPara("user_no"),getPara("turn_no"), Double.parseDouble(getPara("grade")))>0){
            renderJson(BaseResult.ok("成绩修改成功!"));
        }
        else{
            renderJson(BaseResult.fail("成绩修改失败!"));
        }

    }

    /***
     * 提交申诉交给管理员
     */
    @Param(name = "user_no",required = true)
    @Param(name = "category",required = true)
    @Param(name = "description",required = true)
    public void submitComplaint(){
        Calendar ca = Calendar.getInstance();
        int day=ca.get(Calendar.DATE);//获取日
        int minute=ca.get(Calendar.MINUTE);//分
        int hour=ca.get(Calendar.HOUR);//小时
        int second=ca.get(Calendar.SECOND);//秒
        String no = String.valueOf(day)+String.valueOf(minute)+String.valueOf(hour)+String.valueOf(second);

        Complaint complaint = new Complaint();
        complaint.setComplaintNo(no);
        complaint.setUserNo(getPara("user_no"));
        complaint.setCategory(Integer.valueOf(getPara("category")));
        complaint.setDescription(getPara("description"));
        complaint.save();
        renderJson(BaseResult.ok("提交申诉成功"));
        return;

    }

//    /***
//     * 在用户界面返回申诉结果
//     */
//    @Param(name = "user_no",required = true)
//    public void complaintResults(){
//        Complaint complaint=new Complaint();
//        complaint=  gameService.getByState(getPara("user_no"));
//
//        if(complaint.getState()==0){
//            renderJson(BaseResult.fail("未查询到申诉结果!"));
//        }else {
//            List<Record> complaintResult = gameService.getComplaintResult(getPara("user_no"));
//            renderJson(DataResult,data(complaintResult));
//            renderJson(BaseResult.fail("成功查询到申诉结果!"));
//        }
//     }
    /***
     * 存入比赛信息
     */
    public void gameInformation(){
        Game game=new Game();
        game.setGameNo(getPara("game_no"));
        game.setName(getPara("name"));
        game.setType(Integer.valueOf(getPara("type")));
        game.setObject(Integer.valueOf(getPara("object")));
        game.setSex(Integer.valueOf(getPara("sex")));
        game.setTurn(Integer.valueOf(getPara("turn")));
        game.setProcessNo(getPara("process_no"));
        game.setNowTurnNo(getPara("now_turn_no"));
        game.setDescription(getPara("description"));
        game.save();
        renderJson(BaseResult.ok("项目信息保存成功"));
        return;
    }

    /***
     * 存入场次信息
     */
    public void gameTurnInformation(){
        GameTurn gameTurn = new GameTurn();
        gameTurn.setGameNo(getPara("game_no"));
        gameTurn.setTurnNo(getPara("turn_no"));
        gameTurn.setName(getPara("name"));
        gameTurn.setPlace(getPara("place"));
        gameTurn.setTime(getPara("time"));
        gameTurn.setNum(Integer.valueOf(getPara("num")));
        gameTurn.setNextTurnNo(getPara("next_turn_no"));
        gameTurn.save();
        renderJson(BaseResult.ok("场次信息保存成功"));
        return;
    }

    /***
     * 为各个赛程分配裁判,存入裁判表
     */
    public void allotReferee(){
        Referee referee = new Referee();
        referee.setGameNo(getPara("game_no"));
        referee.setTurnNo(getPara("turn_no"));
        referee.setUserNo(getPara("user_no"));
        referee.save();
        renderJson(BaseResult.ok("分配裁判成功"));
        return;
    }

    /***
     * 创建队伍存入团队表,并将团队编号存入参赛信息表
     */
    public void foundTeam(){
        Calendar ca = Calendar.getInstance();
        int day=ca.get(Calendar.DATE);//获取日
        int minute=ca.get(Calendar.MINUTE);//分
        int hour=ca.get(Calendar.HOUR);//小时
        int second=ca.get(Calendar.SECOND);//秒
        String no = String.valueOf(day)+String.valueOf(minute)+String.valueOf(hour)+String.valueOf(second);

        Team team = new Team();
        team.setTeamNo(no);
        team.setName(getPara("name"));
        team.save();
        Grade grade = new Grade();
        grade.setGameNo(getPara("game_no"));
        grade.setTurnNo(getPara("turn_no"));
        grade.setNo(no);
        grade.save();
        renderJson(BaseResult.ok("创建队伍成功"));
        return;
    }

    /**
     * 显示“报名结束"的项目
     */
    public void showSignedGame(){
        List<Record> records = gameService.showSignedGame();
        if(StrKit.notNull(records) && records.size() > 0){
            renderJson(DataResult.data(records));
            return;
        }
        renderJson(BaseResult.fail("未查询到报名结束的比赛"));
    }

    /**
     * 存储审核完成的运动员
     */
    @Param(name = "user_no", required = true)
    @Param(name = "game_no", required = true)
    public void saveMember(){
        try{
            Record record = gameService.getSavingMember(getPara("user_no"), getPara("game_no"));
            if(Db.update("enroll", record)){
                Grade grade = new Grade();
                grade.setNo(record.getStr("player_no"));
                grade.setGameNo(record.getStr("game_no"));
                grade.setTurnNo(Db.queryStr("select now_turn_no from game where is_deleted=0 and game_no=?", record.getStr("game_no")));
                renderJson(grade.save() ? BaseResult.ok() : BaseResult.fail("提交失败！"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            renderJson(BaseResult.fail("提交失败！"));
        }
    }
}
