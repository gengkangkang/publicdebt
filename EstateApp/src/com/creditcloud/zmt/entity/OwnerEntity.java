package com.creditcloud.zmt.entity;

/**
 * @author yangkx
 * @mark ������ʵ����Ϣ
 */
public class OwnerEntity {
	private String ID;
	private String NAME;
	private String IDTYPE;
	private String IDNUMBER;
	private String HOURSEID;
	private String CARID;
	private String PROPERTYPAYINFO;
	private String LEVEL;
	private String REMARK;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getIDTYPE() {
		return IDTYPE;
	}
	public void setIDTYPE(String iDTYPE) {
		IDTYPE = iDTYPE;
	}
	public String getIDNUMBER() {
		return IDNUMBER;
	}
	public void setIDNUMBER(String iDNUMBER) {
		IDNUMBER = iDNUMBER;
	}
	public String getHOURSEID() {
		return HOURSEID;
	}
	public void setHOURSEID(String hOURSEID) {
		HOURSEID = hOURSEID;
	}
	public String getCARID() {
		return CARID;
	}
	public void setCARID(String cARID) {
		CARID = cARID;
	}
	public String getPROPERTYPAYINFO() {
		return PROPERTYPAYINFO;
	}
	public void setPROPERTYPAYINFO(String pROPERTYPAYINFO) {
		PROPERTYPAYINFO = pROPERTYPAYINFO;
	}
	public String getLEVEL() {
		return LEVEL;
	}
	public void setLEVEL(String lEVEL) {
		LEVEL = lEVEL;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
}
