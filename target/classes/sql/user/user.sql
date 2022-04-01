#sql("getByUserNo")
select * from user where user_no = #para(user_no) and is_deleted=0
#end

#sql("getByUserTel")
select * from user where phone = #para(phone) and is_deleted=0
#end

#sql("myTeam")
select * from team where team_no in (select team_no from team_mate where mate_no = #para(user_no) and is_deleted=0) and is_deleted=0
#end

#sql("selectMember")
select mate_no from team_mate where team_no=#para(team_no) and is_deleted=0
#end

#sql("deleteMember")
update team_mate set is_deleted=1 where mate_no=#para(member_no) and team_no=#para(team_no) and is_deleted=0
#end

#sql("deleteTeam")
update team set is_deleted=1 where team_no=#para(team_no) and is_deleted=0
#end

#sql("getUserDetailByNo")
select user.user_no as user_no,user.name as user_name,school.name as school_name,user.phone as phone,user.sex as sex
from user,school where user.user_no = #para(user_no) and user.is_deleted=0 and user.school_no = school.school_no and school.is_deleted=0
#end

#sql("getMyTeam")
select team_mate.team_no as team_no from user,team_mate where user.user_no = team_mate.mate_no and user.is_deleted=0
and team_mate.ranks=1 and team_mate.is_deleted=0 and user.user_no=#para(user_no)
#end

#sql("getMyTeamDetail")
select team.team_no as team_no,team.name as team_name,user.name as user_name,user.user_no as user_no,user.sex as sex,team_mate.ranks as user_ranks
from user,team,team_mate
where user.user_no = team_mate.mate_no and user.is_deleted=0 and team_mate.is_deleted=0 and team_mate.team_no = team.team_no
  and team.is_deleted=0 and team.team_no=#para(team_no)
#end

#sql("getMyScoreInput")
select game.game_no as game_no,game.name as game_name,game_turn.name as turn_name,game_turn.num as num
from game,game_turn,user,referee
where user.user_no=#para(user_no) and user.is_deleted=0 and referee.user_no=user.user_no and referee.is_deleted=0
  and referee.game_no=game.game_no and game.is_deleted=0 and referee.turn_no=game_turn.turn_no and game_turn.is_deleted=0
#end

#sql("getInputMember")
select * from grade where game_no=#para(gameNo) and turn_no=#para(turnNo) and no=#para(mateNo)
#end

#sql("showInputMembers")
select user.name as user_name,user.user_no as user_no,grade.ranking as ranking from user,grade
where user.is_deleted=0 and user.user_no=grade.no and grade.is_deleted=0 and grade.game_no=#para(gameNo) and grade.turn_no=#para(turnNo)
#end

#sql("showComplaint")
select complaint_no,category,user_no,description,state from complaint where is_deleted=0 and state=0
#end

#sql("showComplaintResult")
select complaint_no,category,user_no,description,result from complaint where is_deleted=0 and state=0
#end