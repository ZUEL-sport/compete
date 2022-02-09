#sql("listByNoProcess")
select * from game where process_no not in (select process_no from process where process.name = #para(name) and is_deleted=0) and is_deleted=0
#end

#sql("gameDetail")
select * from game where game_no = #para(game_no)
#end

#sql("listByGrade")
select *
from grade
where grade.game_no = #para(game_no)
  and grade.turn_no = #para(turn_no)
  and is_deleted=0
    #end