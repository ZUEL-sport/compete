#sql("listByNoProcess")
select id,name from game where process_no not in (select process_no from process where process.name = #para(name) and is_deleted=0)
#end

#sql("listByProcess")
select id,name from game where process_no in (select process_no from process where process.name = #para(name) and is_deleted=0)
#end

#sql("getGame")
select * from game where game_no = #para(game_no) and is_deleted=0
#end

#sql("getTurn")
select * from game_turn where turn_no = #para(turn_no) and is_deleted=0
#end

#sql("getTeam")
select * from team where team_no = #para(team_no) and is_deleted=0
#end

#sql("getMyGame")
select user.name as user_name,game.name as game_name,game_turn.name as turn_name,time,place
from user,game,game_turn,grade where grade.no=user.user_no and grade.game_no=game.game_no and grade.turn_no=game_turn.turn_no
    and user.user_no = #para(user_no) and grade.is_deleted=0 and grade is null and ranking is null
#end

#sql("getTeamGame")
select team.name as team_name,game.name as game_name,game_turn.name as turn_name,time,place
from team,game,game_turn,grade where grade.no=team.team_no and grade.game_no=game.game_no and grade.turn_no=game_turn.turn_no
    and team.team_no = #para(team_no) and grade.is_deleted=0 and grade is null and ranking is null
#end

#sql("getAllPeopleGame")
select user.name as user_name,game.name as game_name,game_turn.name as turn_name,grade,ranking
from user,game,game_turn,grade where grade.no=user.user_no and grade.game_no=game.game_no and grade.turn_no=game_turn.turn_no
    and grade.turn_no = #para(turn_no) and grade.is_deleted=0 and grade is not null and ranking is not null
#end

#sql("getAllTeamGame")
select team.name as team_name,game.name as game_name,game_turn.name as turn_name,grade,ranking
from team,game,game_turn,grade where grade.no=team.team_no and grade.game_no=game.game_no and grade.turn_no=game_turn.turn_no
    and grade.turn_no = #para(turn_no) and grade.is_deleted=0 and grade is not null and ranking is not null
#end

#sql("getMyGrade")
/*为什么不能写成grade.grade,grade.ranking*/
select user.name as user_name,game.name as game_name,game_turn.name as turn_name,grade,ranking
from user,game,game_turn,grade where grade.no=user.user_no and grade.game_no=game.game_no and grade.turn_no=game_turn.turn_no
    and user.user_no = #para(user_no) and grade.is_deleted=0 and grade is not null and ranking is not null
#end

#sql("getTeamGrade")
select team.name as team_name,game.name as game_name,game_turn.name as turn_name,grade,ranking
from team,game,game_turn,grade where grade.no=team.team_no and grade.game_no=game.game_no and grade.turn_no=game_turn.turn_no
    and team.team_no = #para(team_no) and grade.is_deleted=0 and grade.grade is not null and grade.ranking is not null
#end

#sql("listByGrade")
select * from grade where grade.game_no = #para(game_no) and grade.turn_no = #para(turn_no) and is_deleted=0
#end

#sql("updateGameProcess")
update game set process_no=#para(process_no) where game_no=#para(game_no) and is_deleted=0
#end

#sql("listGameProcess")
select game.name as game_name,process.name as process_name from game,process where game.process_no=process.process_no and game.is_deleted=0 and process.is_deleted=0
#end

#sql("updateGrade")
update grade set grade=#para(grade) where no=#para(user_no) and turn_no=#para(turn_no) and is_deleted=0
#end