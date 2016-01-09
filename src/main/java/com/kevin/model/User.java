package com.kevin.model;
/**
 * 
 * @author  gengkangkang
 *
 * 2015年11月22日 上午12:29:27
 */
public class User {

    private int id;
    private int state;
    private String nickname;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
