#----------用户模块---------------#
#namespace("user")
#include("user/user.sql")
#end
#--------------------------------#

#-----另一种形式下的sql语句书写----#
/*#sql("getByPhone1")
select * from user where phone = ? and is_deleted=0
    #end

#sql("getByPhone2")
select * from user where phone=#para(phone) and is_deleted=0
    #end
*/
#------------------------------#