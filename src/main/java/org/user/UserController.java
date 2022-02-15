package org.user;

import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.common.pojo.DataResult;
import cn.fabrice.jfinal.annotation.Param;
import cn.fabrice.jfinal.annotation.ValidateParam;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.common.interceptor.CorsInterceptor;
import org.common.module.Cookie;
import org.common.module.Game;
import org.common.module.TeamMate;
import org.common.module.User;
import org.game.GameService;

import java.math.BigInteger;
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
    private static final long COOKIE_LIFE = 3600;
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
            Cookie cookie = new Cookie();
            cookie.setUuno(user.getUserNo());
            cookie.setAccessToken("登录");
            cookie.setExpiresIn(BigInteger.valueOf(COOKIE_LIFE));
            cookie.setExpiresTime(BigInteger.valueOf(System.currentTimeMillis() + COOKIE_LIFE));
            cookie.save();
            setCookie("user_no", user.getUserNo(), (int)COOKIE_LIFE);
            return;
        }
    }

    /**
     * 注册
     */
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

    /**
     * 显示个人信息
     * 返回前端个人信息
     */
    public void userDetail(){
        Record record = userService.getUserDetail(getCookie("user_no"));
        renderJson(DataResult.data(record));
    }

    /**
     * 个人信息修改
     * @param name 姓名
     * @param phone 电话号码
     * @param sex 性别
     */
    @Param(name = "name")
    @Param(name = "phone")
    @Param(name = "sex")
    public void changeUserDetail(String name, String phone, String sex){
        User user = userService.getByUserNo(getCookie("user_no"));
        if(StrKit.notNull(name)){
            user.setName(name);
        }
        if(StrKit.notNull(phone)){
            user.setPhone(phone);
        }
        if(StrKit.notNull(sex)){
            user.setSex("男".equals(sex) ? 0 : 1);
        }
        renderJson(user.update() ? BaseResult.ok("修改成功！") : BaseResult.fail("修改失败！"));
    }

    /**
     * 团队信息显示（身为队长时）
     * 返回前端我为队长的所有团队的团队信息
     */
    public void myTeamDetail(){
        List<Record> records = userService.getMyTeamDetail(getCookie("user_no"));
        if(StrKit.notNull(records)){
            renderJson(DataResult.data(records));
            return;
        }
        renderJson(BaseResult.fail("您不是任何团队的队长!"));
    }

    /**
     * 显示所有该裁判能录入成绩的项目
     * 返回前端 该裁判能录入的所有比赛
     */
    public void showScoreInput(){
        List<Record> records = userService.getMyScoreInput(getCookie("user_no"));
        if(StrKit.notNull(records)){
            renderJson(DataResult.data(records));
            return;
        }
        renderJson(BaseResult.fail("目前没有需要您录入成绩的比赛"));
    }

    /**
     * 展示本场比赛能录入成绩的所有运动员的信息
     */
    @Param(name = "game_no",required = true)
    @Param(name = "turn_no",required = true)
    public void showInputMembers(){
        List<Record> members = userService.showInputMembers(getPara("game_no"), getPara("turn_no"));
        if(StrKit.notNull(members)){
            renderJson(DataResult.data(members));
            return;
        }
        renderJson(BaseResult.fail("未查询到需录入成绩的运动员"));
    }

    /**
     * 录入成绩
     */
    @Param(name = "mate_no",required = true)
    @Param(name = "game_no",required = true)
    @Param(name = "turn_no",required = true)
    @Param(name = "grade",required = true)
    @Param(name = "ranking",required = true)
    public void inputScore(){
        Record member = userService.inputScore(getPara("mate_no"), getPara("game_no"), getPara("turn_no"));
        member.set("grade", getPara("grade")).set("ranking", getPara("ranking"));
        renderJson(Db.update("grade", member) ? BaseResult.ok() : BaseResult.fail("成绩录入失败"));
    }

    /**
     * 显示待处理的申诉
     */
    public void showComplaint(){
        List<Record> records = userService.showComplaint();
        if(StrKit.notNull(records)){
            renderJson(DataResult.data(records));
            return;
        }
        renderJson(BaseResult.fail("目前没有未处理的申诉"));
    }

    /**
     * 显示申诉结果
     */
    public void showComplaintResult(){
        List<Record> records = userService.showComplaintResult();
        if(StrKit.notNull(records)){
            renderJson(DataResult.data(records));
            return;
        }
        renderJson(BaseResult.fail("当前没有已处理的申诉"));
    }

    public void resetPassword(){
        User user = userService.getByUserNo(getCookie("user_no"));
        if(getPara("user_no").equals(user.getUserNo())){
            user.setPassword(getPara("password"));
            user.update();
        }
        renderJson(BaseResult.ok("重置密码成功"));
        return;
    }

    /**
     * 我的项目(待比赛--grade中成绩为null)
     * 根据编号(no)联表查询得到参赛者(名字),项目名称,场次名称,比赛时间,比赛场地
     * (1)个人项目:从主页传来的user_no
     * (2)团队项目:根据user_no得到的team_no
     *
     * 在数据库查询时直接联表查询获得所有需要的数据,比在逻辑层面再去筛选存数据更为简便
     * 原方法(毕竟是自己绞尽脑汁想的,还是记录一下):
     * Record user = userService.getPeople(getPara("user_no"));
     * List<Record> myGame= gameService.getMyGame(getPara("user_no"));
     * List<Record> myTeam = userService.myTeam(getPara("user_no"));
     * for(Record record:myTeam){
     * myGame.addAll(gameService.getMyGame(record.getStr("team_no")));
     * }
     * //参赛者
     * List<Record> peopleResult = new ArrayList<Record>();
     * //项目名称
     * List<Record> gameResult = new ArrayList<Record>();
     * //场次名称
     * List<Record> turnResult = new ArrayList<Record>();
     * //遍历将myGame中的game_no和turn_no对应的game和turn传到前端,前端取出其中的项目名称+场次名称+比赛时间+比赛场地
     * for(Record record:myGame){
     * //三条列表:参赛者+项目+场次
     * if (Objects.equals(record.getStr("no"), getPara("user_no"))) {
     * //说明该记录为个人参赛
     * peopleResult.add(user);
     * }
     * else{
     * //否则该记录为团队参赛,传入团队信息
     * peopleResult.add(gameService.getTeam(record.getStr("no")));
     * }
     * Record game = gameService.getGame(record.getStr("game_no"));
     * Record turn = gameService.getTurn(record.getStr("turn_no"));
     * gameResult.add(game);
     * turnResult.add(turn);
     * }
     * Map<String, List<Record>> map = new HashMap<String, List<Record>>();
     * //将list存入map,再renderJson(map)
     * map.put("people",peopleResult);
     * map.put("game",gameResult);
     * map.put("turn",turnResult);
     * renderJson(map);
     * return;
     *
     * 在数据库查询时即获得所需结果显然更为简便
     */
    public void myGame(){
        //个人项目
        Record user = userService.getPeople(getCookie("user_no"));
        List<Record> myGame = gameService.getMyGame(getCookie("user_no"));
        //团队项目
        List<Record> myTeam = userService.myTeam(getPara("user_no"));
        /*遍历根据myTeam中的team_no获取参赛信息,将参赛信息存入myGame*/
        for(Record record:myTeam){
            myGame.addAll(gameService.getTeamGame(record.getStr("team_no")));
        }
        renderJson(DataResult.data(myGame));
        return;
    }

    /**
     * 我的成绩(已完成的比赛)
     * 根据编号(user_no)从成绩表中找到grade
     */
    public void myGrade(){
        User user = userService.getByUserNo(getCookie("user_no"));
        /*有成绩的参赛信息*/
        List<Record> myGame= gameService.getMyGrade(user.getUserNo());
        renderJson(DataResult.data(myGame));
        return;
    }

    /**
     * 团队成绩
     * 根据编号(user_no)找到团队,用团队编号从成绩表中找到grade
     */
    public void teamGrade(){
        List<Record> myTeam= userService.myTeam(getCookie("user_no"));
        List<Record> myGame= new ArrayList<Record>();
        /*遍历根据myTeam中的team_no获取成绩信息,将参赛信息存入myGame*/
        for(Record record:myTeam){
            myGame.addAll(gameService.getTeamGrade(record.getStr("team_no")));
        }
        renderJson(DataResult.data(myGame));
        return;
    }

    /**
     * 团队管理---添加团队成员
     * (1)在我的团队界面显示团队
     * (2)点击进入各个团队,其中自己身份为队长的团队处可以点击进行团队成员管理
     * (3)此时团队编号由点击跳转事件传入成员管理界面
     * (4)输入成员学号,将成员添加进入团队
     */
    @Param(name = "member_no",required = true)
    @Param(name = "team_no",required = true)
    public void addMember(){
        TeamMate teamMate=new TeamMate();
        User user=userService.getByUserNo(getPara("member_no"));
        if(user==null){
            renderJson(BaseResult.fail("当前成员不存在!"));
            return;
        }
        if(userService.selectMember(getPara("member_no"),getPara("team_no"))){
            renderJson(BaseResult.fail("当前成员已在团队中!"));
            return;
        }
        else{
            teamMate.setMateNo(getPara("member_no"));
            teamMate.setTeamNo(getPara("team_no"));
            teamMate.setRanks(0);
            renderJson(teamMate.save()?BaseResult.ok("添加成功！"):BaseResult.fail("添加失败"));
        }
    }

    /**
     * 团队管理---删除团队成员
     * (1)在我的团队界面显示团队
     * (2)点击进入各个团队,其中自己身份为队长的团队处可以点击进行团队成员管理
     * (3)此时团队编号由点击跳转事件传入成员管理界面
     * (4)输入成员学号,将成员从团队中删除(is_deleted置为1)
     */
    @Param(name = "member_no",required = true)
    @Param(name = "team_no",required = true)
    public void deleteMember(){
        User user=userService.getByUserNo(getPara("member_no"));
        if(user==null){
            renderJson(BaseResult.fail("当前成员不存在!"));
            return;
        }
        if(!userService.selectMember(getPara("member_no"),getPara("team_no"))){
            renderJson(BaseResult.fail("当前成员不在团队中!"));
            return;
        }

        else{
            if(userService.deleteMember(getPara("member_no"),getPara("team_no"))>0){
                renderJson(BaseResult.ok("删除成功！"));
            }
            else{
                renderJson(BaseResult.fail("删除失败!"));
            }
        }
    }

    /**
     * 团队管理---解散团队
     * (1)在我的团队界面显示团队
     * (2)点击进入各个团队,其中自己身份为队长的团队处可以点击进行团队成员管理
     * (3)此时团队编号由点击跳转事件传入成员管理界面
     * (4)根据团队编号将团队从团队表中删除,同时删除该团队的成员
     */
    @Param(name = "team_no",required = true)
    public void deleteTeam(){
        if(userService.deleteTeam(getPara("team_no"))>0){
            List<Record> member= userService.findMember(getPara("team_no"));
            for(Record record:member){
                userService.deleteMember(record.getStr("mate_no"),getPara("team_no"));
            }
            renderJson(BaseResult.ok("删除成功！"));
        }
        else{
            renderJson(BaseResult.fail("删除失败!"));
        }
    }
}
