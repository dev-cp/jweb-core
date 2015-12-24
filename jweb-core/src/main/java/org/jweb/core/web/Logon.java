package org.jweb.core.web;

public class Logon {
	private String code;
	private String email;
	private String name;

	public Logon() {

	}

	public Logon(String code, String email, String name) {
		this.code = code;
		this.email = email;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}