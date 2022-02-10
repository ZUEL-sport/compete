#sql("listByNoProcess")
select * from game where process_no not in (select process_no from process where process.name = #para(name) and is_deleted=0
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
select * from grade where no = #para(no) and is_deleted=0 and grade is null and ranking is null
#end

#sql("getMyGrade")
select * from grade where no = #para(no) and is_deleted=0 and grade is not null and ranking is not null
#end

#sql("listByGrade")
select *
from grade
where grade.game_no = #para(game_no)
  and grade.turn_no = #para(turn_no)
  and is_deleted=0
    #end

