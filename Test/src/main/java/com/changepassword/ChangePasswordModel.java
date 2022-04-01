package com.changepassword;

public class ChangePasswordModel {

	private String loginName;
	private String currentPassword;
	private String newPassword;

	public ChangePasswordModel(String loginName, String currentPassword, String newPassword) {

		this.loginName = loginName;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
