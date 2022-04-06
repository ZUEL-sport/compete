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
select user.user_no as user_no,user.name as user_name,school.name as school_name,user.phone as phone,user.sex as sex,user.password as password
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
select game.game_no as game_no,game_turn.turn_no as turn_no,game.name as game_name,game_turn.name as turn_name
from game,game_turn,referee
where referee.user_no="004"
  and referee.game_no=game.game_no and referee.turn_no=game.now_turn_no and referee.turn_no=game_turn.turn_no and game.process_no="05"
    #end

#sql("getInputMember")
select * from grade where game_no=#para(gameNo) and turn_no=#para(turnNo) and no=#para(mateNo)
#end

#sql("showInputMembers")
select user.name as user_name,user.user_no as user_no,game.name as game_name,game_turn.name as turn_name,grade.grade as grade,grade.ranking as ranking
from user,grade,game,game_turn
where user.user_no=grade.no and grade.game_no=game.game_no and grade.turn_no=game_turn.turn_no and grade.game_no="01" and grade.turn_no="01"
#end

#sql("showComplaint")
select complaint.id as id,complaint.created_time as created_time,complaint_no,category,user.name as name,description from complaint,user where user.user_no=complaint.user_no and state=0
#end

#sql("showComplaintResult")
select complaint_no,category,user_no,description,result from complaint where is_deleted=0 and state=0
#end