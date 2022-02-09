#sql("getByUserNo")
select * from user where user_no = #para(user_no) and is_deleted=0
#end

#sql("getByUserTel")
select * from user where phone = #para(phone) and is_deleted=0
#end

#sql("getTeamByUserNo")
select team_mate.team_no as 团队编号,team_mate.mate_no as 学号,user.name as 姓名
from team,team_mate,user
where team.mate_no = #para(user_no) and team.team_no = team_mate.team_no
and user.user_no = team_mate.mate_no and team.is_deleted=0
and team_mate.is_deleted=0 and user.is_deleted=0
#end