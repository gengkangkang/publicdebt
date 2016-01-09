package com.kevin.model;

/**
 * 个人车辆实体信息
 * 
 * @author gengkangkang
 *
 * 2016年1月9日 下午8:56:06
 * 
 * Email: tjeagle@163.com
 */
public class Car {
	private String ID;
	private String CARNO;
	private String CARBRAND;
	private String CARPRICE;
	private String BUYDATE;
	private String REMARK;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getCARNO() {
		return CARNO;
	}

	public void setCARNO(String cARNO) {
		CARNO = cARNO;
	}

	public String getCARBRAND() {
		return CARBRAND;
	}

	public void setCARBRAND(String cARBRAND) {
		CARBRAND = cARBRAND;
	}

	public String getCARPRICE() {
		return CARPRICE;
	}

	public void setCARPRICE(String cARPRICE) {
		CARPRICE = cARPRICE;
	}

	public String getBUYDATE() {
		return BUYDATE;
	}

	public void setBUYDATE(String bUYDATE) {
		BUYDATE = bUYDATE;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
}
