#sql("listByNoProcess")
select * from game where process_no not in (select process_no from process where process.name = #para(name) and is_deleted=0)
#end