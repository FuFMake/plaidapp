package com.aradata.plaidapp.model.payloads;

public class ApiResponse {
	private Boolean success;
	private String status;
	private String message;

	public ApiResponse(Boolean success, String message) {
		this.success = success;
		this.message = message;
		if (success)
				status = "OK";
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}