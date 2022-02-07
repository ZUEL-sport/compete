package org.common.module.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseEnroll<M extends BaseEnroll<M>> extends Model<M> implements IBean {

	/**
	 * 主键 主键唯一标识
	 */
	public void setId(java.math.BigInteger id) {
		set("id", id);
	}
	
	/**
	 * 主键 主键唯一标识
	 */
	public java.math.BigInteger getId() {
		return get("id");
	}
	
	/**
	 * 创建时间 创建时间
	 */
	public void setCreatedTime(java.util.Date createdTime) {
		set("created_time", createdTime);
	}
	
	/**
	 * 创建时间 创建时间
	 */
	public java.util.Date getCreatedTime() {
		return get("created_time");
	}
	
	/**
	 * 更新时间 更新时间
	 */
	public void setUpdatedTime(java.util.Date updatedTime) {
		set("updated_time", updatedTime);
	}
	
	/**
	 * 更新时间 更新时间
	 */
	public java.util.Date getUpdatedTime() {
		return get("updated_time");
	}
	
	/**
	 * 是否删除 0-未删除;1-已删除
	 */
	public void setIsDeleted(java.lang.Integer isDeleted) {
		set("is_deleted", isDeleted);
	}
	
	/**
	 * 是否删除 0-未删除;1-已删除
	 */
	public java.lang.Integer getIsDeleted() {
		return getInt("is_deleted");
	}
	
	/**
	 * 学号 报名学生的学号
	 */
	public void setPlayerNo(java.lang.String playerNo) {
		set("player_no", playerNo);
	}
	
	/**
	 * 学号 报名学生的学号
	 */
	public java.lang.String getPlayerNo() {
		return getStr("player_no");
	}
	
	/**
	 * 项目编号 报名的项目的编号
	 */
	public void setGameNo(java.lang.String gameNo) {
		set("game_no", gameNo);
	}
	
	/**
	 * 项目编号 报名的项目的编号
	 */
	public java.lang.String getGameNo() {
		return getStr("game_no");
	}
	
	/**
	 * 是否通过 0-待审核,1-通过,2-未通过
	 */
	public void setIsPass(java.lang.Integer isPass) {
		set("is_pass", isPass);
	}
	
	/**
	 * 是否通过 0-待审核,1-通过,2-未通过
	 */
	public java.lang.Integer getIsPass() {
		return getInt("is_pass");
	}
	
}
