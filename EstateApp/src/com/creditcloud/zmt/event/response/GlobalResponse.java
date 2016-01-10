package com.creditcloud.zmt.event.response;

import com.creditcloud.event.ApiResponse;

public class GlobalResponse extends ApiResponse{
    /**
     * 返回具体数据
     */
    private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
