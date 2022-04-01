package com.signup;

public class SigninModel {

	private String firstName;
	private String lastName;
	private String loginName;
	private String password;

	public SigninModel(String firstName, String lastName, String loginName, String password) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.loginName = loginName;
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
