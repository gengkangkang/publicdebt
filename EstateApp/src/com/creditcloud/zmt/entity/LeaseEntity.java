package com.creditcloud.zmt.entity;

/**
 * @author yangkx
 * @mark 房屋租赁实体信息
 */
public class LeaseEntity {
	private String ID;
	private String USERID;
	private String HOURSEID;
	private String LEASEHOURSE;
	private String LEASEDATE;
	private String REMARK;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getHOURSEID() {
		return HOURSEID;
	}
	public void setHOURSEID(String hOURSEID) {
		HOURSEID = hOURSEID;
	}
	public String getLEASEHOURSE() {
		return LEASEHOURSE;
	}
	public void setLEASEHOURSE(String lEASEHOURSE) {
		LEASEHOURSE = lEASEHOURSE;
	}
	public String getLEASEDATE() {
		return LEASEDATE;
	}
	public void setLEASEDATE(String lEASEDATE) {
		LEASEDATE = lEASEDATE;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
}
