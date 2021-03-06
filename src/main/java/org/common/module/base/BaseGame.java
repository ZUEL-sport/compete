package org.common.module.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGame<M extends BaseGame<M>> extends Model<M> implements IBean {

	/**
	 * 主键;主键唯一标识
	 */
	public void setId(java.math.BigInteger id) {
		set("id", id);
	}
	
	/**
	 * 主键;主键唯一标识
	 */
	public java.math.BigInteger getId() {
		return get("id");
	}
	
	/**
	 * 创建时间;创建时间
	 */
	public void setCreatedTime(java.util.Date createdTime) {
		set("created_time", createdTime);
	}
	
	/**
	 * 创建时间;创建时间
	 */
	public java.util.Date getCreatedTime() {
		return get("created_time");
	}
	
	/**
	 * 更新时间;更新时间
	 */
	public void setUpdatedTime(java.util.Date updatedTime) {
		set("updated_time", updatedTime);
	}
	
	/**
	 * 更新时间;更新时间
	 */
	public java.util.Date getUpdatedTime() {
		return get("updated_time");
	}
	
	/**
	 * 是否删除;0-未删除;1-已删除
	 */
	public void setIsDeleted(java.lang.Integer isDeleted) {
		set("is_deleted", isDeleted);
	}
	
	/**
	 * 是否删除;0-未删除;1-已删除
	 */
	public java.lang.Integer getIsDeleted() {
		return getInt("is_deleted");
	}
	
	/**
	 * 项目编号;项目编号
	 */
	public void setGameNo(java.lang.String gameNo) {
		set("game_no", gameNo);
	}
	
	/**
	 * 项目编号;项目编号
	 */
	public java.lang.String getGameNo() {
		return getStr("game_no");
	}
	
	/**
	 * 项目名称;比赛的名称
	 */
	public void setName(java.lang.String name) {
		set("name", name);
	}
	
	/**
	 * 项目名称;比赛的名称
	 */
	public java.lang.String getName() {
		return getStr("name");
	}
	
	/**
	 * 类别编号;0-个人,1-团队
	 */
	public void setType(java.lang.Integer type) {
		set("type", type);
	}
	
	/**
	 * 类别编号;0-个人,1-团队
	 */
	public java.lang.Integer getType() {
		return getInt("type");
	}
	
	/**
	 * 参赛人群;0-不限,1-学生,2-老师
	 */
	public void setObject(java.lang.Integer object) {
		set("object", object);
	}
	
	/**
	 * 参赛人群;0-不限,1-学生,2-老师
	 */
	public java.lang.Integer getObject() {
		return getInt("object");
	}
	
	/**
	 * 性别要求;0-不限,1-男,2-女
	 */
	public void setSex(java.lang.Integer sex) {
		set("sex", sex);
	}
	
	/**
	 * 性别要求;0-不限,1-男,2-女
	 */
	public java.lang.Integer getSex() {
		return getInt("sex");
	}
	
	/**
	 * 比赛总场次;全部赛程所需的场次
	 */
	public void setTurn(java.lang.Integer turn) {
		set("turn", turn);
	}
	
	/**
	 * 比赛总场次;全部赛程所需的场次
	 */
	public java.lang.Integer getTurn() {
		return getInt("turn");
	}
	
	/**
	 * 流程编号;流程编号
	 */
	public void setProcessNo(java.lang.String processNo) {
		set("process_no", processNo);
	}
	
	/**
	 * 流程编号;流程编号
	 */
	public java.lang.String getProcessNo() {
		return getStr("process_no");
	}
	
	/**
	 * 当前场次编号;表示当前场次,用于流程中成绩录入时确定场次
	 */
	public void setNowTurnNo(java.lang.String nowTurnNo) {
		set("now_turn_no", nowTurnNo);
	}
	
	/**
	 * 当前场次编号;表示当前场次,用于流程中成绩录入时确定场次
	 */
	public java.lang.String getNowTurnNo() {
		return getStr("now_turn_no");
	}
	
	/**
	 * 项目描述;项目的介绍
	 */
	public void setDescription(java.lang.String description) {
		set("description", description);
	}
	
	/**
	 * 项目描述;项目的介绍
	 */
	public java.lang.String getDescription() {
		return getStr("description");
	}
	
}
