#sql("getByUserNo")
select * from user where user_no = #para(user_no) and is_deleted=0
#end

#sql("getByUserTel")
select * from user where phone = #para(phone) and is_deleted=0
#end