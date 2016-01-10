package com.creditcloud.zmt.event.response;

import com.creditcloud.event.ApiResponse;
import com.creditcloud.model.User;
import com.estate.entity.Client;

public class LoginResponse extends ApiResponse {

	private String access_token;

	private User user;

	private Client client;

	private String[] scope;

	private String createdAt;

	private String token_type;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String[] getScope() {
		return scope;
	}

	public void setScope(String[] scope) {
		this.scope = scope;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

}
